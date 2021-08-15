package com.valairan.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valairan.inventory.Constants;
import com.valairan.inventory.R;

public class bagRemoveConfirm extends DialogFragment {


    String name;
    String currentUserUID;


    public Button cancel;
    public Button confirm;

    FirebaseUser currentUser;
    FirebaseAuth currentAuth;
    FirebaseDatabase database;
    DatabaseReference databaseRefRoot;
    DatabaseReference databaseRefBagList;

    public bagRemoveConfirm() {
        // Required empty public constructor
    }

    public bagRemoveConfirm(String name) {
        this.name = name;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        currentAuth = FirebaseAuth.getInstance();
        currentUser = currentAuth.getCurrentUser();
        currentUserUID = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        databaseRefRoot = database.getReference("Users");
        databaseRefBagList = databaseRefRoot.child(currentUserUID).child("ListOfBags");

        return inflater.inflate(R.layout.fragment_bag_remove_confirm, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancel = view.findViewById(R.id.cancelRemoveBag);
        confirm = view.findViewById(R.id.confirmRemoveBag);
        bagRemoveConfirm fragment = this;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query queryBag = databaseRefRoot.child(currentUserUID).child("ListOfBags").orderByChild("fullName").equalTo(name);
                queryBag.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            snapshot.child(name).getRef().removeValue();
                            Toast.makeText(getContext(), Constants.REMOVED_BAG, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Query queryItems = databaseRefRoot.child(currentUserUID).child("Inventory").orderByChild("itemLocation").equalTo(name);

                queryItems.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                ds.getRef().removeValue();

                            }
                        }
                        fragment.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });


            }
        });
    }
}