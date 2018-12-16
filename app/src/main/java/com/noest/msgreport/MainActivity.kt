package com.noest.msgreport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketAddress

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartServer.setOnClickListener {
            Thread(object : Runnable {
                override fun run() {
                    val socket = ServerSocket(9999)
                    while (true) {
                        val client = socket.accept()
                        LogX.d(TAG, "客户端连接: " + client.inetAddress)
                        val stream = DataInputStream(client.getInputStream())
                        try {
                            LogX.d(TAG, "客户端说: " + stream.readUTF())
                        } catch (e: Exception) {
                            LogX.d(TAG, e.message)
                        }
                    }
                }
            }).start()
        }


        btnClientSend.setOnClickListener {
            Thread(object : Runnable {
                override fun run() {
                    val socket = Socket("192.168.0.106", 9999)
                    val stream = socket.getOutputStream()
                    val bw = DataOutputStream(stream)
                    bw.writeUTF("10086/n你的验证码是0987")
                    socket.shutdownOutput()
                    socket.close()
                }
            }).start()
        }
    }
}
