package com.valairan.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.valairan.Abstract.Item;
import com.valairan.Abstract.suitcaseForSpinner;
import com.valairan.Fragments.AddBag;
import com.valairan.Fragments.AddItem;
import com.valairan.adapters.InventoryAdapter;
import com.valairan.adapters.SuitcaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    String currentUserUID;
    FirebaseDatabase database;
    DatabaseReference databaseRefRoot;
    DatabaseReference databaseRefBagList;
    DatabaseReference databaseRefInventory;
    DataSnapshot listSnapshotReturn;

    public Spinner bagSelector;
    public RecyclerView inventoryRecyclerView;
    public Button addBagButton;
    public Button removeBagButton;
    public FloatingActionButton addItemButton;
    public ProgressBar progressBar;

    suitcaseForSpinner selectedItem;

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
        if (currentUser != null) {

        }
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
        progressBar = findViewById(R.id.loadingIcon_main);

        hideAll();
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


        addBagButton.setOnClickListener(view -> {
            AddBag fragment = new AddBag();
            fragment.show(getSupportFragmentManager(), "Bag adding fragment");
        });

        removeBagButton.setOnClickListener(view -> {

            suitcaseForSpinner removeThis = (suitcaseForSpinner) bagSelector.getSelectedItem();
            if (removeThis.getName().equals("All items")) {
                Toast.makeText(getApplicationContext(), "Nothing was removed.", Toast.LENGTH_LONG).show();

            } else {
                databaseRefBagList.child(removeThis.getName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getApplicationContext(), "Removed selected item.", Toast.LENGTH_LONG).show();

                    }
                });
            }

        });

        addItemButton.setOnClickListener(view -> {
            AddItem fragment = new AddItem((List) listOfBags);
            fragment.show(getSupportFragmentManager(), "Item adding fragment");
        });


        bagSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (suitcaseForSpinner) bagSelector.getItemAtPosition(i);
                listOfItems.clear();

                if (selectedItem.getName().equals("All")) {
                    for (DataSnapshot ds : listSnapshotReturn.getChildren()) {
                            Item temp = new Item(ds.child("itemName").getValue().toString(),
                                    ds.child("itemQuantity").getValue().toString(),
                                    ds.child("itemLocation").getValue().toString(),
                                    ds.child("itemType").getValue().toString(),
                                    ds.child("notes").getValue().toString());
                            listOfItems.add(temp);

                    }
                } else {
                    for (DataSnapshot ds : listSnapshotReturn.getChildren()) {
                        if (ds.child("itemLocation").getValue().toString().equals(selectedItem.getName())) {
                            Item temp = new Item(ds.child("itemName").getValue().toString(),
                                    ds.child("itemQuantity").getValue().toString(),
                                    ds.child("itemLocation").getValue().toString(),
                                    ds.child("itemType").getValue().toString(),
                                    ds.child("notes").getValue().toString());
                            listOfItems.add(temp);
                        }

                    }
                }

                itemSelectorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.e("", "Nothing Selected");
            }
        });


        inventoryRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        bagSelector.setSelection(0);
        selectedItem = (suitcaseForSpinner) bagSelector.getSelectedItem();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showAll();
                // Actions to do after 10 seconds
            }
        }, 2000);

        databaseRefBagList.addValueEventListener(bagListener);

        databaseRefInventory.addValueEventListener(inventoryListener);

    }

    private void hideAll() {
        bagSelector.setVisibility(View.INVISIBLE);
        inventoryRecyclerView.setVisibility(View.INVISIBLE);
        addBagButton.setVisibility(View.INVISIBLE);
        removeBagButton.setVisibility(View.INVISIBLE);
        addItemButton.setVisibility(View.INVISIBLE);
    }

    private void showAll() {
        progressBar.setVisibility(View.GONE);
        bagSelector.setVisibility(View.VISIBLE);
        inventoryRecyclerView.setVisibility(View.VISIBLE);
        addBagButton.setVisibility(View.VISIBLE);
        removeBagButton.setVisibility(View.VISIBLE);
        addItemButton.setVisibility(View.VISIBLE);
    }


    final ValueEventListener bagListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listOfBags.clear();
            for (DataSnapshot ds : snapshot.getChildren()) {
                String key = ds.getKey();
                suitcaseForSpinner temp = new suitcaseForSpinner(key);
                listOfBags.add(temp);
                //listOfBags.add(key);
            }
            bagSelectorAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), "Something went wrong. Please reload the app.", Toast.LENGTH_LONG).show();
        }
    };

    ValueEventListener inventoryListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listOfItems.clear();
            listSnapshotReturn = snapshot;
            for (DataSnapshot ds : snapshot.getChildren()) {
                String key = ds.getKey();

                if (selectedItem == null || selectedItem.getName().equals("All")) {
                    Item temp = new Item(ds.child("itemName").getValue().toString(),
                            ds.child("itemQuantity").getValue().toString(),
                            ds.child("itemLocation").getValue().toString(),
                            ds.child("itemType").getValue().toString(),
                            ds.child("notes").getValue().toString());

                    listOfItems.add(temp);
                }


            }
            itemSelectorAdapter.notifyDataSetChanged();
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



