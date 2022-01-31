package com.hms.grocy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.CheckoutActivity;
import com.hms.grocy.MainActivity;
import com.hms.grocy.R;
import com.hms.grocy.adapter.CartAdapter;
import com.hms.grocy.model.Cart;
import com.hms.grocy.model.CartItem;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private GetDataService getDataService;
    private View view;

    private CheckBox chkSelectAll;
    private Button btnCheckout;

    private ArrayList<CartItem> carts = new ArrayList<>();

    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, null);

        chkSelectAll = view.findViewById(R.id.chk_select_all);

        getAllCarts();

        chkSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkSelectAll.isChecked()) {
                    adapter.selectAll();
                }
                else {
                    adapter.unselectAll();
                }
            }
        });

        btnCheckout = view.findViewById(R.id.btn_checkout_cart);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adapter.getCheckedCount() == 0)
                    return;

                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                if(chkSelectAll.isChecked()) {
                    intent.putExtra("carts", carts);
                }
                else {
                    ArrayList<CartItem> selectedCart = new ArrayList<>();
                    for (CartItem c : carts) {
                        if(c.isChecked())
                            selectedCart.add(c);
                    }
                    intent.putExtra("carts", selectedCart);
                }
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllCarts();
        resetCheckAll();
    }

    public void getAllCarts() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        int userId = ((MainActivity) getActivity()).getConsumer().getId();
        Call<Cart> call = getDataService.getAllCarts(userId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                viewAllCarts(response.body().getItems());
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.v("Grocy", t.toString());
            }
        });
    }

    private void viewAllCarts(ArrayList<CartItem> carts) {
        this.carts = carts;
        RecyclerView rvCarts = view.findViewById(R.id.rv_carts);
        adapter = new CartAdapter(carts, chkSelectAll, this);
        rvCarts.setAdapter(adapter);
        rvCarts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void resetCheckAll() {
        chkSelectAll.setChecked(false);
    }

}
