package com.valairan.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valairan.Abstract.Item;
import com.valairan.Abstract.suitcaseForSpinner;
import com.valairan.Fragments.AddBag;
import com.valairan.adapters.InventoryAdapter;
import com.valairan.adapters.SuitcaseAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    String currentUserUID;
    FirebaseDatabase database;
    DatabaseReference databaseRefRoot;
    DatabaseReference databaseRefBagList;
    DatabaseReference databaseRefInventory;


    public Spinner bagSelector;
    public RecyclerView inventoryRecyclerView;
    public Button addBagButton;
    public Button removeBagButton;
    public Button addItemButton;


    ArrayList<Item> listOfItems;
    RecyclerView.Adapter itemSelectorAdapter;
    RecyclerView.LayoutManager itemSelectorLayoutManager;

    ArrayList<suitcaseForSpinner> listOfBags;
    SuitcaseAdapter bagSelectorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUID = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        databaseRefRoot = database.getReference("Users");
        databaseRefBagList = databaseRefRoot.child(currentUserUID).child("ListOfBags");
        databaseRefInventory = databaseRefRoot.child(currentUserUID).child("Inventory");

        bagSelector = findViewById(R.id.bagSelector);

        addBagButton = findViewById(R.id.addBagButton);
        removeBagButton = findViewById(R.id.removeBagButton);

        addItemButton = findViewById(R.id.addItemButton);

        inventoryRecyclerView = findViewById(R.id.listOfItems);


        listOfBags = new ArrayList<>();
        bagSelectorAdapter = new SuitcaseAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listOfBags);
        bagSelector.setAdapter(bagSelectorAdapter);
        bagSelectorAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bagSelectorAdapter.notifyDataSetChanged();


        listOfItems = new ArrayList<>();
        inventoryRecyclerView.setHasFixedSize(true);
        itemSelectorLayoutManager = new LinearLayoutManager(this);
        itemSelectorAdapter = new InventoryAdapter(listOfItems);
        inventoryRecyclerView.setLayoutManager(itemSelectorLayoutManager);
        inventoryRecyclerView.setAdapter(itemSelectorAdapter);

        addBagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBag fragment = new AddBag();
                fragment.show(getSupportFragmentManager(), "Bag adding fragment");
            }
        });

        removeBagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                suitcaseForSpinner removeThis = (suitcaseForSpinner) bagSelector.getSelectedItem();
                databaseRefBagList.child(removeThis.getName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Removed selected item.", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });


        databaseRefBagList.addValueEventListener(bagListener);
        databaseRefInventory.addValueEventListener(inventoryListener);
    }

    final ValueEventListener inventoryListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listOfItems.clear();
            for (DataSnapshot ds : snapshot.getChildren()) {
                String key = ds.getKey();
                Item temp = new Item(ds.child("itemName").getValue().toString(),
                        ds.child("count").getValue().toString(),
                        ds.child("location").getValue().toString(),
                        ds.child("type").getValue().toString(),
                        ds.child("notes").getValue().toString());

                listOfItems.add(temp);
                //listOfBags.add(key);
                Log.e("Key", key);
            }
            itemSelectorAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reload the app.", Toast.LENGTH_LONG).show();

        }
    };

    final ValueEventListener bagListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listOfBags.clear();
            for (DataSnapshot ds : snapshot.getChildren()) {
                String key = ds.getKey();
                suitcaseForSpinner temp = new suitcaseForSpinner(key);
                listOfBags.add(temp);
                //listOfBags.add(key);
                Log.e("Key", key);
            }
            bagSelectorAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reload the app.", Toast.LENGTH_LONG).show();
        }
    };


    @Override
    public void onStart() {
        super.onStart();

    }

}



