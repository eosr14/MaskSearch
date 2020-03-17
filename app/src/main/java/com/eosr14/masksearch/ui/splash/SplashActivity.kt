package com.eosr14.masksearch.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.eosr14.masksearch.R
import com.eosr14.masksearch.common.PERMISSION_LOCATION_REQUEST_CODE
import com.eosr14.masksearch.common.SPLASH_INTERVAL
import com.eosr14.masksearch.common.base.BaseActivity

import com.eosr14.masksearch.ui.main.MainActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setAdView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        } else {
            showMain()
        }
    }

    private fun setAdView() {
        adView?.loadAd(AdRequest.Builder().build())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this@SplashActivity,
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
            showMain()
        }
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
        when (requestCode) {
            PERMISSION_LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT)
                        .show()
                    showMain()
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

}