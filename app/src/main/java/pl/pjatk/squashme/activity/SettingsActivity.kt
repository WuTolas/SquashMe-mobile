package pl.pjatk.squashme.activity

import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.os.ConfigurationCompat
import pl.pjatk.squashme.R
import pl.pjatk.squashme.config.LocaleUtil
import java.util.*

class SettingsActivity : BaseActivity() {

    private lateinit var op1: RadioButton
    private lateinit var op2: RadioButton
    private lateinit var radioGroup: RadioGroup
    private lateinit var tvAppLocale: TextView
    private lateinit var pl: Button
    private lateinit var en: Button
    private lateinit var firstLocaleCode: String
    private lateinit var secondLocaleCode: String
    private lateinit var currentSystemLocaleCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tvAppLocale = findViewById(R.id.tvAppLocale)
        radioGroup = findViewById(R.id.radioGroup)
        op1 = findViewById(R.id.op1)
        op2 = findViewById(R.id.op2)

//        pl = findViewById(R.id.pl)
//        en = findViewById(R.id.en)
//        pl.setOnClickListener {
//            changeLanguage("pl")
//        }
//
//        en.setOnClickListener {
//            changeLanguage("en")
//        }

        currentSystemLocaleCode = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).language
        if (storage.getPreferredLocale() == LocaleUtil.OPTION_PHONE_LANGUAGE) {
            if (currentSystemLocaleCode in LocaleUtil.supportedLocales) {
                tvAppLocale.text = getString(R.string.phone_language, Locale(currentSystemLocaleCode).displayLanguage)
            } else {
                //current system language is neither English nor my second language (for me Bangla)
                tvAppLocale.text = "English"
            }
        } else {
            if (currentSystemLocaleCode == storage.getPreferredLocale()) {
                tvAppLocale.text = getString(R.string.phone_language, Locale(currentSystemLocaleCode).displayLanguage)
            } else {
                tvAppLocale.text = Locale(storage.getPreferredLocale()).displayLanguage
            }
        }
        firstLocaleCode = if (currentSystemLocaleCode in LocaleUtil.supportedLocales) {
            currentSystemLocaleCode
        } else {
            if (storage.getPreferredLocale() == LocaleUtil.OPTION_PHONE_LANGUAGE) {
                //current system language is neither English nor my second language
                "en"
            } else {
                storage.getPreferredLocale()
            }
        }
        secondLocaleCode = getSecondLanguageCode()
        initRadioButtonUI()
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.op1 -> {
                    updateAppLocale(LocaleUtil.OPTION_PHONE_LANGUAGE)
                    recreate()
                }
                R.id.op2 -> {
                    updateAppLocale(secondLocaleCode)
                    recreate()
                }
            }
        }
    }

    private fun getSecondLanguageCode(): String {
        return if (firstLocaleCode == "en") "pl" else "en"
    }

    private fun initRadioButtonUI() {
        if (currentSystemLocaleCode in LocaleUtil.supportedLocales) {
            op1.text = getString(R.string.phone_language, getLanguageNameByCode(firstLocaleCode))
        } else {
            op1.text = getLanguageNameByCode(firstLocaleCode)
        }
        op2.text = getLanguageNameByCode(secondLocaleCode)
        if (storage.getPreferredLocale() == secondLocaleCode) op2.isChecked = true
        else op1.isChecked = true
    }

    private fun getLanguageNameByCode(code: String): String {
        val tempLocale = Locale(code)
        return tempLocale.getDisplayLanguage(tempLocale)
    }

    private fun updateAppLocale(locale: String) {
        storage.setPreferredLocale(locale)
        LocaleUtil.applyLocalizedContext(applicationContext, locale)
    }
}
