package com.example.p2

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.p2.databinding.FragmentAddProductBinding

private const val ARG_PARAM1 = "product"
class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding
        get()=_binding!!

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as ProductsApplication).repository)
    }

    var product: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentAddProductBinding.inflate(inflater, container, false)

        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product?
        }

        binding.saveBtn.setOnClickListener {
            if(product == null) {
                viewModel.insert(Product(null, binding.nameET.text.toString(), binding.locationTV.text.toString(), binding.descriptionET.text.toString()))
            } else if(product != null) {
                viewModel.update(Product(product!!.id, binding.nameET.text.toString(), binding.locationTV.text.toString(), binding.descriptionET.text.toString()))
            }

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, ListFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        if(product != null) {
            binding.nameET.text = Editable.Factory.getInstance().newEditable(product!!.name)
            binding.locationTV.text = product!!.location
            binding.descriptionET.text = Editable.Factory.getInstance().newEditable(product!!.description)
        }

        return binding.root
    }

    companion object {
        fun newInstance(product: Product?) = AddProductFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, product)
            }
        }
    }
}