package com.noest.msgreport.deliver

import com.noest.msgreport.util.Constants
import com.noest.msgreport.util.LogX
import com.noest.msgreport.util.Setting
import com.noest.msgreport.util.VerifyCodeGetter
import java.net.URL

object FtqqDeliver : IDeliver {
    override fun deliver(msg: String, from: String): Boolean {
        val url = makePostUrl(msg, from)
        val result = URL(url).readBytes().toString()
        LogX.d("Ftqq", "result: " + result)
        return true
    }

    fun makePostUrl(msg: String, from: String): String {
        val key = Setting.getString(Constants.FTQQ_SECKEY, "")

        val title: String?
        val body: String?

        val verifyCode = VerifyCodeGetter.Get(msg)
        if (verifyCode?.second == null) {
            title = "来自 [$from] 的短信"
        } else {
            val code = verifyCode.second
            val flag = verifyCode.first ?: ""
            title = "$flag 验证码：$code"
        }
        body = "来自$from 的短信：$msg"
        return "https://sc.ftqq.com/$key.send?text=$title&desp=$body"
    }
}