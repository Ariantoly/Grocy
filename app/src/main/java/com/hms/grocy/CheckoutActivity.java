package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hms.grocy.adapter.CartAdapter;
import com.hms.grocy.adapter.CheckoutAdapter;
import com.hms.grocy.fragment.CartFragment;
import com.hms.grocy.model.CartItem;
import com.hms.grocy.network.PostDataService;
import com.hms.grocy.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private Spinner spinnerDelivery;
    private EditText etAddress;
    private TextView tvTotalPrice, tvDeliveryFee, tvGrandTotal, tvBigGrandTotal, tvAddress;

    private ArrayList<CartItem> carts;

    private PostDataService postDataService;

    private int total = 0, deliveryFee = 0, grandTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        spinnerDelivery = findViewById(R.id.spinner_delivery);
        tvAddress = findViewById(R.id.tv_address);
        etAddress = findViewById(R.id.et_address);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvDeliveryFee = findViewById(R.id.tv_delivery_fee);
        tvGrandTotal = findViewById(R.id.tv_grand_total);
        tvBigGrandTotal = findViewById(R.id.tv_big_grand_total);

        carts = (ArrayList<CartItem>) getIntent().getSerializableExtra("carts");
        viewCarts();

        for(CartItem cart : carts) {
            total += (cart.getQty() * cart.getGrocery().getPrice());
        }
        tvTotalPrice.setText("Rp " + total);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.delivery_type, R.layout.item_spinner_delivery);
        adapter.setDropDownViewResource(R.layout.item_spinner_delivery);
        spinnerDelivery.setAdapter(adapter);
        spinnerDelivery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().equals("Delivery")) {
                    deliveryFee = 1000;
                    tvDeliveryFee.setText("Rp " + deliveryFee);
                    grandTotal = total - deliveryFee;
                    tvGrandTotal.setText("Rp " + grandTotal);
                    tvBigGrandTotal.setText("Rp " + grandTotal);
                    tvAddress.setText("Address*");
                    etAddress.setEnabled(true);
                }
                else {
                    deliveryFee = 0;
                    tvDeliveryFee.setText("Rp " + deliveryFee);
                    grandTotal = total - deliveryFee;
                    tvGrandTotal.setText("Rp " + grandTotal);
                    tvBigGrandTotal.setText("Rp " + grandTotal);
                    tvAddress.setText("Address");
                    etAddress.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void viewCarts() {
        RecyclerView rvCheckout = findViewById(R.id.rv_checkout);
        CheckoutAdapter adapter = new CheckoutAdapter(carts);
        rvCheckout.setAdapter(adapter);
        rvCheckout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    public void doBack(View view) {
        finish();
    }

    public void checkout(View view) {
        boolean isValid = (!etAddress.getText().toString().trim().equals("") && spinnerDelivery.getSelectedItem().equals("Delivery"))
                || spinnerDelivery.getSelectedItem().equals("Take Away");
        if(isValid) {
            postDataService = RetrofitClientInstance.getRetrofitInstance().create(PostDataService.class);
            int userId = MainActivity.getConsumer().getId();
            Call<Void> call = postDataService.checkout(userId, carts, spinnerDelivery.getSelectedItem().toString());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    successMessage();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

        }

    }

    public void successMessage() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        Toast.makeText(this, "Checkout successful", Toast.LENGTH_LONG).show();
        finish();
    }
}