package com.freisia.github.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import com.freisia.github.R
import com.freisia.github.ui.favorite.FavoriteActivity
import com.freisia.github.ui.search.SearchActivity
import com.freisia.github.utilitas.DailyReminderReceiver
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.title = resources.getString(R.string.setting)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val a = Intent(this.applicationContext, SearchActivity::class.java)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuin -> {
                val locale = Locale("in")
                changeLocale(locale)
            }
            R.id.menuenus -> {
                val locale = Locale("en-US")
                changeLocale(locale)
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    private fun changeLocale(locale : Locale){
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        recreate()
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener{

        private lateinit var notification : String
        private lateinit var isNotification : CheckBoxPreference
        private lateinit var dailyReminderReceiver: DailyReminderReceiver


        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preference, rootKey)
            init()
            setData(savedInstanceState)
        }

        private fun init(){
            notification = resources.getString(R.string.notification)
            isNotification = findPreference<CheckBoxPreference>(notification) as CheckBoxPreference
            dailyReminderReceiver = DailyReminderReceiver()
        }

        private fun setData(savedInstanceState: Bundle?){
            if(savedInstanceState == null){
                val sharedPreferences = preferenceManager.sharedPreferences
                isNotification.isChecked = sharedPreferences.getBoolean(notification,false)
            } else{
                isNotification.isChecked = savedInstanceState.getBoolean("SHARED")
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putBoolean("SHARED",isNotification.isChecked)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences,
            key: String?
        ) {
            if (key == notification){
                isNotification.isChecked = sharedPreferences.getBoolean(notification,false)
                if(!isNotification.isChecked){
                    dailyReminderReceiver.cancelReminder(requireContext())
                } else {
                    dailyReminderReceiver.setRepeatingReminder9AM(requireContext())
                }
            }
        }
    }
}