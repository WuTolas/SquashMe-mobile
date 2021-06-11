package pl.pjatk.squashme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.pjatk.squashme.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quickMatchButton = findViewById(R.id.quick_match);
        quickMatchButton.setOnClickListener(V -> launchActivity(QuickMatchActivity.class));

        Button tournamentButton = findViewById(R.id.tournament);
        tournamentButton.setOnClickListener(V -> launchActivity(TournamentActivity.class));

        Button settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(v -> launchActivity(SettingsActivity.class));

        Button historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(v -> launchActivity(HistoryActivity.class));
    }

    private void launchActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
