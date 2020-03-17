package com.eosr14.masksearch.common

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText?.showKeyboardAndFocus(isShowKeyBoard: Boolean) {
    this?.let { it ->
        val imm =
            it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        when (isShowKeyBoard) {
            true -> {
                it.post {
                    it.requestFocus()
                    imm.showSoftInput(it, InputMethodManager.SHOW_FORCED)
                }
            }
            else -> {
                it.post {
                    imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.SHOW_FORCED)
                }
            }
        }
    }
}