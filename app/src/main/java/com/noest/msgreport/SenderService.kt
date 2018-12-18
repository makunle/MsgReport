package com.noest.msgreport

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.noest.minicache.MiniCache
import com.noest.msgreport.Constants.Companion.EXTRA
import com.noest.msgreport.Constants.Companion.FROM
import com.noest.msgreport.Constants.Companion.HOST
import com.noest.msgreport.Constants.Companion.KEY
import com.noest.msgreport.Constants.Companion.MSG
import com.noest.msgreport.Constants.Companion.PORT
import com.noest.msgreport.Constants.Companion.TO
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import java.net.Socket
import java.nio.charset.Charset

class SenderService : IntentService("sender_service") {
    companion object {
        const val EXTRA_MSG = "msg"
        const val EXTRA_FROM = "from"
        const val TAG = "SenderService"

        fun startService(context: Context, msg: String?, from: String?) {
            val intent = Intent(context, SenderService::class.java)
            intent.putExtra(EXTRA_FROM, from)
            intent.putExtra(EXTRA_MSG, msg)
            context.startService(intent)
        }
    }

    val mKV = MiniCache.getDefaultCache()

    override fun onHandleIntent(intent: Intent?) {
        val from = intent?.getStringExtra(EXTRA_FROM)
        val msg = intent?.getStringExtra(EXTRA_MSG)

        wxSend(msg, from)
    }

    fun wxSend(msg: String?, from:String?) {
        val host = mKV.getString(HOST, "localhost")
        val port = mKV.getInt(PORT, 9999)
        val key = mKV.getString(KEY, "");
        val to = mKV.getString(TO, "指间的微妙")
        val extra = mKV.getString(EXTRA, "")

        val js = JSONObject()
        js.put(KEY, key)
        js.put(MSG, msg)
        js.put(TO, to)
        js.put(EXTRA, extra)
        js.put(FROM, from)

        var socket: Socket? = null
        try {
            socket = Socket(host, port)

            val output = socket.getOutputStream()
            val input = socket.getInputStream()

            output?.write(js.toString().toByteArray())

            val bytes = ByteArray(1024)
            input?.read(bytes);
            LogX.d(TAG, "return: " + bytes.toString(Charset.defaultCharset()))
        } catch (e: Exception) {
            LogX.d(TAG, e.localizedMessage)
        } finally {
            socket?.close()
        }
    }
}