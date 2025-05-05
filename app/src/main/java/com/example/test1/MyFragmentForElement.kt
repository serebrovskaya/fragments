package com.example.test1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.test1.databinding.FragmentForElementBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val ARG_TITLE = "title"
private const val ARG_ID = "id"
private const val ARG_AVAILABILITY = "available"
private const val ARG_TYPE = "type"
private const val ARG_NEW = "isNew"

class MyFragmentForElement : Fragment() {

    private val viewModel: LibraryViewModel by activityViewModels()

    private var _binding: FragmentForElementBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ReceiverFragmentBinding is NULL")

    private var receivedNew: Boolean? = null
    private var receivedTitle: String? = null
    private var receivedId: Int? = null
    private var receivedAvailability: Boolean? = null
    private var receivedType: String? = null

    private var receivedPage: Int? = null
    private var receivedAuthor: String? = null
    private var receivedIssue: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            receivedNew = it.getBoolean(ARG_NEW)
            receivedType = it.getString(ARG_TYPE)
        }

        if (receivedNew == false) {
            arguments?.let {
                receivedTitle = it.getString(ARG_TITLE)
                receivedId = it.getInt(ARG_ID)
                receivedAvailability = it.getBoolean(ARG_AVAILABILITY)
            }

            when (receivedType) {
                "book" -> {
                    receivedPage = arguments?.getInt("pageCount")
                    receivedAuthor = arguments?.getString("author")
                }

                "newspaper" -> receivedIssue = arguments?.getInt("issueNumber")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentForElementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (receivedType) {
            "book" -> {
                binding.page.visibility = View.VISIBLE
                binding.author.visibility = View.VISIBLE
                binding.issue.visibility = View.GONE
            }

            "newspaper" -> {
                binding.page.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.issue.visibility = View.VISIBLE
            }

            else -> {
                binding.page.visibility = View.GONE
                binding.author.visibility = View.GONE
                binding.issue.visibility = View.GONE
            }
        }

        if (receivedNew == false) {
            binding.title.setText(arguments?.getString(ARG_TITLE))
            binding.id.setText(arguments?.getInt(ARG_ID).toString())
            binding.availability.setText(if (receivedAvailability == true) "Доступно" else "Нe доступно")
            binding.buttonSaveFr.visibility = View.GONE

            when (receivedType) {
                "book" -> {
                    binding.page.setText(receivedPage.toString())
                    binding.author.setText(receivedAuthor)
                }

                "newspaper" -> {
                    binding.issue.setText(receivedIssue.toString())
                }
            }
        } else {
            binding.title.hint = "Автор"
            binding.id.hint = "Id"
            binding.availability.hint = "Доступность (да/нет)"
            binding.buttonSaveFr.visibility = View.VISIBLE

            when (receivedType) {
                "book" -> {
                    binding.author.hint = "Автор"
                    binding.page.hint = "Кол-во страниц"
                }

                "newspaper" -> {
                    binding.issue.hint = "Номер газеты"
                }
            }

            binding.buttonSaveFr.setOnClickListener {
                val resultItem = prepareLibraryItem()
                resultItem.let {
                    viewModel.generateItem(resultItem)
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }

        lifecycleScope.launch {
            binding.icon.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            delay(500)

            binding.icon.setImageResource(
                when (receivedType) {
                    "book" -> R.drawable.book
                    "newspaper" -> R.drawable.newspaper
                    else -> android.R.color.transparent
                }
            )

            binding.progressBar.visibility = View.GONE
            binding.icon.visibility = View.VISIBLE
        }

    }

    private fun prepareLibraryItem(): LibraryItem {
        val isAvailable: Boolean = when (binding.availability.text.toString().lowercase()) {
            "да" -> true
            "нет" -> false
            else -> false
        }
        lateinit var resultItem: LibraryItem
        if (receivedType == "book") {
            resultItem = LibraryItem.Book(
                binding.id.text.toString().toLongOrNull() ?: 0,
                binding.title.text.toString(),
                isAvailable,
                binding.page.text.toString().toIntOrNull() ?: 0,
                binding.author.text.toString()
            )
        }
        if (receivedType == "newspaper") {
            resultItem = LibraryItem.Newspaper(
                binding.id.text.toString().toLongOrNull() ?: 0,
                binding.title.text.toString(),
                isAvailable,
                binding.issue.text.toString().toIntOrNull() ?: 0
            )
        }
        return resultItem
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private var openCounter = 0

        fun newInstance(item: LibraryItem?, type: String): MyFragmentForElement {
            openCounter++

            if (openCounter % 2 == 0) {
                throw IllegalArgumentException()
            }
            return MyFragmentForElement().apply {
                arguments = Bundle().apply {
                    if (item != null) {
                        putBoolean(ARG_NEW, false)
                        putString(ARG_TITLE, item.name)
                        putLong(ARG_ID, item.id)
                        putBoolean(ARG_AVAILABILITY, item.isAvailable)

                        when (item) {
                            is LibraryItem.Book -> {
                                putString(ARG_TYPE, "book")
                                putInt("pageCount", item.pageCount)
                                putString("author", item.author)
                            }

                            is LibraryItem.Newspaper -> {
                                putString(ARG_TYPE, "newspaper")
                                putInt("issueNumber", item.issueNumber)
                            }
                        }
                    } else {
                        putString(ARG_TYPE, type)
                        putBoolean(ARG_NEW, true)
                    }
                }
            }
        }

    }

}