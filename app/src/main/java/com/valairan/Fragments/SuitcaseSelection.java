package com.valairan.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valairan.inventory.R;


public class SuitcaseSelection extends Fragment {

    public TextView suitcaseName;
    public String value;

    public SuitcaseSelection(){

    }

    public SuitcaseSelection(String value) {
        this.value = value;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_suitcase_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        suitcaseName = view.findViewById(R.id.suitcaseFragmentItem);
        suitcaseName.setText(value);

    }


}