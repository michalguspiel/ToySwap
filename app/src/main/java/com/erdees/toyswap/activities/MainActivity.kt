package com.erdees.toyswap.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erdees.toyswap.Utils
import com.erdees.toyswap.databinding.ActivityMainBinding
import com.erdees.toyswap.fragments.TestFragment


class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewBinding.helloWorldTV.text = "View Binding WORKs!!!!"
            viewBinding.helloWorldTV.setOnClickListener{
                Utils.openFragment(
                    TestFragment.newInstance(),
                    TestFragment.TAG,
                    supportFragmentManager
                )
            }
    }
}