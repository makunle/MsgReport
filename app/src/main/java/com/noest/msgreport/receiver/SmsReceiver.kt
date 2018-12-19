package com.noest.msgreport.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import com.noest.msgreport.util.LogX
import com.noest.msgreport.service.SenderService
import java.lang.StringBuilder

class SmsReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "SmsReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val smsArray = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        val msgBody = StringBuilder()
        var from: String? = null

        smsArray.forEach {
            from = it.originatingAddress
            msgBody.append(it.messageBody)

            LogX.d(TAG, " " + it.displayOriginatingAddress + "|" + it.serviceCenterAddress + "|" + it.userData)
        }

        LogX.d(TAG, "sms from: $from \n content: $msgBody")

        SenderService.startService(context, msgBody.toString(), from)

        val keySet = intent.extras.keySet()
        for (key in keySet) {
            LogX.d(TAG, "bundle: " + key + " | " + intent.getStringExtra(key))
        }

        val cursor = context.contentResolver.query(Uri.parse(intent.getStringExtra("msg_uri")), null, null, null, null)
        cursor?.let {
            while (cursor.moveToNext()) {
                LogX.d(TAG, "-------------------------")
                val names = cursor.columnNames
                for (name in names) {
                    LogX.d(TAG, name + " --> " + cursor.getString(cursor.getColumnIndex(name)))
                }
            }
            cursor.close()
        }

    }
}