package com.valairan.inventory;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valairan.inventory.databinding.ActivityLoginBinding;

import java.time.Duration;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth currentAuth;

    public EditText uNameEntry;
    public EditText pWordEntry;
    public TextView SignUpPage;
    public Button LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uNameEntry = findViewById(R.id.uNameEntry);
        pWordEntry = findViewById(R.id.pWordEntry);
        SignUpPage = findViewById(R.id.switchToSignUp);
        LoginButton = findViewById(R.id.loginButton);

        currentAuth = FirebaseAuth.getInstance();

        SignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }




    public void attemptLogin(){

        String email = uNameEntry.getText().toString();
        String pass = pWordEntry.getText().toString();
        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Fields are empty...", Toast.LENGTH_SHORT).show();
            return;
        }else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                uNameEntry.setError("Please enter a valid email.");
                return;
            }
        }
        Toast.makeText(getApplicationContext(), "Logging you in", Toast.LENGTH_SHORT).show();

        currentAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = currentAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();

                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}