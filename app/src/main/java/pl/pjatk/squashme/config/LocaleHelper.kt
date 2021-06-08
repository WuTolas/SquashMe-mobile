package pl.pjatk.squashme.config

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import java.util.*


class LocaleHelper {

    // the method is used to set the language at runtime
    companion object {
        private const val selectedLanguage = "Locale.Helper.Selected.Language"

        fun setLocale(context: Context, language: String): Context? {
            persist(context, language)

            // updating the language for devices above android nougat
            return updateResources(context, language)
            // for devices having lower version of android os
        }

        private fun persist(context: Context, language: String) {
            val preferences =
                    PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(selectedLanguage, language)
            editor.apply()
        }

        // the method is used update the language of application by creating
        // object of inbuilt Locale class and passing language argument to it
        @TargetApi(Build.VERSION_CODES.N)// todo good
        private fun updateResources(context: Context, language: String): Context? {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration: Configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }
    }
}
