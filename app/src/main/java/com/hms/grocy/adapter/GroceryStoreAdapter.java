package com.hms.grocy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.GroceryStoreActivity;
import com.hms.grocy.R;
import com.hms.grocy.model.GroceryStore;

import java.util.ArrayList;

public class GroceryStoreAdapter extends RecyclerView.Adapter<GroceryStoreAdapter.ViewHolder> {

    private ArrayList<GroceryStore> groceryStores;
    private Context context;

    public GroceryStoreAdapter(ArrayList<GroceryStore> groceryStores) {
        this.groceryStores = groceryStores;
    }

    @NonNull
    @Override
    public GroceryStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewGroceryStore = inflater.inflate(R.layout.item_rv_stores, parent, false);
        GroceryStoreAdapter.ViewHolder viewHolder = new GroceryStoreAdapter.ViewHolder(viewGroceryStore);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryStoreAdapter.ViewHolder holder, int position) {
        GroceryStore groceryStore = groceryStores.get(position);

        TextView tvStoreName = holder.tvStoreName;
        tvStoreName.setText(groceryStore.getName());

        TextView tvStoreCity = holder.tvStoreCity;
        tvStoreCity.setText(groceryStore.getCity());

        TextView tvGroceryCount = holder.tvGroceryCount;
        tvGroceryCount.setText(groceryStore.getGroceryCount() + " groceries");

        CardView cvStores = holder.cvStores;
        cvStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroceryStoreActivity.class);
                intent.putExtra("storeId", groceryStore.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groceryStores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvStoreName, tvStoreCity, tvGroceryCount;
        public CardView cvStores;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tv_store_name);
            tvStoreCity = itemView.findViewById(R.id.tv_store_city);
            tvGroceryCount = itemView.findViewById(R.id.tv_grocery_count);
            cvStores = itemView.findViewById(R.id.cv_stores);
        }
    }
}
