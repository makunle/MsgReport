package com.noest.msgreport.deliver

import com.noest.minicache.MiniCache
import com.noest.msgreport.util.Constants
import com.noest.msgreport.util.LogX
import com.noest.msgreport.service.SenderService
import org.json.JSONObject
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset

object WxDeliver : IDeliver {
    val mKV = MiniCache.getDefaultCache()

    override fun deliver(msg: String, from: String): Boolean {
        val host = mKV.getString(Constants.HOST, "localhost")
        val port = mKV.getInt(Constants.PORT, 9999)
        val key = mKV.getString(Constants.KEY, "");
        val to = mKV.getString(Constants.TO, "指间的微妙")
        val extra = mKV.getString(Constants.EXTRA, "")

        val js = JSONObject()
        js.put(Constants.KEY, key)
        js.put(Constants.MSG, msg)
        js.put(Constants.TO, to)
        js.put(Constants.EXTRA, extra)
        js.put(Constants.FROM, from)

        var socket: Socket? = null
        try {
            socket = Socket(host, port)

            val output = socket.getOutputStream()
            val input = socket.getInputStream()

            output?.write(js.toString().toByteArray())

            val bytes = ByteArray(1024)
            input?.read(bytes);
            val rst = bytes.toString(Charset.defaultCharset())
            LogX.d(SenderService.TAG, "return: $rst")
            return rst.startsWith("success")
        } catch (e: Exception) {
            LogX.d(SenderService.TAG, e.localizedMessage)
        } finally {
            socket?.close()
        }
        return false
    }
}