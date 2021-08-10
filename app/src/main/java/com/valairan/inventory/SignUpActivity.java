package com.valairan.inventory;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth currentAuth;


    public EditText fNameEntry;
    public EditText lNameEntry;
    public EditText uNameEntry;
    public EditText pWordEntry;
    public TextView SignUpPage;
    public Button signUpButton;

    FirebaseDatabase mDatabase;
    DatabaseReference currentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fNameEntry = findViewById(R.id.fNameEntry);
        lNameEntry = findViewById(R.id.lNameEntry);
        uNameEntry = findViewById(R.id.new_uNameEntry);
        pWordEntry = findViewById(R.id.new_pWordEntry);
        SignUpPage = findViewById(R.id.accountExistsText);
        signUpButton = findViewById(R.id.signUpButton);

        currentAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        currentRef = mDatabase.getReference("Users");


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        SignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    public void attemptSignUp(){
        String email = uNameEntry.getText().toString();
        String pass = pWordEntry.getText().toString();
        if(email.isEmpty() || pass.isEmpty() || fNameEntry.getText().toString().isEmpty()|| lNameEntry.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Fields are empty...", Toast.LENGTH_SHORT).show();
            return;
        }else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                uNameEntry.setError("Please enter a valid email.");
                return;
            }
        }
            Toast.makeText(getApplicationContext(), "Signing you up...", Toast.LENGTH_SHORT).show();
        currentAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = currentAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("userName", fNameEntry.getText().toString());
                            User dbUser = new User(fNameEntry.getText().toString().trim() + " " + lNameEntry.getText().toString().trim(), email);
                            mDatabase.getReference("Users").child(user.getUid()).setValue(dbUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(), "Sign up sucessful.", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Sign up failed.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}