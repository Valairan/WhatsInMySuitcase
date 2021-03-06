package com.valairan.inventory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth currentAuth;

    public EditText uNameEntry;
    public EditText pWordEntry;
    public TextView SignUpPage;
    public Button LoginButton;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uNameEntry = findViewById(R.id.uNameEntry);
        pWordEntry = findViewById(R.id.pWordEntry);
        SignUpPage = findViewById(R.id.switchToSignUp);
        LoginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.loadingIcon_login);

        uNameEntry.setVisibility(View.INVISIBLE);
        pWordEntry.setVisibility(View.INVISIBLE);
        SignUpPage.setVisibility(View.INVISIBLE);
        LoginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        currentAuth = FirebaseAuth.getInstance();

        SignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });


        FirebaseUser currentUser = currentAuth.getCurrentUser();


        if (currentUser == FirebaseAuth.getInstance().getCurrentUser() && currentUser != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    // Actions to do after 10 seconds
                }
            }, 1000);

        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    uNameEntry.setVisibility(View.VISIBLE);
                    pWordEntry.setVisibility(View.VISIBLE);
                    SignUpPage.setVisibility(View.VISIBLE);
                    LoginButton.setVisibility(View.VISIBLE);

                    // Actions to do after 10 seconds
                }
            }, 2000);

        }


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }


    public void attemptLogin() {

        String email = uNameEntry.getText().toString();
        String pass = pWordEntry.getText().toString();
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), Constants.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                uNameEntry.setError("Please enter a valid email.");
                return;
            }
        }

        currentAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = currentAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(intent);

                            // ATTENTION: This was auto-generated to handle app links.
                            Intent appLinkIntent = getIntent();
                            String appLinkAction = appLinkIntent.getAction();
                            Uri appLinkData = appLinkIntent.getData();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), Constants.GENERIC_ERROR_MSG,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}