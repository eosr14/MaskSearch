package com.eosr14.masksearch.ui.splash

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.eosr14.masksearch.R
import com.eosr14.masksearch.common.base.BaseActivity
import com.eosr14.masksearch.ui.main.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        } else {
            showMain()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@SplashActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // 권한 있음
            showMain()
        } else {
            // 권한 없음
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SplashActivity,
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
        AlertDialog.Builder(this@SplashActivity)
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

    private fun showMain() {
        addDisposable(
            Observable
                .interval(SPLASH_INTERVAL, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    MainActivity.startActivity(this)
                    finish()
                }
        )
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
            showMain()
        }
    }

    companion object {
        private const val SPLASH_INTERVAL = 2000L
        private const val PERMISSION_LOCATION_REQUEST_CODE = 1000
    }

}