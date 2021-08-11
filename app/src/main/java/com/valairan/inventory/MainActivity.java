package com.valairan.inventory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    String currentUserUID;
    FirebaseDatabase database;
    DatabaseReference databaseRefRoot;
    DatabaseReference databaseRefChild;


    public Spinner bagSelector;
    public RecyclerView listOfItems;
    public Button addBagButton;
    public Button removeBagButton;

    ArrayList<suitcaseForSpinner> listOfBags;
    SuitcaseAdapter bagSelectorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserUID = currentUser.getUid();
        database = FirebaseDatabase.getInstance();
        databaseRefRoot = database.getReference("Users");
        databaseRefChild = databaseRefRoot.child(currentUserUID).child("ListOfBags");

        listOfBags = new ArrayList<>();
        bagSelector = findViewById(R.id.bagSelector);
        listOfItems = findViewById(R.id.listOfItems);
        addBagButton = findViewById(R.id.addBagButton);
        removeBagButton = findViewById(R.id.removeBagButton);

        bagSelectorList = new SuitcaseAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listOfBags);
        bagSelector.setAdapter(bagSelectorList);
        bagSelectorList.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        bagSelectorList.notifyDataSetChanged();


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
                Toast.makeText(getApplicationContext(), " " + bagSelector.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                suitcaseForSpinner removeThis = (suitcaseForSpinner) bagSelector.getSelectedItem();
                databaseRefChild.child(removeThis.getName()).removeValue();
            }
        });


        databaseRefChild.addListenerForSingleValueEvent(bagListener);
    }

    final ValueEventListener bagListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                String key = ds.getKey();
                suitcaseForSpinner temp = new suitcaseForSpinner(key);
                listOfBags.add(temp);
                //listOfBags.add(key);
                Log.e("Key", key);
            }
            bagSelectorList.notifyDataSetChanged();
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

class SuitcaseAdapter extends ArrayAdapter<suitcaseForSpinner> {

    public SuitcaseAdapter(Context context, int support_simple_spinner_dropdown_item, ArrayList<suitcaseForSpinner> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.fragment_suitcase_selection, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.suitcaseFragmentItem);
        suitcaseForSpinner currentItem = getItem(position);
        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }

        return convertView;
    }
}

class suitcaseForSpinner {
    public String suitcaseString;

    public suitcaseForSpinner(String suitcaseNString) {
        suitcaseString = suitcaseNString;
    }

    public String getName() {
        return suitcaseString;
    }
}