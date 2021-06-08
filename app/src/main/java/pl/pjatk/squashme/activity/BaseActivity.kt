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

    companion object {
        private const val TAG = "BaseActivity"
    }

    private fun resetTitleBar() {
        try {
            val label = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package name not found")
        }
    }

    override fun attachBaseContext(newBase: Context) {
        oldPrefLocaleCode = Storage(newBase).getPreferredLocale()
        applyOverrideConfiguration(LocaleUtil.getLocalizedConfiguration(oldPrefLocaleCode))

        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetTitleBar()
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
