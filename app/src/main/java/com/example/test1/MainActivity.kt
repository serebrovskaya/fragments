package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.test1.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var isFragmentShown = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonShow.setOnClickListener {
            if (!isFragmentShown) {
                binding.shimmerContainer.visibility = View.VISIBLE
                binding.buttonShow.text = "Скрыть библиотеку"
                if (savedInstanceState == null) {
                    openFragmentWithRecycle()
                }
                isFragmentShown = true
                binding.buttonGoogleBooks.visibility = View.GONE
            } else {
                closeFragment()
                binding.buttonShow.text = "Показать библиотеку"
                isFragmentShown = false
                binding.buttonGoogleBooks.visibility = View.VISIBLE
            }
        }

        binding.buttonGoogleBooks.setOnClickListener {
            val intent = Intent(this, GoogleBooksActivity::class.java)
            startActivity(intent)
        }
    }
    private val viewModel: LibraryViewModel by viewModels()

    private fun openFragmentWithRecycle() {

        binding.shimmerContainer.startShimmer()
        viewModel.loadData()

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (!isLoading) {
                    binding.shimmerContainer.stopShimmer()
                    binding.shimmerContainer.visibility = View.GONE

                    val fragment = MyFragmentWithRecycler.newInstance()
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_recycle, fragment)
                        .commit()
                }
            }
        }
    }
    private fun closeFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_recycle)
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .remove(it)
                .commit()
        }
    }

}