package com.erdees.toyswap

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object Utils {


    fun openFragment(fragment: Fragment, fragmentTag: String, fm: FragmentManager) {
        val backStateName = fragment.javaClass.name
        val fragmentPopped = fm.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped && fm.findFragmentByTag(fragmentTag) == null) { //if fragment isn't in backStack, create it
            val ft: FragmentTransaction = fm.beginTransaction()
            ft.replace(R.id.mainContainer, fragment, fragmentTag)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    fun View.makeGone(){
        this.visibility = View.GONE
    }

    fun View.makeInvisible(){
        this.visibility = View.INVISIBLE
    }

    fun View.makeVisible(){
        this.visibility = View.VISIBLE
    }

}