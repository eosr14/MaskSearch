package com.eosr14.masksearch.common.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.eosr14.masksearch.R
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.exit_dialog.*

class ExitDialog(
    context: Context,
    private val appExitAction: Runnable,
    private val continueAction: Runnable
) : Dialog(context), View.OnClickListener {

    init {
        this.setCanceledOnTouchOutside(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.exit_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)

        setBindView()
    }

    private fun setBindView() {
        adView.loadAd(AdRequest.Builder().build())
        tv_exit_dialog_cancel.setOnClickListener(this)
        tv_exit_dialog_continue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            tv_exit_dialog_cancel.id -> {
                dismiss()
                appExitAction.run()
            }
            tv_exit_dialog_continue.id -> {
                dismiss()
                continueAction.run()
            }
        }
    }

}