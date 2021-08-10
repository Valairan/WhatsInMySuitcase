package com.valairan.inventory;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    String currentUserUID;

    public Spinner bagSelector;
    public ListView listOfItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUID = currentUser.getUid();

        bagSelector = findViewById(R.id.bagSelector);
        listOfItems = findViewById(R.id.listOfItems);

    }
    @Override
    public void onStart() {
        super.onStart();

    }


    public void attemptLogin(){

    }
}