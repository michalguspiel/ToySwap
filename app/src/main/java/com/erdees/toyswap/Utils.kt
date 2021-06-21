package com.erdees.toyswap

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

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

    fun View.makeToast(message : String){
        Toast.makeText(this.context,message,Toast.LENGTH_SHORT).show()
    }

    fun View.makeSnackbar(message: String){
        Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun ViewGroup.showLoading(progressBar: ProgressBar) {
        this.addView(progressBar)
    }
    fun ViewGroup.endLoading(progressBar: ProgressBar) {
        this.removeView(progressBar)
    }

    fun <T> LiveData<T>.ignoreFirst(): MutableLiveData<T> {
        val result = MediatorLiveData<T>()
        var isFirst = true
        result.addSource(this) {
            if (isFirst) isFirst = false
            else result.value = it
        }
        return result
    }


     fun launchImageCrop(uri: Uri,context: Context,fragment: Fragment,x: Int,y:Int) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(x, y)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(context, fragment)
    }

    fun View.setMargins(
        topMargin: Int,
        bottomMargin: Int,
        leftMargin: Int,
        rightMargin: Int
    ) {
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.topMargin = topMargin
        layoutParams.bottomMargin = bottomMargin
        layoutParams.leftMargin = leftMargin
        layoutParams.rightMargin = rightMargin
        this.layoutParams = layoutParams
    }


    fun View.disable(){
        this.isEnabled = false
    }

    fun View.enable(){
        this.isEnabled = true
    }


}