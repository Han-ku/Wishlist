package com.example.p2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p2.databinding.FragmentAddProductBinding
import java.io.ByteArrayOutputStream
import java.util.*

private const val ARG_PARAM1 = "product"
private val REQUEST_IMAGE_CAPTURE = 1
private val REQUEST_CAMERA_PERMISSION = 2

class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as ProductsApplication).repository)
    }

    var product: Product? = null

    private lateinit var photoAdapter: PhotoAdapter
    private val photos: MutableList<Bitmap> = mutableListOf()
    private lateinit var photo: Bitmap

    private lateinit var delete: TextView
    private lateinit var cancel: TextView
    private lateinit var image: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentAddProductBinding.inflate(inflater, container, false)

        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product?
        }

        binding.imageView.setOnClickListener {
            openImageDialog()
        }

        binding.locationImage.setOnClickListener {
            val mapFragment = MapsFragment()
            mapFragment.setOnLocationSelectedListener(object : OnLocationSelectedListener {
                override fun onLocationSelected(address: String) {
                    binding.locationTV.text = address
                }
            })
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainerView, mapFragment)
            transaction.addToBackStack("mapsFragment")
            transaction.commit()
        }


        binding.saveBtn.setOnClickListener {
            val allInfo = checkInfo()

            if (allInfo) {
                if (product == null) {
                    viewModel.insert(Product(null, binding.nameET.text.toString(), binding.locationTV.text.toString(), binding.descriptionET.text.toString(), getСompressedPhotoWithText(binding.descriptionET.text.toString())))
                } else {
                    viewModel.update(Product(product!!.id, binding.nameET.text.toString(), binding.locationTV.text.toString(), binding.descriptionET.text.toString(), getСompressedPhotoWithText(binding.descriptionET.text.toString())))
                }

                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, ListFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        photoAdapter = PhotoAdapter(photos)
        binding.imageRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.imageRV.adapter = photoAdapter

        binding.addImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCameraIntent()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            }
        }

        if(product != null) {
            binding.nameET.text = Editable.Factory.getInstance().newEditable(product!!.name)
            binding.locationTV.text = product!!.location
            binding.descriptionET.text = Editable.Factory.getInstance().newEditable(product!!.description)
        }

        return binding.root
    }

    private fun startCameraIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraIntent()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //photos.add(imageBitmap)
            photo = imageBitmap
            //val stream = ByteArrayOutputStream()
            //imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            //photo = stream.toByteArray()
            binding.imageView.setImageBitmap(resizeBitmap(imageBitmap, 500, 600))

            //photoAdapter.notifyItemInserted(photos.size - 1)
        }
    }

    fun getСompressedPhotoWithText(userText : String) : ByteArray {
        val bitmapWithText = Bitmap.createBitmap(photo.width, photo.height, photo.config)
        val canvas = Canvas(bitmapWithText)
        canvas.drawBitmap(photo, 0f, 0f, null)

        val paint = Paint()
        paint.textSize = 32f
        paint.color = Color.RED

        canvas.drawText(userText, 35F, 35F, paint)

        val stream = ByteArrayOutputStream()
        bitmapWithText.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    @SuppressLint("MissingInflatedId")
    fun openImageDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_image)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        image = dialog.findViewById(R.id.photo)
        //image.setImageBitmap(resizeBitmap(photo, 500, 600))

        delete = dialog.findViewById(R.id.delete)
        cancel = dialog.findViewById(R.id.cancel)

//        TODO add functionality
        delete.setOnClickListener { which ->
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun checkInfo(): Boolean {
        var allInfo = true

        if (binding.nameET.text.toString().isEmpty()) {
            allInfo = false
            binding.nameErrorTV.visibility = View.VISIBLE
        } else {
            binding.nameErrorTV.visibility = View.GONE
        }

        if (binding.locationTV.text.isNullOrEmpty()) {
            allInfo = false
            binding.locationErrorTV.visibility = View.VISIBLE
        } else {
            binding.locationErrorTV.visibility = View.GONE
        }

        return allInfo
    }

    fun resizeBitmap(photo: Bitmap, desiredWidth: Int, desiredHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createScaledBitmap(photo, desiredWidth, desiredHeight, true)
        return scaledBitmap
    }

    companion object {
        fun newInstance(product: Product?) = AddProductFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, product)
            }
        }
    }


}