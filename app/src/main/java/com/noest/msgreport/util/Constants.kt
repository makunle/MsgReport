package com.noest.msgreport.util

class Constants {
    companion object {
        const val SERVER_HOST = "host"
        const val SERVER_PORT = "port"
        const val SERVER_KEY = "key"
        const val RECEIVER_NAME = "receiver_name"
        const val TO = "to"
        const val MSG = "msg"
        const val KEY = "key"
        const val FROM = "from"
        const val WAY_WX = "wx"
        const val WAY_MAIL = "mail"
        val WAY_ALL = arrayListOf(WAY_WX, WAY_MAIL)

        const val MAIL_RECEIVER = "mail_receiver"

        const val DEVICE_ID = "device_id"

        const val MAIL_SENDER_ACCOUNT = "mail_sender_account"
        const val MAIL_SENDER_PASSWORD = "mail_sender_passwd"

        const val TEST_MSG = "test_msg"
        const val TEST_FROM = "test_from"
    }
}