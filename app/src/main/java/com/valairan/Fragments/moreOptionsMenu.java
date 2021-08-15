package com.valairan.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.valairan.inventory.LoginActivity;
import com.valairan.inventory.R;


public class moreOptionsMenu extends DialogFragment {


    public moreOptionsMenu() {
        // Required empty public constructor
    }

    public Button logoutButton;
    public Button resetPasswordButton;


    public static moreOptionsMenu newInstance() {
        moreOptionsMenu fragment = new moreOptionsMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_options_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logoutButton = view.findViewById(R.id.signOutButton);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);

        moreOptionsMenu fragment = this;

        logoutButton.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent signoutIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(signoutIntent);
        });

        resetPasswordButton.setOnClickListener(view12 -> FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(getContext(), "A password reset email has been sent to you", Toast.LENGTH_SHORT).show();
                    fragment.dismiss();
                } else {
                    Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }));


    }
}