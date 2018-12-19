package com.noest.msgreport.util

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Created by Administrator on 2017/8/7.
 */

object VerifyCodeGetter {
    private val TAG = "GetCode"

    private val signReg = "(?:是|为| is|:|：|（)"
    private val codeReg = "([a-zA-Z0-9]*)"
    private val typeReg = "((?:验证|交易|授权|随机|登录密|验证代|提货|兑换|动态)码|code)"

    /**
     * 执行预处理操作
     * 1、删除短信开头的【名称】
     * 2、合并flag所在区域：
     * a、（xx flag xx） -->  flag
     * b、 flag sign -->  flag
     *
     * @param str
     * @return
     */
    fun Get(orgStr: String): Pair<String?, String?>? {
        var str = orgStr

        val type = getCodeType(str) ?: return null           //获取验证码类型
        str = removeNameSurround(str, type)      //删除(xxx type xxx)中的xxx
        val titleInfo = removeTitleSurround(str)           //删除【xxx】类似的title，并获取title
        val code = getCode(titleInfo.second, type)

        return Pair(titleInfo.first, code)
    }

    /**
     * @return title, 去除title的内容
     */
    private fun removeTitleSurround(orgStr: String): Pair<String?, String> {
        var str = orgStr
        val nameSurround = "(【.*?】)"
        val m = Pattern.compile(nameSurround, Pattern.COMMENTS).matcher(str)
        var title: String? = null
        if (m.lookingAt()) {
            title = m.group()
            str = m.replaceAll("")
        }
        return Pair(title, str)
    }

    /**
     * //删除(xxx type xxx)中的xxx
     *
     * @param str
     * @return
     */
    fun removeNameSurround(orgStr: String, type: String): String {
        var str = orgStr
        val nameSurround = "(【.*?$type.*?】) | (\\(.*?$type.*?\\)) | (\\[*?$type.*?\\]) | (（.*?$type.*?）)"
        val m = Pattern.compile(nameSurround, Pattern.COMMENTS).matcher(str)

        var start = 0
        var minGropStr = "  "
        while (m.find(start)) {
            if (start == 0)
                minGropStr = m.group()
            else if (minGropStr.length > m.group().length) {
                minGropStr = m.group()
            }
            start = m.start() + 1
        }
        str = str.replace(minGropStr.toRegex(), type)
        return str
    }

    /**
     * 获取验证码类型
     * 有比较特殊的短信，"您正在办理掌厅随机码登录业务，验证码是399243，" 包含随机码，验证码
     * 优先选择 typeReg signReg+ 的 [.*码]
     * @param str
     * @return
     */
    fun getCodeType(str: String): String? {
        val withSign = Pattern.compile("($typeReg)$signReg+").matcher(str)
        if (withSign.find()) {
            return withSign.group(1)
        }
        val m = Pattern.compile(typeReg).matcher(str)
        return if (m.find()) {
            m.group()
        } else null
    }

    /**
     * 获取验证短信中的验证码
     * 首先看是否有  验证码[是\为\:\(]之类的字样，有的话验证码为其后紧跟的内容
     * 如果没有的话先找到  验证码  所在的位置，然后找距离它最近的 可能为code的内容
     * 距离相同时，默认左边为有效内容
     *
     * @param msg
     * @return
     */
    fun getCode(msg: String, type: String): String? {

        val flagReg = type + signReg

        val m = Pattern.compile(codeReg).matcher(msg)
        val fullFlag = Pattern.compile("$flagReg+").matcher(msg)
        val flag = Pattern.compile("$flagReg*").matcher(msg)

        //有明确的标识时，直接确定验证码
        var index = -1
        if (fullFlag.find()) {
            index = fullFlag.end()
        }
        var haveFlagRes: String? = null
        while (index != -1 && m.find(index)) {
            if (!TextUtils.isEmpty(m.group())) {
                haveFlagRes = m.group()
                break
            }
            index = m.end() + 1
            if (index >= msg.length) break
        }
        if (!TextUtils.isEmpty(haveFlagRes)) return haveFlagRes


        if (!flag.find()) return null

        //无明确标识时，找寻距离type最近的
        var withoutFlagRes: String? = null
        var dist = Integer.MAX_VALUE
        var newDist = -1

        while (m.find()) {
            if (!TextUtils.isEmpty(m.group())) {
                if (m.start() < 3) return m.group() //最开始的一般是验证码

                if (m.end() <= flag.start()) {
                    newDist = flag.start() - m.end()
                } else {
                    newDist = m.start() - flag.end()
                }
                if (newDist < dist) {
                    withoutFlagRes = m.group()
                    dist = newDist
                }
            }
        }
        return withoutFlagRes
    }
}
