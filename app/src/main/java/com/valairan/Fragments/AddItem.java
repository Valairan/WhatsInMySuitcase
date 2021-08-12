package com.valairan.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valairan.Abstract.Item;
import com.valairan.inventory.MainActivity;
import com.valairan.inventory.R;


public class AddItem extends DialogFragment {


    private EditText itemName;
    private EditText itemCount;
    private EditText itemType;
    private EditText itemLocation;
    private EditText itemNotes;

    FirebaseUser currentUser;
    String currentUserUID;
    FirebaseDatabase database;
    DatabaseReference databaseRefRoot;
    DatabaseReference databaseRefInventory;

    public AddItem() {
        // Required empty public constructor
    }

    public Button cancelButton;
    public Button updateButon;

    // TODO: Rename and change types and number of parameters
    public static AddItem newInstance(String param1, String param2) {
        AddItem fragment = new AddItem();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelButton = view.findViewById(R.id.cancelButtonAddItem);
        updateButon = view.findViewById(R.id.updateButtonAddItem);

        itemName = view.findViewById(R.id.itemNameEntry);
        itemCount = view.findViewById(R.id.itemQuantityEntry);
        itemType = view.findViewById(R.id.itemTypeEntry);
        itemLocation = view.findViewById(R.id.itemLocationEntry);
        itemNotes = view.findViewById(R.id.specialNotesEntryAddItem);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUID = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        databaseRefRoot = database.getReference("Users");
        databaseRefInventory = databaseRefRoot.child(currentUserUID).child("Inventory");

        AddItem fragment = this;

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment.dismiss();
            }
        });

        updateButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Adding item to bag.", Toast.LENGTH_SHORT).show();

                String itemNameString = itemName.getText().toString().trim();
                String itemCountString = itemCount.getText().toString().trim();
                String itemTypeString = itemType.getText().toString().trim();
                String itemLocationString = itemLocation.getText().toString().trim();
                String itemNotesString = itemNotes.getText().toString().trim();

                Item temp = new Item(itemNameString, itemCountString, itemLocationString, itemTypeString, itemNotesString);
                databaseRefInventory.child(itemNameString).setValue(temp);

                Toast.makeText(getContext(), "Item added to bag.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }
}