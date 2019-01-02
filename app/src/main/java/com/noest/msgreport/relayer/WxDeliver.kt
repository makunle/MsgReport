package com.noest.msgreport.relayer

import com.noest.msgreport.service.RelayService
import com.noest.msgreport.util.Constants
import com.noest.msgreport.util.LogX
import com.noest.msgreport.util.Setting
import org.json.JSONObject
import java.net.Socket
import java.nio.charset.Charset

object WxDeliver : IDeliver {

    override fun deliver(msg: String, from: String): Boolean {
        val host = Setting.getString(Constants.SERVER_HOST, "")
        val port = Setting.getString(Constants.SERVER_PORT, "").toInt()
        val key = Setting.getString(Constants.SERVER_KEY, "")
        val to = Setting.getString(Constants.RECEIVER_NAME, "")
        val device = Setting.getString(Constants.DEVICE_ID, "")

        val js = JSONObject()

        js.put(Constants.KEY, key)
        js.put(Constants.MSG, msg)
        js.put(Constants.TO, to)
        js.put(Constants.FROM, from)
        js.put(Constants.DEVICE_ID, device)

        var socket: Socket? = null
        try {
            socket = Socket(host, port)

            val output = socket.getOutputStream()
            val input = socket.getInputStream()

            output?.write(js.toString().toByteArray())

            val bytes = ByteArray(1024)
            input?.read(bytes);
            val rst = bytes.toString(Charset.defaultCharset())
            LogX.d(RelayService.TAG, "return: $rst")
            return rst.startsWith("success")
        } catch (e: Exception) {
            LogX.d(RelayService.TAG, e.localizedMessage)
        } finally {
            socket?.close()
        }
        return false
    }
}