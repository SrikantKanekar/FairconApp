package com.example.faircon.framework.datasource.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faircon.business.domain.util.printLogD
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.datasource.cache.entity.AuthToken
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val authTokenDao: AuthTokenDao,
) {
    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun setValue(newValue: AuthToken) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue
            }
        }
    }

    fun login(newValue: AuthToken) {
        setValue(newValue)
    }

    fun logout() {
        printLogD("SessionManager", "logout : Logging out")

        CoroutineScope(IO).launch {
            var errorMessage: String? = null
            try {
                _cachedToken.value!!.account_pk?.let { token ->
                    authTokenDao.nullifyToken(token)
                } ?: throw CancellationException("Token Error. Logging out user.")
            } catch (e: CancellationException) {
                printLogD("SessionManager", "logout : CancellationException -> ${e.message}")
                errorMessage = e.message
            } catch (e: Exception) {
                printLogD("SessionManager", "logout : Exception -> ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            } finally {
                errorMessage?.let {
                    printLogD("SessionManager", "logout : Finally -> $errorMessage")
                }
                printLogD("SessionManager", "logout : Finally Logged out")
                setValue(AuthToken())
            }
        }
    }
}