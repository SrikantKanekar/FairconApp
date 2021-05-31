package com.example.faircon.business.domain.util

import android.util.Log
import com.example.faircon.business.domain.util.Constant.DEBUG
import com.example.faircon.business.domain.util.Constant.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

object Constant {
    const val TAG = "DEBUG"
    const val DEBUG = true
}