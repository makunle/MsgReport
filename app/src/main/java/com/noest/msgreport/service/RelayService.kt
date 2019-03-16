package com.noest.msgreport.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.noest.msgreport.deliver.FtqqDeliver
import com.noest.msgreport.util.Constants.Companion.WAY_MAIL
import com.noest.msgreport.util.Constants.Companion.WAY_WX
import com.noest.msgreport.deliver.MailDeliver
import com.noest.msgreport.deliver.WxDeliver
import com.noest.msgreport.util.Constants.Companion.WAY_ALL
import com.noest.msgreport.util.Constants.Companion.WAY_FTQQ
import com.noest.msgreport.util.Setting
import java.lang.Exception

class RelayService : IntentService("RelayService") {
    companion object {
        const val TAG = "RelayService"

        const val EXTRA_MSG = "msg"
        const val EXTRA_FROM = "from"
        const val EXTRA_WAY = "way"

        val delivers = mapOf(
            WAY_MAIL to MailDeliver,
            WAY_WX to WxDeliver,
            WAY_FTQQ to FtqqDeliver
        )

        fun startService(context: Context, msg: String?, from: String?, way: ArrayList<String> = WAY_ALL) {
            val intent = Intent(context, RelayService::class.java)
            intent.putExtra(EXTRA_FROM, from)
            intent.putExtra(EXTRA_MSG, msg)
            intent.putStringArrayListExtra(EXTRA_WAY, way)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent) {
        val from = intent.getStringExtra(EXTRA_FROM)
        val msg = intent.getStringExtra(EXTRA_MSG)
        val way = intent.getStringArrayListExtra(EXTRA_WAY)

        way.forEach {
            val key = it
            val use = Setting.getBoolean(key, false)
            if (use) {
                try {
                    delivers[key]?.deliver(msg, from)
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            applicationContext,
                            "relayer failed with deliver: $key, check params or network\n${e.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}