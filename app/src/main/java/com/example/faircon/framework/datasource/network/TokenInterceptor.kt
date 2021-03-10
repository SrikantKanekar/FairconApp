package com.example.faircon.framework.datasource.network

import com.example.faircon.framework.datasource.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor
@Inject
constructor(
    private val sessionManager: SessionManager
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain
            .request()
            .newBuilder()

        sessionManager.cachedToken.value?.let { authToken->
            requestBuilder.addHeader("Authorization", "Token ${authToken.token!!}")
        }

        return chain.proceed(requestBuilder.build())
    }
}