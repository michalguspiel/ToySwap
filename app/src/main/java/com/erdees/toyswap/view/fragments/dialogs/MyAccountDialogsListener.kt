package com.erdees.toyswap.view.fragments.dialogs

interface MyAccountDialogsListener {
    fun onCloseDialog()

    fun displaySnackBar(){}

    var passwordChangedSuccessfully: Boolean?

}