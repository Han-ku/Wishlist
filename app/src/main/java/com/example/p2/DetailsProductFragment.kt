package com.example.p2

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.p2.databinding.FragmentDetailsProductBinding

private const val ARG_PARAM1 = "product"
class DetailsProductFragment : Fragment() {
    private var _binding: FragmentDetailsProductBinding? = null
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
        _binding =  FragmentDetailsProductBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product?
        }

        binding.titleTV.text = product!!.name
        binding.descriptionTV.text = product!!.description
        binding.locationTV.text = product!!.location

        binding.boughtBtn.setOnClickListener {
            viewModel.delete(product!!)

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, ListFragment())
            transaction.addToBackStack(null)
            transaction.commit()

            Toast.makeText(context, "Product został kupiony", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, AddProductFragment.newInstance(product))
                transaction.addToBackStack(null)
                transaction.commit()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        fun newInstance(product: Product?) = DetailsProductFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, product)
            }
        }
    }
}