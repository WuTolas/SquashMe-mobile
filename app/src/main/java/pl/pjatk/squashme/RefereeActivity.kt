package pl.pjatk.squashme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RefereeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_referee)

        RefereeFragment.newInstance("", "")
    }
}
