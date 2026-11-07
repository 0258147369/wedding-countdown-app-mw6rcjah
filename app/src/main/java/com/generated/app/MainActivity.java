package com.generated.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    private TextView countdownText;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("wedding_prefs", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(64, 64, 64, 64);
        layout.setBackgroundColor(0xFFFCE4EC);

        countdownText = new TextView(this);
        countdownText.setTextSize(32);
        countdownText.setGravity(Gravity.CENTER);
        updateCountdownDisplay();

        Button dateBtn = new Button(this);
        dateBtn.setText(getString(R.string.set_date));
        dateBtn.setOnClickListener(v -> showDatePicker());

        layout.addView(countdownText);
        layout.addView(dateBtn);
        setContentView(layout);
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, day);
            prefs.edit().putLong("wedding_date", selected.getTimeInMillis()).apply();
            updateCountdownDisplay();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateCountdownDisplay() {
        long weddingTime = prefs.getLong("wedding_date", 0);
        if (weddingTime == 0) {
            countdownText.setText("אנא קבע תאריך");
        } else {
            long diff = weddingTime - System.currentTimeMillis();
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            if (days < 0) {
                countdownText.setText("היום הגדול הגיע!");
            } else {
                countdownText.setText(days + " " + getString(R.string.days_left));
            }
        }
    }
}
