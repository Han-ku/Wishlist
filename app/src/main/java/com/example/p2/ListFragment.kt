package com.example.p2

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.p2.databinding.FragmentListBinding


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding
        get()=_binding!!

    private lateinit var adapter: TaskListAdapter
    private val viewModel: TaskListViewModel by activityViewModels()

    private lateinit var delete: TextView
    private lateinit var cancel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentListBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = TaskListAdapter(requireContext(), mutableListOf(),
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
                    adapter.updateTaskList(viewModel.deleteTask(it) as ArrayList<Task>)
                    binding.amountTextView.text = adapter.itemCount.toString()
                    dialog.dismiss()
                }

                cancel.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            })


        binding.recyclerView.adapter = adapter

        viewModel.tasks.observe(requireActivity()) { tasks ->
            adapter.submitList(tasks)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}