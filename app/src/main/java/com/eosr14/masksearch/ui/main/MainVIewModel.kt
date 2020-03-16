package com.eosr14.masksearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eosr14.masksearch.common.base.BaseViewModel
import com.eosr14.masksearch.network.RetrofitManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class MainVIewModel(
    private val mainViewModelInterface: MainViewModelInterface
) : BaseViewModel() {

    private val _currentLatitude = MutableLiveData(0.0)
    val currentLatitude: LiveData<Double> get() = _currentLatitude

    private val _currentLongitude = MutableLiveData(0.0)
    val currentLongitude: LiveData<Double> get() = _currentLongitude

    fun onClickMyLocation() {
        mainViewModelInterface.onClickMyLocation()
    }

    fun requestMaskStore(lat: Double, lng: Double, meter: Int) {
        addDisposable(
            RetrofitManager.requestMaskStore(lat, lng, meter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, {

                })
        )
    }

}