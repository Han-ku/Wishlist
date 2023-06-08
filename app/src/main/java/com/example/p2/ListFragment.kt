package com.example.p2

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p2.databinding.FragmentListBinding
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var delete: TextView
    private lateinit var cancel: TextView
    private var allProducts: List<Product> = emptyList()

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as ProductsApplication).repository)
    }

    @SuppressLint("SuspiciousIndentation", "MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        createNotificationChannel()

        var adapter = ProductsAdapter(clickListener = {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, DetailsProductFragment.newInstance(it))
            transaction.addToBackStack(null)
            transaction.commit()
        }){
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_delete)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            delete = dialog.findViewById(R.id.delete)
            cancel = dialog.findViewById(R.id.cancel)

            delete.setOnClickListener { which ->
                viewModel.delete(it)
                dialog.dismiss()
            }

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter  = adapter
        }

        viewModel.products.observe(requireActivity()) { products ->
            val sortedList = products?.sortedBy { it.id }

            if (sortedList != null) {
                adapter.submitList(sortedList)
                allProducts = sortedList
                context?.let { checkDistance(it) }
            } else {
                adapter.submitList(emptyList())
            }
        }

        binding.addBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, AddProductFragment())
            transaction.addToBackStack("addProductFragment")
            transaction.commit()
        }

        return binding.root
    }

    fun checkDistance(context: Context) {
        if (allProducts.isNotEmpty()) {
            val fragmentContext = context ?: return

            LocationTracker(fragmentContext).getCurrentLocation { location ->
                if (location != null) {
                    val currentUserLocation: LatLng = location
                    val distanceThreshold = 100.0

                    for (product in allProducts) {
                        val productLocation: LatLng = convertAddressToLatLng(context, product.location!!)!!

                        if (productLocation != null) {
                            val distance: Double = calculateDistance(currentUserLocation, productLocation)

                            if (distance <= distanceThreshold) {
                                createNotification(fragmentContext, product.name!!, product.id!!)
                            }
                        }
                    }
                }
            }
        }
    }

    fun convertAddressToLatLng(context: Context, address: String): LatLng? {
        val geocoder = Geocoder(context)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses!!.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                return LatLng(latitude, longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun calculateDistance(latLng1: LatLng, latLng2: LatLng): Double {
        val earthRadius = 6371.0

        val lat1 = Math.toRadians(latLng1.latitude)
        val lon1 = Math.toRadians(latLng1.longitude)
        val lat2 = Math.toRadians(latLng2.latitude)
        val lon2 = Math.toRadians(latLng2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        val distance = earthRadius * c
        return distance * 1000 // Convert distance to meters
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
            lightColor = Color.BLUE
            enableLights(true)
        }
        val manager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun createNotification(context: Context, name: String, productId: Int) {
        val detailsIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("productId", productId)
            putExtra("fragmentToLoad", "DetailsProductFragment")
        }

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(detailsIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val locationTracker = LocationTracker(context)

        if (locationTracker.checkLocationPermission()) {
            locationTracker.startLocationUpdates()
            locationTracker.getCurrentLocation { location ->

                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Title")
                    .setContentText("Product $name is nearby.")
                    .setSmallIcon(R.drawable.baseline_notifications_none_24)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
//                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .build()

                val notificationManager = NotificationManagerCompat.from(context)

                notificationManager.notify(NOTIFICTION_ID, notification)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        checkDistance(requireContext())
        LocationTracker(requireContext()).stopLocationUpdates()
    }

    private companion object {
        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "channelName"
        private const val NOTIFICTION_ID = 0
    }

}