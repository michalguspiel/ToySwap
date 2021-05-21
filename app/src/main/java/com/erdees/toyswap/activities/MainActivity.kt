package com.erdees.toyswap.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils
import com.erdees.toyswap.activities.welcomeActivity.LoginActivity
import com.erdees.toyswap.databinding.ActivityMainBinding
import com.erdees.toyswap.fragments.AddItemFragment
import com.erdees.toyswap.fragments.MainFragment
import com.erdees.toyswap.fragments.MyAccountFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**TODO ADD MAIN LOGIC 
 * TODO FRAGMENTS: MY ACC, BROWSE ITEMS,MESSAGES,*/

class MainActivity : AppCompatActivity() {

    /**Fragments*/
    val mainFragment = MainFragment.newInstance()
    val myAccountFragment = MyAccountFragment.newInstance()
    val addFragment = AddItemFragment.newInstance()

    lateinit var auth: FirebaseAuth

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_mainFragment -> {
                    Utils.openFragment(mainFragment,MainFragment.TAG,supportFragmentManager)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_addItemFragment -> {
                    Utils.openFragment(addFragment,AddItemFragment.TAG,supportFragmentManager)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_myAccountFragment -> {
                    if(auth.currentUser != null) Utils.openFragment(myAccountFragment,MyAccountFragment.TAG,supportFragmentManager)
                    else openLoginActivity()
                }
                else -> {}
            }
            true
        }
        Utils.openFragment(mainFragment,MainFragment.TAG,supportFragmentManager)

    }

    private fun openLoginActivity(){
        val loginActivity = Intent(this,LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(loginActivity)
    }

    companion object{
        const val TAG = "MainActivity"
    }
}