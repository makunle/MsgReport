package com.noest.msgreport.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noest.msgreport.R
import com.noest.msgreport.service.RelayService
import com.noest.msgreport.util.Constants
import com.noest.msgreport.util.Constants.Companion.DEVICE_ID
import com.noest.msgreport.util.Constants.Companion.MAIL_SENDER_ACCOUNT
import com.noest.msgreport.util.Constants.Companion.MAIL_SENDER_PASSWORD
import com.noest.msgreport.util.Constants.Companion.MAIL_RECEIVER
import com.noest.msgreport.util.Constants.Companion.TEST_FROM
import com.noest.msgreport.util.Constants.Companion.TEST_MSG
import com.noest.msgreport.util.Constants.Companion.USE_MAIL_WAY
import com.noest.msgreport.util.Setting
import kotlinx.android.synthetic.main.mail_set_layout.view.*

class MailSettingFragment : Fragment() {

    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mail_set_layout, container, false)

        initStat(view)

        view.switchMailRelay.setOnCheckedChangeListener { _, isChecked ->
            Setting.setBoolean(USE_MAIL_WAY, isChecked)
        }

        view.etMailReceiverAddr.addTextChangedListener(textWatcher)
        view.etSenderAddr.addTextChangedListener(textWatcher)
        view.etSenderPassword.addTextChangedListener(textWatcher)
        view.etDeviceId.addTextChangedListener(textWatcher)

        view.etTestMsg.addTextChangedListener(textWatcher)
        view.etTestFrom.addTextChangedListener(textWatcher)

        view.btSendTest.setOnClickListener {
            RelayService.startService(inflater.context,
                view.etTestMsg.text.toString(),
                view.etTestFrom.text.toString(),
                Constants.WAY_MAIL
            )
        }

        mView = view
        return view
    }

    private fun initStat(view: View) {
        view.switchMailRelay.isChecked = Setting.getBoolean(USE_MAIL_WAY, false)

        view.etMailReceiverAddr.setText(Setting.getString(MAIL_RECEIVER, "makunle@qq.com"))
        view.etSenderAddr.setText(Setting.getString(MAIL_SENDER_ACCOUNT, "mkl@noest.top"))
        view.etSenderPassword.setText(Setting.getString(MAIL_SENDER_PASSWORD, ""))
        view.etDeviceId.setText(Setting.getString(DEVICE_ID, ""))

        view.etTestMsg.setText(Setting.getString(TEST_MSG, ""))
        view.etTestFrom.setText(Setting.getString(TEST_FROM, ""))
    }

    val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            Setting.setString(MAIL_RECEIVER, mView.etMailReceiverAddr.text.toString())
            Setting.setString(MAIL_SENDER_ACCOUNT, mView.etSenderAddr.text.toString())
            Setting.setString(MAIL_SENDER_PASSWORD, mView.etSenderPassword.text.toString())
            Setting.setString(DEVICE_ID, mView.etDeviceId.text.toString())

            Setting.setString(TEST_MSG, mView.etTestMsg.text.toString())
            Setting.setString(TEST_FROM, mView.etTestFrom.text.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }
}