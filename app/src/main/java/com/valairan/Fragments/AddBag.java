package com.valairan.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valairan.Abstract.Bag;
import com.valairan.inventory.R;


public class AddBag extends DialogFragment {


    public AddBag() {
        // Required empty public constructor
    }

    public Bag localBag;

    public AddBag(Bag bag) {
        localBag = bag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_add_bag, container, false);
    }


    String bagName;
    String bagCapacity;
    String SpecialInstructions;
    String currentUserUID;

    EditText bName;
    EditText bCapacity;
    EditText bInstructions;

    Button updateBagList;
    Button cancelFragment;

    FirebaseUser currentUser;
    FirebaseAuth currentAuth;
    FirebaseDatabase database;
    DatabaseReference dbReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AddBag currentAddBag= this;

        bName = getView().findViewById(R.id.bagNameEntry);
        bCapacity = view.findViewById(R.id.bagCapacityEntry);
        bInstructions = view.findViewById(R.id.specialNotesEntryAddBag);

        updateBagList = view.findViewById(R.id.updateButtonAddBag);
        cancelFragment = view.findViewById(R.id.cancelButtonAddBag);

        currentAuth = FirebaseAuth.getInstance();
        currentUser = currentAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();
        database = dbReference.getDatabase();

        currentUserUID = currentUser.getUid().toString();

        if(localBag != null){
            bName.setText(localBag.fullName);
            bCapacity.setText(localBag.capacity);
            bInstructions.setText(localBag.specialNotes);

        }


        updateBagList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Adding bag.", Toast.LENGTH_LONG).show();

                bagName = bName.getText().toString().trim();
                bagCapacity = bCapacity.getText().toString().trim();
                SpecialInstructions = bInstructions.getText().toString().trim();

                Bag bag = new Bag(bagName, bagCapacity, SpecialInstructions);
                database.getReference("Users").child(currentUserUID).child("ListOfBags").child(bagName).setValue(bag).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Bag added.", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    cancelFragment.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentAddBag.dismiss();
        }
    });

    }


}