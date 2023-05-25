package com.example.p2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.p2.databinding.FragmentDetailsProductBinding

private const val ARG_PARAM1 = "product"
class DetailsProductFragment : Fragment() {
    var product: Product? = null
    private var _binding: FragmentDetailsProductBinding? = null
    private val binding
        get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentDetailsProductBinding.inflate(inflater, container, false)

        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product?
        }

        binding.titleTV.text = product!!.name

        return binding.root
    }

    companion object {
        fun newInstance(product: Product?) = DetailsProductFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, product)
            }
        }
    }
}