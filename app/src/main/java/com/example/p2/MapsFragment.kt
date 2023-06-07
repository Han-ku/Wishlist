package com.example.p2

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.p2.databinding.FragmentMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding
        get()=_binding!!

    private lateinit var map: GoogleMap

    var loc = ""
    private var onLocationSelectedListener: OnLocationSelectedListener? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(OnMapReadyCallback { googleMap ->
            map = googleMap
            map.isMyLocationEnabled = true

            val locationTracker = LocationTracker(requireContext())

            locationTracker.getCurrentLocation { location ->
                    val latLng = LatLng(location!!.latitude, location.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }

//            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.setOnMapClickListener(::onMapClick)

            binding.zoomInBtn.setOnClickListener {
                map.animateCamera(CameraUpdateFactory.zoomIn())
            }

            binding.zoomOutBtn.setOnClickListener {
                map.animateCamera(CameraUpdateFactory.zoomOut())
            }
        })
    }

    private fun onMapClick(latLng: LatLng) {
        drawMarker(latLng)
        loc = LocationTracker(requireContext()).setAdress(latLng)
        getLocation()
    }

    private fun getLocation() {
        binding.locationLayout.visibility = View.VISIBLE
        binding.locationTV.text = loc
        onLocationSelectedListener?.onLocationSelected(loc)

        binding.saveBtn.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun drawMarker(latLng: LatLng) {
        val width = 100
        val height = 110

        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.location)
        val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
        val bitmapDescriptor: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(bitmapDescriptor)

        map.apply {
            clear()
            addMarker(markerOptions)
        }
    }


    fun setOnLocationSelectedListener(listener: OnLocationSelectedListener) {
        onLocationSelectedListener = listener
    }
}

interface OnLocationSelectedListener {
    fun onLocationSelected(address: String)
}