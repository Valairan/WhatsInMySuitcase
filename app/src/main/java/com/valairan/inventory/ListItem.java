package com.valairan.inventory;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Name = "Item Name";
    private static final String Quantity = "Quantity";
    private static final String Location = "Location";
    private static final String specialNotes = "Special Notes";

    public ListItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ListItem.
     */
    // TODO: Rename and change types and number of parameters
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