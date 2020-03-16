package com.eosr14.masksearch.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.eosr14.masksearch.R
import com.eosr14.masksearch.common.KAKAO_NATIVE_APP_KEY
import com.eosr14.masksearch.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView

class MainActivity : AppCompatActivity(), MainViewModelInterface,
    MapView.MapViewEventListener,
    MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private lateinit var mainViewModel: MainVIewModel

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

    override fun onDestroy() {
        super.onDestroy()
        map_main.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        map_main.setShowCurrentLocationMarker(false)
    }

    private fun bindView() {
        map_main.setMapViewEventListener(this)
    }

    @SuppressLint("MissingPermission")
    private fun initLocation() {
        (getSystemService(Context.LOCATION_SERVICE) as LocationManager).apply {
            getLastKnownLocation(this)?.let {
                map_main.apply {
                    this.currentLocationTrackingMode =
                        MapView.CurrentLocationTrackingMode.TrackingModeOff
                    val currentMapPoint = MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
                    this.setMapCenterPointAndZoomLevel(
                        currentMapPoint,
                        1,
                        true
                    )
                    this.setShowCurrentLocationMarker(true)
                    mainViewModel.requestMaskStore(it.latitude, it.longitude, 800)
                }
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
                // Found best last known location: %s", l);
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
    override fun onClickMyLocation() {
        initLocation()
    }
    // ]-- MainViewModelInterface

    // MapView.MapViewEventListener [--

    override fun onMapViewInitialized(p0: MapView?) {
        initLocation()
    }

    override fun onMapViewMoveFinished(mapView: MapView?, point: MapPoint?) {
        point?.mapPointGeoCoord?.let {
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
        val message = if (result.isNullOrEmpty()) "주소를 불러오는데 실패하였습니다." else result
        Toast.makeText(this@MainActivity, "가져온 주소 = $message", Toast.LENGTH_SHORT).show()
    }
    // ]-- MapReverseGeoCoder.ReverseGeoCodingResultListener

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // 권한 있음
            bindView()
        } else {
            // 권한 없음
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // 다시보지않기 체크 X
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_LOCATION_REQUEST_CODE
                )
            } else {
                // 다시보지않기 체크
                showPermissionDenied()
            }
        }
    }

    private fun showPermissionDenied() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("안내")
            .setMessage("퍼미션 거부 안내")
            .setPositiveButton(
                "앱 설정 > 권한"
            ) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + "com.eosr14.masksearch")
                )
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton(
                "취소"
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE
            && Manifest.permission.ACCESS_FINE_LOCATION == permissions[0]
            && PackageManager.PERMISSION_GRANTED == grantResults[0]
        ) {
            bindView()
        }
    }


    companion object {
        private const val PERMISSION_LOCATION_REQUEST_CODE = 1000

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}