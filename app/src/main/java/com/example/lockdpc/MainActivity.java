
package com.example.lockdpc;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LockDPCPrefs";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FIRST_RUN = "firstRun";

    private String generatedPassword;
    private SharedPreferences prefs;

    private TextView tvShowPassword;
    private EditText etPasswordInput;
    private Button btnUnlock, btnCopyPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        boolean firstRun = prefs.getBoolean(KEY_FIRST_RUN, true);

        tvShowPassword = findViewById(R.id.tvShowPassword);
        etPasswordInput = findViewById(R.id.etPasswordInput);
        btnUnlock = findViewById(R.id.btnUnlock);
        btnCopyPassword = findViewById(R.id.btnCopyPassword);

        if (firstRun) {
            generatedPassword = generateRandomPassword(18);
            prefs.edit()
                .putString(KEY_PASSWORD, generatedPassword)
                .putBoolean(KEY_FIRST_RUN, false)
                .apply();
            showPassword();
        } else {
            generatedPassword = prefs.getString(KEY_PASSWORD, "");
            hidePassword();
        }

        btnCopyPassword.setOnClickListener(v -> copyPasswordToClipboard());

        btnUnlock.setOnClickListener(v -> {
            String input = etPasswordInput.getText().toString();
            if (input.equals(generatedPassword)) {
                Toast.makeText(this, "נפתח בהצלחה!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "סיסמה שגויה", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPassword() {
        tvShowPassword.setText("הסיסמה שלך:
" + generatedPassword);
        tvShowPassword.setVisibility(View.VISIBLE);
        etPasswordInput.setVisibility(View.VISIBLE);
        btnUnlock.setVisibility(View.VISIBLE);
        btnCopyPassword.setVisibility(View.VISIBLE);
    }

    private void hidePassword() {
        tvShowPassword.setVisibility(View.GONE);
        etPasswordInput.setVisibility(View.VISIBLE);
        btnUnlock.setVisibility(View.VISIBLE);
        btnCopyPassword.setVisibility(View.GONE);
        etPasswordInput.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void copyPasswordToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("password", generatedPassword);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "הסיסמה הועתקה ללוח", Toast.LENGTH_SHORT).show();
    }

    private String generateRandomPassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
