package pl.pjatk.squashme.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import pl.pjatk.squashme.R
import pl.pjatk.squashme.config.LocaleUtil

class SettingsActivity : BaseActivity() {

    private lateinit var languages: Spinner

    companion object {
        private const val TAG = "SettingsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        languages = findViewById(R.id.languages_list)

        getCurrentLanguage()
        languages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = languages.selectedItem.toString()
                if (selectedItem != storage.getPreferredLocale()) {
                    updateAppLocale(languages.selectedItem.toString())
                    recreate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun getCurrentLanguage() {
        val adapter = languages.adapter
        val n = adapter.count
        for (i in 0 until n) {
            if (adapter.getItem(i) == storage.getPreferredLocale()) {
                languages.setSelection(i)
                break
            }
        }
    }

    private fun updateAppLocale(locale: String) {
        storage.setPreferredLocale(locale)
        LocaleUtil.applyLocalizedContext(applicationContext, locale)
        Log.i(TAG, "Chosen language: $locale")
    }
}
