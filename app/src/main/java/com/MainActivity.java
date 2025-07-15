package com.lockdpc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {
    private static final String PASSWORD_KEY = "random_password";
    private static final String PASSWORD_SHOWN_KEY = "password_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String storedPassword = prefs.getString(PASSWORD_KEY, null);
        boolean shown = prefs.getBoolean(PASSWORD_SHOWN_KEY, false);

        if (storedPassword == null) {
            storedPassword = generateRandomPassword(18);
            prefs.edit().putString(PASSWORD_KEY, storedPassword).apply();
        }

        if (!shown) {
            setContentView(R.layout.activity_main);
            TextView text = findViewById(R.id.passwordText);
            text.setText("Your password: " + storedPassword);
            Button copyBtn = findViewById(R.id.copyButton);
            copyBtn.setOnClickListener(v -> {
                android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setPrimaryClip(android.content.ClipData.newPlainText("Password", storedPassword));
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                prefs.edit().putBoolean(PASSWORD_SHOWN_KEY, true).apply();
                startActivity(new Intent(this, ProtectedActivity.class));
                finish();
            });
        } else {
            startActivity(new Intent(this, ProtectedActivity.class));
            finish();
        }
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#&!";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
