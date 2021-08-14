package com.valairan.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valairan.inventory.R;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListItem extends Fragment {


    private static final String Name = "Item Name";
    private static final String Quantity = "Quantity";
    private static final String Location = "Location";
    private static final String specialNotes = "Special Notes";

    public ListItem() {
        // Required empty public constructor
    }

    public static ListItem newInstance(String Name, String Quantity, String Location, String SpecialNotes, URI itemImage) {
        ListItem fragment = new ListItem();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_item, container, false);

    }
}