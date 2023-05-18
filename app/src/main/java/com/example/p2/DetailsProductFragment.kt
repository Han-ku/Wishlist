package com.example.p2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.p2.databinding.FragmentDetailsProductBinding

class DetailsProductFragment : Fragment() {

    private var _binding: FragmentDetailsProductBinding? = null
    private val binding
        get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentDetailsProductBinding.inflate(inflater, container, false)

        return binding.root
    }
}