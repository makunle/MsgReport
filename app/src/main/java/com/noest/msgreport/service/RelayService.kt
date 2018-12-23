package com.noest.msgreport.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.noest.msgreport.util.Constants.Companion.WAY_MAIL
import com.noest.msgreport.util.Constants.Companion.WAY_WX
import com.noest.msgreport.relayer.MailDeliver
import com.noest.msgreport.relayer.WxDeliver
import com.noest.msgreport.util.Constants
import com.noest.msgreport.util.Setting

class RelayService : IntentService("RelayService") {
    companion object {
        const val TAG = "RelayService"

        const val EXTRA_MSG = "msg"
        const val EXTRA_FROM = "from"
        const val EXTRA_WAY = "way"

        fun startService(context: Context, msg: String?, from: String?, way: String = WAY_MAIL) {
            val intent = Intent(context, RelayService::class.java)
            intent.putExtra(EXTRA_FROM, from)
            intent.putExtra(EXTRA_MSG, msg)
            intent.putExtra(EXTRA_WAY, way)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent) {
        val from = intent.getStringExtra(EXTRA_FROM)
        val msg = intent.getStringExtra(EXTRA_MSG)
        val way = intent.getStringExtra(EXTRA_WAY)

        when (way) {
            WAY_MAIL -> {
                val useMail = Setting.getBoolean(Constants.USE_MAIL_WAY, false)
                if (useMail) {
                    MailDeliver.deliver(msg, from)
                }
            }
            WAY_WX -> {
                val useWx = Setting.getBoolean(Constants.USE_WX_WAY, false)
                if (useWx) {
                    WxDeliver.deliver(msg, from)
                }
            }
        }
    }
}