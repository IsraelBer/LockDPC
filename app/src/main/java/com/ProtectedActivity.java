package com.lockdpc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProtectedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String stored = prefs.getString("random_password", "");

        setContentView(R.layout.protected_activity);
        EditText input = findViewById(R.id.passwordInput);
        Button btn = findViewById(R.id.enterButton);

        btn.setOnClickListener(v -> {
            String entered = input.getText().toString();
            if (stored.equals(entered)) {
                Toast.makeText(this, "Access granted", Toast.LENGTH_SHORT).show();
                // TODO: show real settings
            } else {
                Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
