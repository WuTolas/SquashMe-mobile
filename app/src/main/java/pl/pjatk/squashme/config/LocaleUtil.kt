package pl.pjatk.squashme.config

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.core.os.ConfigurationCompat
import java.util.*

class LocaleUtil(base: Context): ContextWrapper(base)  {
    companion object {
        val supportedLocales = listOf("en", "pl")
        const val OPTION_PHONE_LANGUAGE = "sys_def"

        /**
         * returns the locale to use depending on the preference value
         * when preference value = "sys_def" returns the locale of current system
         * else it returns the locale code e.g. "en", "pl" etc.
         */
        private fun getLocaleFromPrefCode(prefCode: String): Locale{
            val localeCode = if(prefCode != OPTION_PHONE_LANGUAGE) {
                prefCode
            } else {
                val systemLang = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0).language
                if(systemLang in supportedLocales){
                    systemLang
                } else {
                    "en"
                }
            }
            return Locale(localeCode)
        }

        fun getLocalizedConfiguration(prefLocaleCode: String): Configuration {
            val locale = getLocaleFromPrefCode(prefLocaleCode)
            return getLocalizedConfiguration(locale)
        }

        private fun getLocalizedConfiguration(locale: Locale): Configuration {
            val config = Configuration()
            return config.apply {
                config.setLayoutDirection(locale)
                config.setLocale(locale)
                val localeList = LocaleList(locale)
                LocaleList.setDefault(localeList)
                config.setLocales(localeList)
            }
        }

        fun getLocalizedContext(baseContext: Context, prefLocaleCode: String): Context {
            val currentLocale = getLocaleFromPrefCode(prefLocaleCode)
            val baseLocale = getLocaleFromConfiguration(baseContext.resources.configuration)
            Locale.setDefault(currentLocale)
            return if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
                val config = getLocalizedConfiguration(currentLocale)
                baseContext.createConfigurationContext(config)
                baseContext
            } else {
                baseContext
            }
        }

        fun applyLocalizedContext(baseContext: Context, prefLocaleCode: String) {
            val currentLocale = getLocaleFromPrefCode(prefLocaleCode)
            val baseLocale = getLocaleFromConfiguration(baseContext.resources.configuration)
            Locale.setDefault(currentLocale)
            if (!baseLocale.toString().equals(currentLocale.toString(), ignoreCase = true)) {
                val config = getLocalizedConfiguration(currentLocale)
                baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            }
        }

        private fun getLocaleFromConfiguration(configuration: Configuration): Locale {
            return configuration.locales.get(0)
        }
    }
}
