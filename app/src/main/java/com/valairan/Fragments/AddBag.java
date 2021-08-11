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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBag extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddBag() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_add_bag, container, false);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBag newInstance(String param1, String param2) {
        AddBag fragment = new AddBag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AddBag currentAddBag= this;

        bName = getView().findViewById(R.id.bagNameEntry);
        bCapacity = view.findViewById(R.id.bagCapacityEntry);
        bInstructions = view.findViewById(R.id.bagInformation);

        updateBagList = view.findViewById(R.id.updateButton);
        cancelFragment = view.findViewById(R.id.cancelButton);

        currentAuth = FirebaseAuth.getInstance();
        currentUser = currentAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();
        database = dbReference.getDatabase();

        currentUserUID = currentUser.getUid().toString();

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