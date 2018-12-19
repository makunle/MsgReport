package com.noest.msgreport.deliver

import com.noest.minicache.MiniCache
import com.noest.msgreport.util.Constants.Companion.MAIL_ACCOUNT
import com.noest.msgreport.util.Constants.Companion.MAIL_FROM
import com.noest.msgreport.util.Constants.Companion.MAIL_PASSWORD
import com.noest.msgreport.util.Constants.Companion.MAIL_TO
import com.noest.msgreport.util.VerifyCodeGetter
import java.util.*
import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailDeliver : IDeliver {
    val mKV = MiniCache.getDefaultCache()

    override fun deliver(msg: String, from: String): Boolean {
        val mailFrom = mKV.getString(MAIL_FROM, "smsdeliver@163.com")
        val mailTo = mKV.getString(MAIL_TO, "makunle@qq.com")
        val senderAccount = mKV.getString(MAIL_ACCOUNT, "smsdeliver@163.com")
        val senderPassword = mKV.getString(MAIL_PASSWORD, "abc122333")

        val props = Properties()
        props.setProperty("mail.smtp.auth", "true")
        props.setProperty("mail.transport.protocol", "smtp")
        props.setProperty("mail.smtp.host", "smtp.163.com")

        val session = Session.getInstance(props)

        val mailMsg = MimeMessage(session)
        mailMsg.setFrom(InternetAddress(mailFrom, from))
        mailMsg.setRecipient(Message.RecipientType.TO, InternetAddress(mailTo))
        mailMsg.setSubject(createSubject(msg, from), "UTF-8")
        mailMsg.setContent(createContent(msg, from), "text/html;charset=UTF-8")

        val transport = session.transport
        transport.connect(senderAccount, senderPassword)
        transport.sendMessage(mailMsg, mailMsg.allRecipients)
        transport.close()

        return true
    }

    private fun createContent(msg: String, from: String): String {
        return "$from:\n $msg"
    }

    private fun createSubject(msg: String, from: String): String {
        val verifyCode = VerifyCodeGetter.Get(msg)
        if (verifyCode?.second == null) {
            return from
        }
        val code = verifyCode.second
        val title = verifyCode.first ?: ""
        return title + "验证码: " + code
    }

}