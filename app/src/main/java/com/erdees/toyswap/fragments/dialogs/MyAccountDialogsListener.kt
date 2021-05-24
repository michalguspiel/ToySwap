package com.erdees.toyswap.fragments.dialogs

interface MyAccountDialogsListener {
    fun onCloseDialog()

    fun displaySnackBar(){}

    var passwordChangedSuccessfully: Boolean?

}