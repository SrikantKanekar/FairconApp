package com.example.faircon.business.interactors.app

import com.example.faircon.business.domain.util.printLogD
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Send a ping to googles primary DNS.
 * If successful, that means we have internet.
 */
object DoesNetworkHaveInternet {

    // Make sure to execute this on a background thread.
    fun execute(): Boolean {
        return try{
            printLogD("DoesNetworkHaveInternet", "PINGING google.")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            printLogD("DoesNetworkHaveInternet", "PING success.")
            true
        }catch (e: IOException){
            printLogD("DoesNetworkHaveInternet", "No internet connection. $e")
            false
        }
    }
}