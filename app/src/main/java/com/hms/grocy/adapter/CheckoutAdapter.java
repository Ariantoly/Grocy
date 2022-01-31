package com.hms.grocy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.R;
import com.hms.grocy.model.CartItem;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private ArrayList<CartItem> carts;
    private Context context;

    public CheckoutAdapter(ArrayList<CartItem> carts) {
        this.carts = carts;
    }

    @NonNull
    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewCheckout = inflater.inflate(R.layout.item_rv_checkout, parent, false);
        CheckoutAdapter.ViewHolder viewHolder = new CheckoutAdapter.ViewHolder(viewCheckout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutAdapter.ViewHolder holder, int position) {
        CartItem cart = carts.get(position);

        TextView tvGroceryName = holder.tvGroceryName;
        tvGroceryName.setText(cart.getGrocery().getName());

        TextView tvGroceryStore = holder.tvGroceryStore;
        tvGroceryStore.setText(cart.getGrocery().getStore().getName());

        TextView tvQuantity = holder.tvQuantity;
        tvQuantity.setText("Qty: " + cart.getQty());

        TextView tvSubtotal = holder.tvSubtotal;
        tvSubtotal.setText("Rp " + (cart.getGrocery().getPrice() * cart.getQty()));

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(carts.get(position).getGrocery().getImage())
                .placeholder(R.color.white)
                .error(R.drawable.unknown)
                .into(holder.imgCheckout);
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGroceryName, tvGroceryStore, tvQuantity, tvSubtotal;
        public ImageView imgCheckout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroceryName = itemView.findViewById(R.id.tv_grocery_name_checkout);
            tvGroceryStore = itemView.findViewById(R.id.tv_store_checkout);
            tvQuantity = itemView.findViewById(R.id.tv_quantity_checkout);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal_checkout);
            imgCheckout = itemView.findViewById(R.id.img_checkout);
        }
    }
}
