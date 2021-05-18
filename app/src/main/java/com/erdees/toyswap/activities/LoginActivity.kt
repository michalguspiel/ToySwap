package com.erdees.toyswap.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erdees.toyswap.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding : LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}