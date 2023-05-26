package com.example.p2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.p2.databinding.FragmentDetailsProductBinding
import com.example.p2.databinding.FragmentMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import java.util.*

const val RANGE = 10.0
const val RANGE2 = 100.0
private const val STROKE_WIDTH = 10f


class MapsFragment() : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding
        get()=_binding!!

    private lateinit var map: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        turnOnLocation()
        googleMap.setOnMapClickListener(::onMapClick)
    }


    private lateinit var save: TextView
    private lateinit var cancel: TextView
    private lateinit var location: TextView
    var loc = ""

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    @SuppressLint("MissingPermission")
    private val onPermisonResult = { results: Map<String, Boolean> ->
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            onPermisonResult
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun turnOnLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun onMapClick(latLng: LatLng) {
        drawCircle(latLng)
        setAdress(latLng)
        askForSave()
    }

    private fun askForSave() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_location)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        save = dialog.findViewById(R.id.save)
        cancel = dialog.findViewById(R.id.cancel)
        location = dialog.findViewById(R.id.locationTV)
        location.text = loc


        save.setOnClickListener { which ->
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val previousFragment = fragmentManager.fragments[fragmentManager.fragments.size - 2]

            val args = Bundle()
            args.putString("latLng", loc)
            previousFragment.arguments = args

            fragmentManager.popBackStack()
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun drawCircle(latLng: LatLng) {
//        TODO change circle to photo outline_location_on_24
        var circle = CircleOptions()
            .strokeColor(Color.RED)
            .radius(RANGE)
            .center(latLng)
            .strokeWidth(STROKE_WIDTH)
        map.apply {
            clear()
            addCircle(circle)
        }
    }

    fun setAdress(latLng: LatLng) {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latLng.latitude,
            latLng.longitude,
            1
        ) as List<Address>
        val street: String = addresses[0].getAddressLine(0)
        loc = street
    }
}