package com.example.test1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null){
            openFragment0()
        }
    }

    private fun openFragment0(){
        val fragment = MyFragment0.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_0, fragment)
            .commit()
    }
}