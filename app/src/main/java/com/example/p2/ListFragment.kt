package com.example.p2

import android.app.Dialog
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.p2.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get()=_binding!!


    private lateinit var delete: TextView
    private lateinit var cancel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentListBinding.inflate(inflater, container, false)

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java,
            "productsDB"
        ).allowMainThreadQueries().build()

        val allProducts = db.productDao().getAll()

        binding.amountTextView.text = allProducts.size.toString()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())

            adapter = ProductListAdapter(requireContext(), allProducts,
                clickListener = {
//                val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.fragmentContainerView, TaskDetailsFragment.newInstance(it))
//                transaction.addToBackStack(null)
//                transaction.commit()
                },
                longClickListener = {
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.dialog_delete)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                    delete = dialog.findViewById(R.id.delete)
                    cancel = dialog.findViewById(R.id.cancel)

                    delete.setOnClickListener { which ->
    //                    adapter.updateTaskList(viewModel.deleteTask(it) as ArrayList<Product>)
    //                    binding.amountTextView.text = adapter.itemCount.toString()
                        dialog.dismiss()
                    }

                    cancel.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()
                })
        }


        binding.addBtn.setOnClickListener {
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