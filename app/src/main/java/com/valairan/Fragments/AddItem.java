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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valairan.Abstract.Item;
import com.valairan.Abstract.suitcaseForSpinner;
import com.valairan.adapters.SuitcaseAdapter;
import com.valairan.inventory.Constants;
import com.valairan.inventory.R;

import java.util.ArrayList;
import java.util.List;

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

    public FloatingActionButton cancelItemButton;
    public FloatingActionButton updateItemButton;
    public FloatingActionButton deleteItemButton;
    String name, type, location, count, notes;

    public List<suitcaseForSpinner> localList;


    public AddItem(String Name, String Type, String Quantity, String Notes, List<suitcaseForSpinner> listOfBags) {
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

        cancelItemButton = view.findViewById(R.id.dismissItemMenuButton);
        deleteItemButton = view.findViewById(R.id.deleteItemButton);
        updateItemButton = view.findViewById(R.id.updateItemButton);
        deleteItemButton.setVisibility(View.GONE);

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

        if (!(name == null) && !(count == null) && !(type == null) && !(notes == null)) {
            itemName.setText(name);
            itemCount.setText(count);
            itemType.setText(type);
            itemNotes.setText(notes);
            deleteItemButton.setVisibility(View.VISIBLE);
        }

        AddItem fragment = this;

        cancelItemButton.setOnClickListener(view1 -> fragment.dismiss());

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRefInventory.child(name).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            fragment.dismiss();
                            Toast.makeText(getContext(), Constants.REMOVED_ITEM, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), Constants.GENERIC_ERROR_MSG, Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemNameString = itemName.getText().toString().trim();
                String itemCountString = itemCount.getText().toString().trim();
                String itemTypeString = itemType.getText().toString().trim();
                suitcaseForSpinner locationTemp = (suitcaseForSpinner) itemLocationSpinner.getSelectedItem();
                String itemLocationString = locationTemp.getName();
                String itemNotesString = itemNotes.getText().toString().trim();

                if (itemNameString.isEmpty() || itemCountString.isEmpty() || itemLocationString.isEmpty()) {
                    Toast.makeText(getContext(), Constants.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();
                } else {
                    if (itemNotesString.isEmpty()) {
                        itemNotesString = "-";
                    }
                    Item temp = new Item(itemNameString, itemCountString, itemLocationString, itemTypeString, itemNotesString);
                    databaseRefInventory.child(itemNameString).setValue(temp);

                    Toast.makeText(getContext(), Constants.ADDED_ITEM, Toast.LENGTH_SHORT).show();

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