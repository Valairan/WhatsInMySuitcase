package com.valairan.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valairan.Abstract.Item;
import com.valairan.Abstract.suitcaseForSpinner;
import com.valairan.adapters.SuitcaseAdapter;
import com.valairan.inventory.R;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class AddItem extends DialogFragment {


    private EditText itemName;
    private EditText itemCount;
    private EditText itemType;
    private EditText itemNotes;
    private Spinner itemLocationSpinner;
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
    String name, type, count, notes;

    public List<suitcaseForSpinner> localList;


    public AddItem( String Name, String Type, String Quantity, String Notes,List<suitcaseForSpinner> listOfBags) {
        name = Name;
        type = Type;
        count = Quantity;
        notes = Notes;
        localList = listOfBags;

    }
    public AddItem(List<suitcaseForSpinner> listOfBags) {
        this.localList = listOfBags;

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
        itemLocationSpinner = view.findViewById(R.id.itemLocationSpinner);
        itemNotes = view.findViewById(R.id.specialNotesEntryAddItem);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUID = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        databaseRefRoot = database.getReference("Users");
        databaseRefInventory = databaseRefRoot.child(currentUserUID).child("Inventory");

        List<suitcaseForSpinner> swapList = new ArrayList<suitcaseForSpinner>(localList);
        swapList.remove(0);

        SuitcaseAdapter localAdapter = new SuitcaseAdapter(this.getContext(), R.layout.support_simple_spinner_dropdown_item, (ArrayList<suitcaseForSpinner>) swapList);
        itemLocationSpinner.setAdapter(localAdapter);
        localAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        localAdapter.notifyDataSetChanged();

        if(!(name == null) && !(count == null) && !(type == null) && !(notes == null)){
            itemName.setText(name);
            itemCount.setText(count);
            itemType.setText(type);
            itemNotes.setText(notes);
        }

        AddItem fragment = this;

        cancelButton.setOnClickListener(view1 -> fragment.dismiss());

        updateButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Adding item to bag.", Toast.LENGTH_SHORT).show();

                String itemNameString = itemName.getText().toString().trim();
                String itemCountString = itemCount.getText().toString().trim();
                String itemTypeString = itemType.getText().toString().trim();
                suitcaseForSpinner locationTemp = (suitcaseForSpinner) itemLocationSpinner.getSelectedItem();
                String itemLocationString = locationTemp.getName();
                String itemNotesString = itemNotes.getText().toString().trim();

                if(itemNameString.isEmpty() || itemCountString.isEmpty() || itemLocationString.isEmpty()){
                    Toast.makeText(getContext(), "Fields are empty.", Toast.LENGTH_SHORT).show();
                }else {
                    if(itemNotesString.isEmpty()){
                        itemNotesString = "-";
                    }
                    Item temp = new Item(itemNameString, itemCountString, itemLocationString, itemTypeString, itemNotesString);
                    databaseRefInventory.child(itemNameString).setValue(temp);

                    Toast.makeText(getContext(), "Item added to bag.", Toast.LENGTH_SHORT).show();

                }

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