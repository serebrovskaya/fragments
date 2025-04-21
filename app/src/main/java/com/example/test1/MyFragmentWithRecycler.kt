package com.example.test1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test1.databinding.FragmentWithRecyclerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MyFragmentWithRecycler : Fragment() {

    private var _binding: FragmentWithRecyclerBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ReceiverFragmentBinding is NULL")

    private val viewModel: LibraryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWithRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter(viewModel.library) { item ->
            openFragment(item)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = binding.recyclerView.layoutManager

        val swipeHandler = SwipeToDeleteCallback(this)
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.recyclerView)

        binding.buttonAdd.setOnClickListener {
            showAddItemDialog()
        }

        viewModel.selItem.observe(viewLifecycleOwner, Observer { item ->
            viewModel.library.addItem(item)
            adapter.notifyItemInserted(viewModel.library.size - 1)

            binding.recyclerView.post {
                layoutManager?.scrollToPosition(viewModel.library.size - 1)
            }
        }
        )
    }

    private fun showAddItemDialog() {
        var type = "book"
        AlertDialog.Builder(requireContext())
            .setTitle("Выберите тип элемента")
            .setItems(arrayOf("Книга", "Газета")) { _, which ->
                when (which) {
                    0 -> type = "book"
                    1 -> type = "newspaper"
                }
                openFragment(null, type)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFragment(item: LibraryItem?, type: String = "book") {
        lifecycleScope.launch {
            flow {
                delay(300)
                val fragment = MyFragmentForElement.newInstance(item, type)

                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_element, fragment)
                    .addToBackStack(null)
                    .commit()

                emit(true)
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    when (e) {
                        is IllegalArgumentException ->
                            AlertDialog.Builder(requireContext())
                                .setTitle("Ошибка")
                                .setNegativeButton("Ок", null)
                                .show()

                        else -> {}
                    }
                }
                .collect {}
        }
    }

    fun delItem(position: Int) {
        viewModel.library.removeItem(position)
        adapter.updateList(viewModel.library)
    }

    companion object {
        fun newInstance() = MyFragmentWithRecycler()
    }
}