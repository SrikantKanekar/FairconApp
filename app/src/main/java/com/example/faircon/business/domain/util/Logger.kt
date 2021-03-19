package com.example.faircon.business.domain.util

import android.util.Log
import com.example.faircon.business.domain.util.Constant.DEBUG
import com.example.faircon.business.domain.util.Constant.TAG
import com.example.faircon.business.domain.util.Constant.TAG_NETWORK

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

fun printLogNetwork(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG_NETWORK, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

object Constant {
    const val TAG = "DEBUG"
    const val TAG_NETWORK = "NETWORK_DEBUG"
    const val DEBUG = true
}