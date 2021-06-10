package pl.pjatk.squashme.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import pl.pjatk.squashme.R
import pl.pjatk.squashme.config.LocaleUtil
import pl.pjatk.squashme.model.Language
import java.util.*

class SettingsActivity : BaseActivity() {

    private lateinit var languages: Spinner
    private lateinit var themes: Spinner

    companion object {
        private const val TAG = "SettingsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        languages = findViewById(R.id.languages_list)
        themes = findViewById(R.id.theme_list)

        getCurrentLanguage()
        languages.onItemSelectedListener = onLanguageSelectedListener()

        getCurrentTheme()
        themes.onItemSelectedListener = onThemeSelectedListener()
    }

    private fun onLanguageSelectedListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = languages.selectedItem.toString()
                val langCode = getLanguageCode(selectedItem)
                if (langCode != storage.getPreferredLocale()) {
                    updateAppLocale(langCode)
                    recreate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun onThemeSelectedListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var themeMode = AppCompatDelegate.getDefaultNightMode()
                when (themes.selectedItem.toString()) {
                    getString(R.string.darkMode) -> {
                        themeMode = AppCompatDelegate.MODE_NIGHT_YES
                    }
                    getString(R.string.lightMode) -> {
                        themeMode = AppCompatDelegate.MODE_NIGHT_NO
                    }
                    getString(R.string.systemSetting) -> {
                        themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                }
                AppCompatDelegate.setDefaultNightMode(themeMode)
                storage.setTheme(themeMode)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun getCurrentLanguage() {
        val adapter = languages.adapter
        val n = adapter.count
        for (i in 0 until n) {
            val code = getLanguageCode(adapter.getItem(i))
            if (code == storage.getPreferredLocale()) {
                languages.setSelection(i)
                break
            }
        }
    }

    private fun getCurrentTheme() {
        val adapter = themes.adapter
        val n = adapter.count
        for (i in 0 until n) {
            val theme = adapter.getItem(i).toString()
            var mode = AppCompatDelegate.getDefaultNightMode()
            when (theme) {
                getString(R.string.darkMode) -> {
                    mode = AppCompatDelegate.MODE_NIGHT_YES
                }
                getString(R.string.lightMode) -> {
                    mode = AppCompatDelegate.MODE_NIGHT_NO
                }
                getString(R.string.systemSetting) -> {
                    mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
            if (mode == storage.getTheme()) {
                themes.setSelection(i)
                break
            }
        }
    }

    private fun getLanguageCode(language: Any): String {
        return Language.valueOf(language.toString().toUpperCase(Locale.getDefault())).code
    }

    private fun updateAppLocale(locale: String) {
        storage.setPreferredLocale(locale)
        LocaleUtil.applyLocalizedContext(applicationContext, locale)
        Log.i(TAG, "Chosen language: $locale")
    }
}
