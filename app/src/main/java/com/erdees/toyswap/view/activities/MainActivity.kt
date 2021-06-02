package com.erdees.toyswap.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils
import com.erdees.toyswap.databinding.ActivityMainBinding
import com.erdees.toyswap.view.fragments.AddItemFragment
import com.erdees.toyswap.view.fragments.BrowserFragment
import com.erdees.toyswap.view.fragments.MyAccountFragment
import com.erdees.toyswap.viewModel.MainActivityViewModel
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    /**Fragments*/
    private val mainFragment = BrowserFragment.newInstance()
    private val myAccountFragment = MyAccountFragment.newInstance()
    private val addFragment = AddItemFragment.newInstance()

    private lateinit var viewModel : MainActivityViewModel
    private lateinit var viewBinding: ActivityMainBinding

    private var currentUser: FirebaseUser? = null
    private var isUserLoggedOut : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        setContentView(view)

        viewModel.userLiveData.observe(this,  {
            currentUser = it
        })

        viewModel.isUserLoggedOut.observe(this,{
            isUserLoggedOut = it
        })

        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.nav_mainFragment -> {
                    Utils.openFragment(mainFragment,BrowserFragment.TAG,supportFragmentManager)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_addItemFragment -> {
                    Utils.openFragment(addFragment,AddItemFragment.TAG,supportFragmentManager)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_myAccountFragment -> {
                    if(!isUserLoggedOut)Utils.openFragment(myAccountFragment,MyAccountFragment.TAG,supportFragmentManager)
                    else openLoginActivity()
                }
                R.id.nav_messagesFragment -> {
                  // TODO   if(!isUserLoggedOut)Utils.openFragment(chatFragment,ChatFragment.TAG,supportFragmentManager)
                    //else TODO openLoginActivity()
                }
                else -> {}
            }
            true
        }
        Utils.openFragment(mainFragment,BrowserFragment.TAG,supportFragmentManager)

    }

    private fun openLoginActivity(){
        val loginActivity = Intent(this, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(loginActivity)
    }

    companion object{
        const val TAG = "MainActivity"
    }
}