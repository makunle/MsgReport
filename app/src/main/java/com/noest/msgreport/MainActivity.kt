package com.noest.msgreport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.*
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnClientSend.setOnClickListener { it ->
            Thread(object : Runnable {
                override fun run() {
                    val host = etServerHost.text.toString()
                    val port = etServerPort.text.toString().toInt()
                    val key = etKey.text.toString()
                    val msg = etMsg.text.toString()
                    val extra = etExtra.text.toString()
                    val to = etTo.text.toString()

                    val js = JSONObject()
                    js.put("key", key)
                    js.put("msg", msg)
                    js.put("extra", extra)
                    js.put("to", to)

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
            }).start()
        }
    }
}
