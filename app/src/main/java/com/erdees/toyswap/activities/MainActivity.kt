package com.erdees.toyswap.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.erdees.toyswap.R
import com.erdees.toyswap.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**TODO ADD MAIN LOGIC +  SIDE NAVIGATION OR BOTTOM NAVIGATION IDK YET DECIDE TMRW  + SMOOTH FRAGMENT TRANSITIONS IN MENU
 * TODO FRAGMENTS: MY ACC, BROWSE ITEMS,MESSAGES,*/

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        val navController = findNavController(R.id.nav_host_fragment)

        viewBinding.bottomNavigation.setupWithNavController(navController)



    }
}