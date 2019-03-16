package com.noest.msgreport.deliver

import android.text.TextUtils
import com.noest.msgreport.util.Constants
import com.noest.msgreport.util.Constants.Companion.DEVICE_ID
import com.noest.msgreport.util.Setting
import com.noest.msgreport.util.VerifyCodeGetter
import java.lang.Exception
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailDeliver : IDeliver {

    override fun deliver(msg: String, from: String): Boolean {
        val mailReceiver = Setting.getString(Constants.MAIL_RECEIVER, "")
        val senderAddr = Setting.getString(Constants.MAIL_SENDER_ACCOUNT, "")
        val senderPassword = Setting.getString(Constants.MAIL_SENDER_PASSWORD, "")
        val deviceId = Setting.getString(Constants.DEVICE_ID, "")

        val props = Properties()
        if (!TextUtils.isEmpty(senderPassword)) {
            props.setProperty("mail.smtp.auth", "true")
        }
        props.setProperty("mail.smtp.host", "mail.noest.top")

        val session = Session.getInstance(props)

        val mailMsg = MimeMessage(session)
        // 发件人
        mailMsg.setFrom(InternetAddress(senderAddr, deviceId))
        mailMsg.setRecipient(Message.RecipientType.TO, InternetAddress(mailReceiver))
        mailMsg.setSubject(createSubject(msg, from), "UTF-8")
        mailMsg.setContent(createContent(msg, from), "text/html;charset=UTF-8")

        var transport: Transport? = null
        try {
            transport = session.transport
            transport.connect(senderAddr, senderPassword)
            transport.sendMessage(mailMsg, mailMsg.allRecipients)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (transport != null) {
                transport.close()
            }
        }

        return true
    }

    private fun createContent(msg: String, from: String): String {
        val device = Setting.getString(DEVICE_ID, "")
        return "$msg <br> 【$from】-【$device】"
    }

    private fun createSubject(msg: String, from: String): String {
        val verifyCode = VerifyCodeGetter.Get(msg)
        if (verifyCode?.second == null) {
            return "短信: $from"
        }
        val code = verifyCode.second
        val title = verifyCode.first ?: ""
        return "$title 验证码: $code"
    }

}