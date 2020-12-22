package com.agung.githubuserapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.agung.githubuserapp.R
import com.agung.githubuserapp.alarm.AlarmReceiver

class MyPreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var LANGUAGE: String
    private lateinit var REMINDER: String

    private lateinit var languagePreference : Preference
    private lateinit var reminderPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)

        init()
    }

    private fun init() {
        LANGUAGE = resources.getString(R.string.language)
        REMINDER = resources.getString(R.string.reminder)
        languagePreference = findPreference<Preference> (LANGUAGE) as Preference
        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference

        languagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }

        alarmReceiver = AlarmReceiver()
        reminderPreference.isChecked = alarmReceiver.isAlarmSet(requireContext(), AlarmReceiver.TYPE_REPEATING)
        reminderPreference.setOnPreferenceChangeListener { preference, newValue ->
            if(newValue as Boolean){
                alarmReceiver.setRepeatingAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING,
                    "09:00", getString(R.string.reminder_label))
            } else {
                alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING)
            }
            true
        }


    }
}