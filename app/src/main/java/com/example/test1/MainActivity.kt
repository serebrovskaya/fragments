package com.example.test1

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            openFragmentWithRecycle()
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
}