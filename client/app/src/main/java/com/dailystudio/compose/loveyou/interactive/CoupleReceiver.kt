package com.dailystudio.compose.loveyou.interactive

import android.os.Process
import com.dailystudio.devbricksx.development.Logger
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CoupleReceiver {

    private var networkService: ExecutorService? = null
    private var udpSocket: DatagramSocket? = null
    private var mEndFlag = false

    @Synchronized
    fun start() {
        networkService = Executors.newSingleThreadExecutor().apply {
            mEndFlag = false

            execute {
                /*
                 * MUST HAVE: to avoid drawing thread affect the main thread performance
                 */
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                loopForCommands()
            }
        }
    }


    @Synchronized
    fun stop() {
        if (networkService == null) {
            Logger.debug("network service is null, needn't to stop")
            return
        }

        synchronized(mEndFlag) {
            mEndFlag = true
        }

        networkService?.let {
            try {
                Logger.debug("shutting down network service ...")
                udpSocket?.close()
                it.shutdownNow()
            } catch (e: Exception) {
                Logger.warn("Could not shut down network service")
            }
        }

        networkService = null
    }


    private fun loopForCommands() {
        val port = JackAndJill.PORT
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            udpSocket = DatagramSocket(port,
                InetAddress.getByName("0.0.0.0")).apply {
                broadcast = true
            }

            while (!mEndFlag) {
                Logger.info("command receiver goes online: port = $port")

                //Receive a packet
                val recvBuf = ByteArray(15000)
                val packet = DatagramPacket(recvBuf, recvBuf.size)
                udpSocket?.receive(packet)

                //Packet received
                Logger.debug("packet received from: " + packet.address.hostAddress)
                val data: String = String(packet.data).trim { it <= ' ' }
                Logger.debug("data received: $data")
            }
        } catch (ex: IOException) {
            Logger.error("receive command failed: ${ex.message}")
        } finally {
            Logger.info("command receiver goes offline: socket closed [${udpSocket?.isClosed}]")
        }
    }
}