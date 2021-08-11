package com.valairan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.valairan.inventory.R;
import com.valairan.Abstract.suitcaseForSpinner;

import java.util.ArrayList;

public class SuitcaseAdapter extends ArrayAdapter<suitcaseForSpinner> {

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