package com.noest.msgreport.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.noest.minicache.MiniCache
import com.noest.msgreport.R
import com.noest.msgreport.service.SenderService
import com.noest.msgreport.util.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    val mKV = MiniCache.getDefaultCache()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle(getString(R.string.param_set))

        init()
        set()

        btnSet.setOnClickListener {
            set()
        }

        btnReset.setOnClickListener {
            init()
        }

        btnClientSend.setOnClickListener {
            val msg = etMsg.text.toString()
            val from = etFrom.text.toString()

            SenderService.startService(this, msg, from)
        }
    }

    fun init() {
        val host = mKV.getString(Constants.HOST, "localhost")
        val port = mKV.getInt(Constants.PORT, 9999)
        val key = mKV.getString(Constants.KEY, "mkey")
        val to = mKV.getString(Constants.TO, "指间的微妙")
        val extra = mKV.getString(Constants.EXTRA, "123")
        val from = mKV.getString(Constants.FROM, "10086")

        etServerHost.setText(host)
        etServerPort.setText(port.toString())
        etKey.setText(key)
        etTo.setText(to)
        etExtra.setText(extra)
        etFrom.setText(from)
    }

    fun set(){
        val host = etServerHost.text.toString()
        val port = etServerPort.text.toString().toInt()
        val key = etKey.text.toString()
        val extra = etExtra.text.toString()
        val to = etTo.text.toString()
        val from = etFrom.text.toString()

        mKV.putString(Constants.HOST, host)
        mKV.putInt(Constants.PORT, port)
        mKV.putString(Constants.KEY, key)
        mKV.putString(Constants.EXTRA, extra)
        mKV.putString(Constants.TO, to)
        mKV.putString(Constants.FROM, from)
    }
}
