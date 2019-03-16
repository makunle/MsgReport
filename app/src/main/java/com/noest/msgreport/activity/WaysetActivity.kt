package com.noest.msgreport.activity

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle

import com.noest.msgreport.R
import com.noest.msgreport.fragment.FtqqSettingFragment
import com.noest.msgreport.fragment.MailSettingFragment
import com.noest.msgreport.fragment.WxSettingFragment
import kotlinx.android.synthetic.main.activity_wayset.*

class WaysetActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wayset)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MailSettingFragment()
                1 -> WxSettingFragment()
                2 -> FtqqSettingFragment()
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
