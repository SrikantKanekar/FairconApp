package com.example.faircon.util

class Constants {
    companion object{
        const val TAG = "DEBUG"

        const val BASE_URL = "https://faircon.pythonanywhere.com/api/"
        const val PASSWORD_RESET_URL = "https://faircon.pythonanywhere.com/password_reset/"

        const val NETWORK_TIMEOUT = 60000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing
    }
}