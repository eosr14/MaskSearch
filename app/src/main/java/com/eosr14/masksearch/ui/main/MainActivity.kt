package com.eosr14.masksearch.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eosr14.masksearch.R
import com.eosr14.masksearch.common.*
import com.eosr14.masksearch.common.base.BaseActivity
import com.eosr14.masksearch.common.base.BaseRecyclerViewAdapter
import com.eosr14.masksearch.common.extension.showKeyboardAndFocus
import com.eosr14.masksearch.common.view.ExitDialog
import com.eosr14.masksearch.common.view.VerticalMarginDecoration
import com.eosr14.masksearch.databinding.ActivityMainBinding
import com.eosr14.masksearch.model.MaskStoreModel
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), MainViewModelInterface,
    MapView.MapViewEventListener,
    MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private lateinit var mainViewModel: MainVIewModel

    private val onSoftKeyBoardShowListener: (Boolean) -> Unit = { isShowKeyBoard ->
        et_auto_complete?.let { it.showKeyboardAndFocus(isShowKeyBoard) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMainBinding>(
            this@MainActivity,
            R.layout.activity_main
        ).apply {
            mainViewModel = MainVIewModel(this@MainActivity)
            viewModel = mainViewModel
            lifecycleOwner = this@MainActivity

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkLocationPermission()
            } else {
                bindView()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        uiEventObserve()
    }

    override fun onBackPressed() {
        if (!this@MainActivity.isFinishing) {
            ExitDialog(
                this@MainActivity,
                Runnable { finish() },
                Runnable { }
            ).show()
        }
    }

    private fun bindView() {
        map_main.setMapViewEventListener(this)
        map_main.setCalloutBalloonAdapter(CustomMarkerBalloonAdapter(this@MainActivity))

        rv_auto_complete.run {
            addItemDecoration(
                VerticalMarginDecoration(
                    this@MainActivity
                )
            )
            layoutManager =
                LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
            adapter = AutoCompleteAdapter(object : BaseRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClick(
                    view: View,
                    position: Int,
                    adapter: BaseRecyclerViewAdapter<*, *>
                ) {
                    (adapter as AutoCompleteAdapter).getItem(position).let {
                        if (it.x != null && it.y != null) {
                            val latitude = it.y.toDouble()
                            val longitude = it.x.toDouble()
                            mainViewModel.clearToRequestMaskStore(
                                latitude,
                                longitude,
                                REQUEST_METER
                            )
                            movePoint(it.placeName, latitude, longitude)
                        }
                    }
                }
            })
        }
    }

    private fun movePoint(text: String, latitude: Double, longitude: Double) {
        mainViewModel.clearAutoCompleteList()
        onSoftKeyBoardShowListener(false)
        map_main?.setMapCenterPointAndZoomLevel(
            MapPoint.mapPointWithGeoCoord(latitude, longitude),
            2,
            true
        )
    }

    private fun uiEventObserve() {
        addDisposable(
            et_auto_complete.textChanges()
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { it.toString() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { term ->
                    when {
                        term.isNotEmpty() -> mainViewModel.requestKaKaoKeyWord(term)
                        else -> mainViewModel.clearAutoCompleteList()
                    }
                }
        )
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        (getSystemService(Context.LOCATION_SERVICE) as LocationManager).apply {
            getLastKnownLocation(this)?.let {
                map_main.apply {
                    val currentMapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
                    val currentMarker = MapPOIItem().apply {
                        itemName = "현재 위치"
                        tag = 0
                        mapPoint = currentMapPoint
                        markerType = MapPOIItem.MarkerType.CustomImage
                        customImageResourceId = R.drawable.location
                        isCustomImageAutoscale = false
                        setCustomImageAnchor(0.5f, 1.0f)
                        userObject = MaskStoreModel.Stores(
                            name = "현재 위치",
                            addr = "",
                            code = tag.toString(),
                            createdAt = "",
                            lat = 0.0,
                            lng = 0.0,
                            remainStat = "",
                            type = "",
                            stockAt = ""
                        )
                    }

                    this.setMapCenterPointAndZoomLevel(
                        currentMapPoint,
                        2,
                        true
                    )
                    this.addPOIItem(currentMarker)
                    this.selectPOIItem(currentMarker, true)

                    mainViewModel.requestMaskStore(it.latitude, it.longitude, REQUEST_METER)
                }
            }
        }
    }

    private fun setDefaultMarker() {
        (getSystemService(Context.LOCATION_SERVICE) as LocationManager).apply {
            getLastKnownLocation(this)?.let {
                val currentMapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
                val currentMarker = MapPOIItem().apply {
                    itemName = "현재 위치"
                    tag = 0
                    mapPoint = currentMapPoint
                    markerType = MapPOIItem.MarkerType.CustomImage
                    customImageResourceId = R.drawable.location
                    isCustomImageAutoscale = false
                    setCustomImageAnchor(0.5f, 1.0f)
                    userObject = MaskStoreModel.Stores(
                        name = "현재 위치",
                        addr = "",
                        code = tag.toString(),
                        createdAt = "",
                        lat = 0.0,
                        lng = 0.0,
                        remainStat = "",
                        type = "",
                        stockAt = ""
                    )
                }
                map_main?.addPOIItem(currentMarker)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(locationManager: LocationManager): Location? {
        val providers: List<String> = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun startFindAddress() {
        MapReverseGeoCoder(
            KAKAO_NATIVE_APP_KEY,
            map_main.mapCenterPoint,
            this@MainActivity,
            this@MainActivity
        ).startFindingAddress()
    }

    // MainViewModelInterface [--
    override fun onClickMyLocation() = initLocation()

    override fun showNetworkErrorToast() =
        Toast.makeText(this, R.string.error_network_message, Toast.LENGTH_SHORT).show()

    override fun clearMarker() {
        map_main?.removeAllPOIItems()
        setDefaultMarker()
    }

    // ]-- MainViewModelInterface

    // MapView.MapViewEventListener [--

    override fun onMapViewInitialized(p0: MapView?) {
        initLocation()
    }

    override fun onMapViewMoveFinished(mapView: MapView?, point: MapPoint?) {
        point?.mapPointGeoCoord?.let {
            mainViewModel.setMoveLatitude(it.latitude)
            mainViewModel.setMoveLongitude(it.longitude)
        }
    }

    override fun onMapViewDoubleTapped(mapView: MapView?, point: MapPoint?) {}

    override fun onMapViewDragStarted(mapView: MapView?, point: MapPoint?) {}

    override fun onMapViewCenterPointMoved(mapView: MapView?, point: MapPoint?) {}

    override fun onMapViewDragEnded(mapView: MapView?, point: MapPoint?) {}

    override fun onMapViewSingleTapped(mapView: MapView?, point: MapPoint?) {}

    override fun onMapViewZoomLevelChanged(mapView: MapView?, point: Int) {}

    override fun onMapViewLongPressed(mapView: MapView?, point: MapPoint?) {}
    // ]-- MapView.MapViewEventListener

    // MapReverseGeoCoder.ReverseGeoCodingResultListener [--
    override fun onReverseGeoCoderFailedToFindAddress(coder: MapReverseGeoCoder?) {
        getGeoAddress("")
    }

    override fun onReverseGeoCoderFoundAddress(coder: MapReverseGeoCoder?, address: String?) {
        getGeoAddress(address)
    }

    private fun getGeoAddress(result: String?) {
        mainViewModel.setCurrentAddress(result)
    }
    // ]-- MapReverseGeoCoder.ReverseGeoCodingResultListener

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // 권한 없음
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION_REQUEST_CODE
            )
        } else {
            // 권한 있음
            bindView()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT)
                        .show()
                    bindView()
                } else {
                    Toast.makeText(this, getString(R.string.permission_define), Toast.LENGTH_SHORT)
                        .show()
                    showAppSetting()
                }
            }
            else -> {
                // nothing
            }
        }
    }

    private fun showAppSetting() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + "com.eosr14.masksearch")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}