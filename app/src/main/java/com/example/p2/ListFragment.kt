package com.example.p2

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.p2.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!


    private lateinit var delete: TextView
    private lateinit var cancel: TextView
    private lateinit var allProducts: List<Product>

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as ProductsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)

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
                //adapter.updateTaskList(viewModel.deleteTask(it) as ArrayList<Product>)
                //binding.amountTextView.text = adapter.itemCount.toString()
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
            products.let { adapter.submitList(it) }
            binding.amountTextView.text = products.size.toString()
        }



//        productListAdapter = ProductListAdapter(requireContext(), listOf(),
//            clickListener = {
////                val transaction = requireActivity().supportFragmentManager.beginTransaction()
////                transaction.replace(R.id.fragmentContainerView, TaskDetailsFragment.newInstance(it))
////                transaction.addToBackStack(null)
////                transaction.commit()
//            }
//        ) {
//            val dialog = Dialog(requireContext())
//            dialog.setContentView(R.layout.dialog_delete)
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//            delete = dialog.findViewById(R.id.delete)
//            cancel = dialog.findViewById(R.id.cancel)
//
//            delete.setOnClickListener { which ->
//                //adapter.updateTaskList(viewModel.deleteTask(it) as ArrayList<Product>)
//                //binding.amountTextView.text = adapter.itemCount.toString()
//                dialog.dismiss()
//            }
//
//            cancel.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            dialog.show()
//        }



        binding.addBtn.setOnClickListener {
            viewModel.insert(Product(null, "Maso","wedwed"))
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.fragmentContainerView, TaskDetailsFragment.newInstance(it))
//                transaction.addToBackStack(null)
//                transaction.commit()
//            db.productDao().insert(Product(name = "product 1", adres = "adres 1"))
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}