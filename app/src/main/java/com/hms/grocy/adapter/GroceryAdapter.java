package com.hms.grocy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.GroceryDetailActivity;
import com.hms.grocy.R;
import com.hms.grocy.model.Grocery;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.ViewHolder> {

    private List<Grocery> groceries;
    private Context context;

    public GroceryAdapter(List<Grocery> groceries) {
        this.groceries = groceries;
    }

    @NonNull
    @Override
    public GroceryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewGrocery = inflater.inflate(R.layout.item_rv_groceries, parent, false);
        GroceryAdapter.ViewHolder viewHolder = new GroceryAdapter.ViewHolder(viewGrocery);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceries.get(position);

        TextView tvGroceryName = holder.tvGroceryName;
        tvGroceryName.setText(grocery.getName());

        TextView tvGroceryStoreName = holder.tvGroceryStoreName;
        tvGroceryStoreName.setText(grocery.getStore().getName());

        TextView tvGroceryPrice = holder.tvGroceryPrice;
        tvGroceryPrice.setText("Rp " + grocery.getPrice());

        TextView tvGrocerySold = holder.tvGrocerySold;
        tvGrocerySold.setText("Sold " + grocery.getSold());

        ImageView imgGrocery = holder.imgGrocery;
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(groceries.get(position).getImage())
                .placeholder(R.color.white)
                .error(R.drawable.unknown)
                .into(imgGrocery);

        CardView cvGroceries = holder.cvGroceries;
        cvGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GroceryDetailActivity.class);
                intent.putExtra("groceryId", grocery.getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGroceryName, tvGroceryStoreName, tvGroceryPrice, tvGrocerySold;
        public ImageView imgGrocery;
        public CardView cvGroceries;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroceryName = itemView.findViewById(R.id.tv_grocery_name);
            tvGroceryStoreName = itemView.findViewById(R.id.tv_grocery_store_name);
            tvGroceryPrice = itemView.findViewById(R.id.tv_grocery_price);
            tvGrocerySold = itemView.findViewById(R.id.tv_grocery_sold);
            imgGrocery = itemView.findViewById(R.id.img_grocery);
            cvGroceries = itemView.findViewById(R.id.cv_groceries);
        }
    }
}
