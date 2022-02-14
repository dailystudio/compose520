package com.dailystudio.compose.loveyou.interactive

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dailystudio.devbricksx.development.Logger

class Jack(context: Context) {

    private val appContext: Context = context.applicationContext
    private var nsdManager: NsdManager? = null

    private var onlineTime: Long = 0

    private val _jillFound: MutableLiveData<String?> = MutableLiveData(null)

    val jillFound: LiveData<String?> = _jillFound

    init {
        nsdManager = appContext.getSystemService(Context.NSD_SERVICE) as NsdManager
        if (nsdManager == null) {
            Logger.warn("Network Service Discovery not supported!")
        }
    }

    fun discover(time: Long) {
        onlineTime = time
        nsdManager?.discoverServices(
            JackAndJill.SERVICE_TYPE,
            NsdManager.PROTOCOL_DNS_SD,
            discoverListener
        )
    }

    fun stopDiscover() {
        nsdManager?.stopServiceDiscovery(discoverListener)
    }

    private fun checkAndResolveService(service: NsdServiceInfo?) {
        Logger.debug("new service found: service = $service")

        nsdManager?.resolveService(
            service,
            object : NsdManager.ResolveListener {
                override fun onResolveFailed(p0: NsdServiceInfo?, errorCode: Int) {
                    Logger.debug("resolve jill info failed: $errorCode")
                }

                override fun onServiceResolved(p0: NsdServiceInfo?) {
                    Logger.debug("resolved jill: $p0")
                    val serviceName = p0?.serviceName

                    Logger.debug("new jill found: [$serviceName]")
                    if (serviceName != null) {
                        val serviceOnlineTime = serviceName.replaceFirst(
                            JackAndJill.SERVICE_BASE_NAME, "")
                            .toLong()
                        if (serviceOnlineTime == onlineTime) {
                            Logger.debug("skip myself")
                        } else {
                            _jillFound.postValue(serviceName)
                        }
                    }
                }
            })
    }

    private val discoverListener = object: NsdManager.DiscoveryListener {

        override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
        }

        override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
        }

        override fun onDiscoveryStarted(regType: String?) {
            Logger.debug("jill discovery started: regType = $regType", regType)
        }

        override fun onDiscoveryStopped(serviceType: String?) {
            Logger.debug("jill discovery stopped: serviceType = $serviceType")
        }

        override fun onServiceFound(service: NsdServiceInfo?) {
            if (service?.serviceType != JackAndJill.SERVICE_TYPE) {
                Logger.warn("unknown service found: service = %s", service)
            } else {
                checkAndResolveService(service)
            }
        }

        override fun onServiceLost(service: NsdServiceInfo?) {
            if (service?.serviceType != JackAndJill.SERVICE_TYPE) {
                Logger.warn("unknown service found: service = %s", service)
            } else {
                Logger.debug("lost jill: $service")
                val serviceName = service.serviceName

                if (serviceName == _jillFound.value) {
                    _jillFound.postValue(null)
                }
            }
        }

    }

}