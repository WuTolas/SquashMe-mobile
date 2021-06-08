package pl.pjatk.squashme.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import pl.pjatk.squashme.R

class SettingsActivity : BaseActivity() {

    private lateinit var pl: Button
    private lateinit var en: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        pl = findViewById(R.id.pl)
        en = findViewById(R.id.en)
        pl.setOnClickListener {
            changeLanguage("pl")
        }

        en.setOnClickListener {
            changeLanguage("en")
        }
    }

    private fun changeLanguage(language: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        sharedPreferences.edit()
                .putString("language", language)
                .apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        navigateUpTo(intent)
    }
}
