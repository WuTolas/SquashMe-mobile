package pl.pjatk.squashme

import android.app.Application
import android.content.Context
import pl.pjatk.squashme.config.LocaleUtil
import pl.pjatk.squashme.config.Storage

/**
 * main class with storage to load data like
 * language or theme based on saved preferences
 */
class SquashMe : Application() {
    val storage: Storage by lazy {
        Storage(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.getLocalizedContext(base, Storage(base).getPreferredLocale()))
    }
}
