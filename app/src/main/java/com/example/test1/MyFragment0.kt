package com.example.test1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test1.databinding.Fragment0Binding
import com.example.test1.LibraryItem.Book
import com.example.test1.LibraryItem.Newspaper
import com.example.test1.LibraryItem.Disc

class MyFragment0: Fragment(R.layout.fragment_0) {
    private var _binding: Fragment0Binding? = null
    private val binding get() = _binding ?: throw RuntimeException("ReceiverFragmentBinding is NULL")

    private val library = Library().apply {
        addItems(
            Book(1867, "Война и мир", true, 5202, "Лев Толстой"),
            Book(1866, "Преступление и наказание", false, 672, "Федор Достоевский"),
            Book(90743, "Маугли", true, 202, "Джозеф Киплинг"),

            Newspaper(303, "Комсомольская правда", true, 123),
            Newspaper(923, "Московский комсомолец", false, 456),
            Newspaper(17245, "Сельская жизнь", true, 794),

            Disc(1975, "Богемская рапсодия", true, "CD"),
            Disc(307, "Дэдпул и Росомаха", true, "DVD"),
            Disc(2001, "Voyage", false, "CD")
        )
    }

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment0Binding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter(library){ item ->
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
            library.addItem(item)
            adapter.notifyItemInserted(library.size - 1)

            binding.recyclerView.post {
                layoutManager?.scrollToPosition(library.size - 1)
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

    private fun openFragment(item: LibraryItem?, type: String = "book" ){
        val fragment = MyFragment.newInstance(item, type)

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_1, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun delItem(position: Int) {
        library.removeItem(position)
        adapter.updateList(library)
    }

    companion object {
        fun newInstance() = MyFragment0()
    }
}