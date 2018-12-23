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
import com.noest.msgreport.util.Constants.Companion.DEVICE_ID
import com.noest.msgreport.util.Constants.Companion.RECEIVER_NAME
import com.noest.msgreport.util.Constants.Companion.SERVER_HOST
import com.noest.msgreport.util.Constants.Companion.SERVER_KEY
import com.noest.msgreport.util.Constants.Companion.SERVER_PORT
import com.noest.msgreport.util.Constants.Companion.TEST_FROM
import com.noest.msgreport.util.Constants.Companion.TEST_MSG
import com.noest.msgreport.util.Constants.Companion.USE_WX_WAY
import com.noest.msgreport.util.Constants.Companion.WAY_WX
import com.noest.msgreport.util.Setting
import kotlinx.android.synthetic.main.wx_set_layout.view.*

class WxSettingFragment : Fragment() {

    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.wx_set_layout, container, false)

        initStat(view)

        view.switchWxRelay.setOnCheckedChangeListener { _, isChecked ->
            Setting.setBoolean(USE_WX_WAY, isChecked)
        }

        view.etWxServerHost.addTextChangedListener(textWatcher)
        view.etWxServerPort.addTextChangedListener(textWatcher)
        view.etWxServerKey.addTextChangedListener(textWatcher)
        view.etWxReceiveName.addTextChangedListener(textWatcher)
        view.etDeviceId.addTextChangedListener(textWatcher)

        view.etTestMsg.addTextChangedListener(textWatcher)
        view.etTestFrom.addTextChangedListener(textWatcher)

        view.btSendTest.setOnClickListener {
            RelayService.startService(inflater.context,
                view.etTestMsg.text.toString(),
                view.etTestFrom.text.toString(),
                WAY_WX)
        }

        mView = view
        return view
    }

    private fun initStat(view: View) {
        view.switchWxRelay.isChecked = Setting.getBoolean(USE_WX_WAY, false)

        view.etWxServerHost.setText(Setting.getString(SERVER_HOST, "localhost"))
        view.etWxServerPort.setText(Setting.getString(SERVER_PORT, "9999"))
        view.etWxServerKey.setText(Setting.getString(SERVER_KEY, ""))
        view.etWxReceiveName.setText(Setting.getString(RECEIVER_NAME, ""))
        view.etDeviceId.setText(Setting.getString(DEVICE_ID, ""))

        view.etTestMsg.setText(Setting.getString(TEST_MSG, ""))
        view.etTestFrom.setText(Setting.getString(TEST_FROM, ""))
    }

    val textWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            Setting.setString(SERVER_HOST, mView.etWxServerHost.text.toString())
            Setting.setString(SERVER_PORT, mView.etWxServerPort.text.toString())
            Setting.setString(SERVER_KEY, mView.etWxServerKey.text.toString())
            Setting.setString(RECEIVER_NAME, mView.etWxReceiveName.text.toString())
            Setting.setString(DEVICE_ID, mView.etDeviceId.text.toString())

            Setting.setString(TEST_MSG, mView.etTestMsg.text.toString())
            Setting.setString(TEST_FROM, mView.etTestFrom.text.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }
}