package pl.pjatk.squashme

import android.app.Application
import android.content.Context
import pl.pjatk.squashme.config.LocaleUtil
import pl.pjatk.squashme.config.Storage

class SquashMe : Application() {
    val storage: Storage by lazy {
        Storage(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtil.getLocalizedContext(base, Storage(base).getPreferredLocale()))
    }
}
