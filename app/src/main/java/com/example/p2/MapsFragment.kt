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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.p2.databinding.FragmentMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import java.util.*

const val RANGE = 10.0
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

    var loc = ""
    private var onLocationSelectedListener: OnLocationSelectedListener? = null
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

    fun setOnLocationSelectedListener(listener: OnLocationSelectedListener) {
        onLocationSelectedListener = listener
    }
}

interface OnLocationSelectedListener {
    fun onLocationSelected(address: String)
}