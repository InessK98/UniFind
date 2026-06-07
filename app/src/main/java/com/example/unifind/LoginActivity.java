package com.example.unifind;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unifind.models.User;

import java.util.UUID;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private Realm realm;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Removed EdgeToEdge to fix the black screen issue
        setContentView(R.layout.activity_login);

        realm = Realm.getDefaultInstance();
        sharedPreferences = getSharedPreferences("UniFindPrefs", Context.MODE_PRIVATE);
        
        emailInput = findViewById(R.id.emailInput);
        Button loginButton = findViewById(R.id.loginButton);
        Button microsoftButton = findViewById(R.id.microsoftButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            handleLogin(email);
        });

        microsoftButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty() || !email.toLowerCase().endsWith("@uir.ac.ma")) {
                email = "etudiant.microsoft@uir.ac.ma";
            }
            Toast.makeText(this, "Connexion Microsoft réussie", Toast.LENGTH_SHORT).show();
            handleLogin(email);
        });
    }

    private void handleLogin(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer votre email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.toLowerCase().endsWith("@uir.ac.ma")) {
            Toast.makeText(this, "Seuls les étudiants de l'UIR peuvent se connecter", Toast.LENGTH_LONG).show();
            return;
        }

        realm.executeTransaction(r -> {
            User user = r.where(User.class).equalTo("email", email).findFirst();
            if (user == null) {
                user = r.createObject(User.class, UUID.randomUUID().toString());
                user.setEmail(email);
                String name = email.split("@")[0].replace(".", " ");
                if (name.length() > 0) {
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                }
                user.setName(name);
                user.setUniversity("UIR");
            }
        });

        sharedPreferences.edit().putString("loggedInEmail", email).apply();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}