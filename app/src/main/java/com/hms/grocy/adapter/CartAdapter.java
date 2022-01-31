package com.hms.grocy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.MainActivity;
import com.hms.grocy.R;
import com.hms.grocy.fragment.CartFragment;
import com.hms.grocy.model.CartItem;
import com.hms.grocy.network.PostDataService;
import com.hms.grocy.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private ArrayList<CartItem> carts;
    private Context context;
    private boolean isSelectAll;
    private int checkedCount;

    private CheckBox chkSelectAll;
    private CartFragment cartFragment;

    private PostDataService postDataService;

    public CartAdapter(ArrayList<CartItem> carts, CheckBox chkSelectAll, CartFragment cartFragment) {
        this.carts = carts;
        this.chkSelectAll = chkSelectAll;
        this.cartFragment = cartFragment;
        checkedCount = 0;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewCart = inflater.inflate(R.layout.item_rv_carts, parent, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(viewCart);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem cart = carts.get(position);

        TextView tvCartName = holder.tvCartName;
        tvCartName.setText(cart.getGrocery().getName());

        TextView tvCartStore = holder.tvCartStore;
        tvCartStore.setText(cart.getGrocery().getStore().getName());

        TextView tvCartQuantity = holder.tvCartQuantity;
        tvCartQuantity.setText("Qty: " + cart.getQty());

        TextView tvSubtotal = holder.tvSubtotal;
        tvSubtotal.setText("Rp " + (cart.getGrocery().getPrice() * cart.getQty()));

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(carts.get(position).getGrocery().getImage())
                .placeholder(R.color.white)
                .error(R.drawable.unknown)
                .into(holder.imgCart);

        ImageButton btnDelete = holder.btnDelete;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataService = RetrofitClientInstance.getRetrofitInstance().create(PostDataService.class);
                int userId = MainActivity.getConsumer().getId();
                int groceryId = cart.getGrocery().getId();
                Call<Void> call = postDataService.deleteCart(userId, groceryId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context, "Delete Successful", Toast.LENGTH_LONG).show();
                        cartFragment.onResume();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.v("Grocy", "Delete Failed: " + t);
                    }
                });

            }
        });

        CheckBox chkCart = holder.chkCart;
        if(isSelectAll) {
            chkCart.setChecked(true);
        }
        else {
            chkCart.setChecked(false);
        }

        chkCart.setOnCheckedChangeListener(null);
        chkCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cart.setChecked(isChecked);
                if(isChecked) {
                    checkedCount++;
                    if(checkedCount == carts.size())
                        chkSelectAll.setChecked(true);
                }
                else {
                    isSelectAll = false;
                    chkSelectAll.setChecked(false);
                    checkedCount--;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public void selectAll() {
        isSelectAll = true;
        notifyDataSetChanged();
    }

    public void unselectAll() {
        isSelectAll = false;
        notifyDataSetChanged();
    }

    public int getCheckedCount() {
        return checkedCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCartName, tvCartStore, tvCartQuantity, tvSubtotal;
        public ImageView imgCart;
        public CheckBox chkCart;
        public ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCartName = itemView.findViewById(R.id.tv_cart_name);
            tvCartStore = itemView.findViewById(R.id.tv_cart_store);
            tvCartQuantity = itemView.findViewById(R.id.tv_cart_quantity);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal);
            imgCart = itemView.findViewById(R.id.img_cart);
            chkCart = itemView.findViewById(R.id.chk_cart);
            btnDelete = itemView.findViewById(R.id.btn_delete_cart);
        }
    }
}
