package pl.pjatk.squashme.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pl.pjatk.squashme.SquashMe
import pl.pjatk.squashme.config.LocaleUtil
import pl.pjatk.squashme.config.Storage

open class BaseActivity : AppCompatActivity() {
    private lateinit var oldPrefLocaleCode: String
    protected val storage: Storage by lazy {
        (application as SquashMe).storage
    }

    /**
     * updates the toolbar text locale if it set from the android:label property of Manifest
     */
    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (e: PackageManager.NameNotFoundException) {
        }
    }

    override fun attachBaseContext(newBase: Context) {
        oldPrefLocaleCode = Storage(newBase).getPreferredLocale()
        applyOverrideConfiguration(LocaleUtil.getLocalizedConfiguration(oldPrefLocaleCode))

        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetTitle()
    }

    override fun onResume() {
        val currentLocaleCode = Storage(this).getPreferredLocale()
        if (oldPrefLocaleCode != currentLocaleCode) {
            recreate()
            oldPrefLocaleCode = currentLocaleCode
        }
        super.onResume()
    }
}
