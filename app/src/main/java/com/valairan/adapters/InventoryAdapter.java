package com.valairan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valairan.Abstract.Item;
import com.valairan.inventory.R;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private ArrayList<Item> mItemList;

    public static class InventoryViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName_viewHolder;
        public TextView itemQuantity_viewHolder;
        public TextView itemLocation_viewHolder;
        public TextView itemType_viewHolder;
        public TextView notes_viewHolder;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName_viewHolder = itemView.findViewById(R.id.itemTitle);
            itemQuantity_viewHolder = itemView.findViewById(R.id.itemCount);
            itemLocation_viewHolder = itemView.findViewById(R.id.itemLocation);
            itemType_viewHolder = itemView.findViewById(R.id.itemType);
            notes_viewHolder = itemView.findViewById(R.id.SpecialNotes);

        }
    }

    public InventoryAdapter(ArrayList<Item> inventoryList) {
        mItemList = inventoryList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item, parent, false);
        InventoryViewHolder invHolder = new InventoryViewHolder(v);
        return invHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        Item currentItem = mItemList.get(position);

        holder.itemName_viewHolder.setText(currentItem.getItemName());
        holder.itemQuantity_viewHolder.setText(currentItem.getItemQuantity());
        holder.itemType_viewHolder.setText(currentItem.getItemType());
        holder.itemLocation_viewHolder.setText(currentItem.getItemLocation());
        holder.notes_viewHolder.setText(currentItem.getNotes());

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
