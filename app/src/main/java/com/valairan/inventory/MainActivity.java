package com.valairan.inventory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.valairan.Abstract.Bag;
import com.valairan.Abstract.Item;
import com.valairan.Abstract.suitcaseForSpinner;
import com.valairan.Fragments.AddBag;
import com.valairan.Fragments.AddItem;
import com.valairan.Fragments.bagRemoveConfirm;
import com.valairan.Fragments.moreOptionsMenu;
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
    public FloatingActionButton addBagButton;
    public FloatingActionButton removeBagButton;
    public FloatingActionButton addItemButton;
    public FloatingActionButton bagInfoButton;
    public ProgressBar progressBar;
    public ImageView moreOptionsButton;

    suitcaseForSpinner selectedItem;

    ArrayList<Item> listOfItems;
    InventoryAdapter itemSelectorAdapter;
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

        moreOptionsButton = findViewById(R.id.moreOptions);

        addBagButton = findViewById(R.id.addBagButton);
        removeBagButton = findViewById(R.id.removeBagButton);
        bagInfoButton = findViewById(R.id.bagInfoButton);

        addItemButton = findViewById(R.id.addItemButton);

        inventoryRecyclerView = findViewById(R.id.listOfItems);
        progressBar = findViewById(R.id.loadingIcon_main);

        hideAll();

        moreOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreOptionsMenu moreOptionsFragment = new moreOptionsMenu();
                moreOptionsFragment.show(getSupportFragmentManager(), "More options menu");
            }
        });


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

        itemSelectorAdapter.setOnItemClickListener(new InventoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String name = listOfItems.get(position).getItemName();
                String type = listOfItems.get(position).getItemType();
                String count = listOfItems.get(position).getItemQuantity();
                String notes = listOfItems.get(position).getNotes();

                AddItem editableFragment = new AddItem(name, type, count, notes, listOfBags);
                editableFragment.show(getSupportFragmentManager(), "Editing Item");
            }
        });


        addBagButton.setOnClickListener(view -> {
            AddBag fragment = new AddBag();
            fragment.show(getSupportFragmentManager(), "Bag adding fragment");
        });

        removeBagButton.setOnClickListener(view -> {

            suitcaseForSpinner removeThis = (suitcaseForSpinner) bagSelector.getSelectedItem();
            if (removeThis.getName().equals("All items")) {
                Toast.makeText(getApplicationContext(), Constants.NO_CHANGE, Toast.LENGTH_SHORT).show();

            } else {
                bagRemoveConfirm fragment = new bagRemoveConfirm(removeThis.getName());
                fragment.show(getSupportFragmentManager(), "Confirm bag removal dialog");

            }

        });

        bagInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suitcaseForSpinner describeThis = (suitcaseForSpinner) bagSelector.getSelectedItem();
                databaseRefBagList.child(describeThis.getName().toString()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("fullName").getValue().toString();
                        String capacity = dataSnapshot.child("capacity").getValue().toString();
                        String notes = dataSnapshot.child("specialNotes").getValue().toString();
                        Bag temp = new Bag(name, capacity, notes);
                        AddBag addBag = new AddBag(temp);
                        addBag.show(getSupportFragmentManager(), "Bag editing fragment");
                    }
                });


            }
        });


        addItemButton.setOnClickListener(view -> {
            Query query = database.getReference().child("Users").child(currentUserUID).child("ListOfBags");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount() > 1){
                        AddItem fragment = new AddItem(listOfBags);
                        fragment.show(getSupportFragmentManager(), "Item adding fragment");
                    }else {
                        Toast.makeText(getApplicationContext(), Constants.ADD_BAG, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });


        bagSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (suitcaseForSpinner) bagSelector.getItemAtPosition(i);
                listOfItems.clear();

                if (selectedItem.getName().equals("All items")) {
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
        moreOptionsButton.setVisibility(View.INVISIBLE);
        bagSelector.setVisibility(View.INVISIBLE);
        bagInfoButton.setVisibility(View.INVISIBLE);
        inventoryRecyclerView.setVisibility(View.INVISIBLE);
        addBagButton.setVisibility(View.INVISIBLE);
        removeBagButton.setVisibility(View.INVISIBLE);
        addItemButton.setVisibility(View.INVISIBLE);
    }

    private void showAll() {
        moreOptionsButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        bagSelector.setVisibility(View.VISIBLE);
        bagInfoButton.setVisibility(View.VISIBLE);
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
            Toast.makeText(getApplicationContext(), Constants.GENERIC_ERROR_MSG, Toast.LENGTH_SHORT).show();
        }
    };

    ValueEventListener inventoryListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listOfItems.clear();
            listSnapshotReturn = snapshot;
            for (DataSnapshot ds : snapshot.getChildren()) {
                String key = ds.getKey();

                if (selectedItem == null || selectedItem.getName().equals("All items")) {
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
            Toast.makeText(getApplicationContext(), Constants.GENERIC_ERROR_MSG, Toast.LENGTH_SHORT).show();

        }
    };


    @Override
    public void onStart() {
        super.onStart();

    }

}



