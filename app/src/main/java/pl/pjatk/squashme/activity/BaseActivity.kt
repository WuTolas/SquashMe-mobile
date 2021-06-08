package pl.pjatk.squashme.activity

import android.content.Context
import android.content.ContextWrapper
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pl.pjatk.squashme.config.ContextUtils
import java.util.*

open class BaseActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "BaseActivity"
    }

    override fun attachBaseContext(newBase: Context) {

        val prefs = PreferenceManager.getDefaultSharedPreferences(newBase)
        val language = prefs.getString("language", "en")
        Log.i(TAG, "Chosen language to $language")
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, Locale(language!!))
        super.attachBaseContext(localeUpdatedContext)
    }
}
