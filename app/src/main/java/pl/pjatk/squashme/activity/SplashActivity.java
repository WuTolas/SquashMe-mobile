package pl.pjatk.squashme.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import pl.pjatk.squashme.R;

/**
 * Splash activity for showing splash image on app startup.
 */
public class SplashActivity extends Activity {

    /**
     * Shows splash for 1 second, finishes this activity and moves to MainActivity.
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1000);
    }
}