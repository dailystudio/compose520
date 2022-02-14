package com.dailystudio.compose.loveyou.interactive

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.annotation.WorkerThread
import com.dailystudio.devbricksx.development.Logger
import java.io.IOException
import java.net.ServerSocket

class Jill(context: Context) {

    private val appContext: Context = context.applicationContext
    private var nsdManager: NsdManager? = null
    private var onlineTime: Long = 0

    init {
        nsdManager = appContext.getSystemService(Context.NSD_SERVICE) as NsdManager
        if (nsdManager == null) {
            Logger.warn("Network Service Discovery not supported!")
        }
    }

    @WorkerThread
    fun register(time: Long) {
        onlineTime = time

        val autoAllocPort = allocatePort()

        val serviceInfo = NsdServiceInfo().apply {
            serviceName = buildString {
                append(JackAndJill.SERVICE_BASE_NAME)
                append(onlineTime)
            }

            serviceType = JackAndJill.SERVICE_TYPE
            port = autoAllocPort
        }

        nsdManager?.registerService(
            serviceInfo, NsdManager.PROTOCOL_DNS_SD,
            registrationListener
        )
    }

    fun unregister() {
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
        override fun onRegistrationFailed(p0: NsdServiceInfo?, errorCode: Int) {
            Logger.error("jill online failed: err(%d)", errorCode)
        }

        override fun onUnregistrationFailed(p0: NsdServiceInfo?, errorCode: Int) {
            Logger.error("jill offline failed: err(%d)", errorCode)
        }

        override fun onServiceRegistered(p0: NsdServiceInfo?) {
            Logger.info("jill online: name = ${p0?.serviceName}, port = ${p0?.port}")
        }

        override fun onServiceUnregistered(p0: NsdServiceInfo?) {
            Logger.info("jill offline: $p0")
        }

    }
}