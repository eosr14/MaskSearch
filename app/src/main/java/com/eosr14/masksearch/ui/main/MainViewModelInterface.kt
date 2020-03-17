package com.eosr14.masksearch.ui.main

import android.widget.Toast

interface MainViewModelInterface {
    fun onClickMyLocation()
    fun showNetworkErrorToast()
    fun clearMarker()
}