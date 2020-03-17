package com.eosr14.masksearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eosr14.masksearch.common.REQUEST_METER
import com.eosr14.masksearch.common.base.BaseViewModel
import com.eosr14.masksearch.model.KaKaoKeyWord
import com.eosr14.masksearch.model.MaskStoreModel
import com.eosr14.masksearch.network.RetrofitManager
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

class MainVIewModel(
    private val mainViewModelInterface: MainViewModelInterface
) : BaseViewModel() {

    private val _moveLatitude = MutableLiveData(0.0)
    val moveLatitude: LiveData<Double> get() = _moveLatitude

    private val _moveLongitude = MutableLiveData(0.0)
    val moveLongitude: LiveData<Double> get() = _moveLongitude

    private val _currentAddress = MutableLiveData("")
    val currentAddress: LiveData<String> = _currentAddress

    private val _searchStoreList = MutableLiveData<List<MaskStoreModel.Stores>>(mutableListOf())
    val searchStoreList: LiveData<List<MaskStoreModel.Stores>> get() = _searchStoreList

    private val _autoCompleteList = MutableLiveData<ArrayList<KaKaoKeyWord.Document>>(arrayListOf())
    val autoCompleteList: LiveData<ArrayList<KaKaoKeyWord.Document>> get() = _autoCompleteList

    private val _isShowAutoComplete = MutableLiveData<Boolean>().apply { value = false }
    val isShowAutoComplete: LiveData<Boolean> = _isShowAutoComplete

    fun onClickMyLocation() {
        mainViewModelInterface.onClickMyLocation()
    }

    fun onClickRefresh() {
        val lat = _moveLatitude.value
        val lng = _moveLongitude.value

        if (lat != null && lng != null) {
            clearToRequestMaskStore(lat, lng, REQUEST_METER)
        }
    }

    fun clearToRequestMaskStore(lat: Double, lng: Double, meter: Int) {
        mainViewModelInterface.clearMarker()
        requestMaskStore(lat, lng, meter)
    }

    fun requestMaskStore(lat: Double, lng: Double, meter: Int) {
        addDisposable(
            RetrofitManager.requestMaskStore(lat, lng, meter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _searchStoreList.value = it.stores
                }, {
                    mainViewModelInterface.showNetworkErrorToast()
                })
        )
    }

    fun requestKaKaoKeyWord(query: String) {
        addDisposable(
            RetrofitManager.requestKaKaoKeyWord(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _autoCompleteList.value = it.documents
                    _isShowAutoComplete.value = true
                }, {
                    mainViewModelInterface.showNetworkErrorToast()
                    _isShowAutoComplete.value = false
                })
        )
    }

    fun clearAutoCompleteList() {
        _isShowAutoComplete.postValue(false)
        _autoCompleteList.value?.clear()
        _autoCompleteList.postValue(_autoCompleteList.value)
    }

    fun setCurrentAddress(address: String?) {
        _currentAddress.value = address
    }

    fun setMoveLatitude(lat: Double) {
        _moveLatitude.value = lat
    }

    fun setMoveLongitude(lng: Double) {
        _moveLongitude.value = lng
    }

    fun getCurrentAddress(): String? = _currentAddress.value

    fun getMoveLatitude(): Double? = _moveLatitude.value

    fun getMoveLongitude(): Double? = _moveLongitude.value


}