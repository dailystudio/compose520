package com.dailystudio.compose.loveyou.interactive

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.annotation.WorkerThread
import com.dailystudio.devbricksx.development.Logger
import java.io.IOException
import java.net.ServerSocket

class Jill(context: Context) {

    private var nsdManager: NsdManager? =
        context.applicationContext.getSystemService(Context.NSD_SERVICE) as NsdManager

    var onlineTime: Long = 0
    var servicePort: Int = -1

    init {
        if (nsdManager == null) {
            Logger.warn("Network Service Discovery not supported!")
        }
    }

    @WorkerThread
    fun online() {
        onlineTime = System.currentTimeMillis()
        servicePort = allocatePort()

        val serviceInfo = NsdServiceInfo().apply {
            serviceName = buildString {
                append(JackAndJill.SERVICE_BASE_NAME)
                append(onlineTime)
            }

            serviceType = JackAndJill.SERVICE_TYPE
            port = servicePort
        }

        nsdManager?.registerService(
            serviceInfo, NsdManager.PROTOCOL_DNS_SD,
            registrationListener
        )
    }

    fun offline() {
        nsdManager?.unregisterService(registrationListener)
    }

    private fun allocatePort(): Int {
        var socket: ServerSocket? = null
        socket = try {
            ServerSocket(0)
        } catch (e: IOException) {
            Logger.error("could not create server socket: %s", e.toString())
            null
        }
        return socket?.localPort ?: -1
    }

    private val registrationListener = object : NsdManager.RegistrationListener {

        override fun onRegistrationFailed(service: NsdServiceInfo?, errorCode: Int) {
            Logger.error("Jill [${service?.serviceName}] online failed: err(%d)", errorCode)
        }

        override fun onUnregistrationFailed(service: NsdServiceInfo?, errorCode: Int) {
            Logger.error("Jill [${service?.serviceName}] offline failed: err(%d)", errorCode)
        }

        override fun onServiceRegistered(service: NsdServiceInfo?) {
            Logger.info("Jill [${service?.serviceName}]\'s online: port = ${service?.port}")
        }

        override fun onServiceUnregistered(service: NsdServiceInfo?) {
            Logger.info("Jill [${service?.serviceName}]\'s offline")
        }

    }
}