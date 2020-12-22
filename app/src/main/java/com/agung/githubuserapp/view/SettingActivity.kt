package com.agung.githubuserapp.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.agung.githubuserapp.R
import com.agung.githubuserapp.view.fragment.MyPreferenceFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val actionbar = supportActionBar
        actionbar!!.title = getString(R.string.setting)
        actionbar.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()

    }
}