package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hms.grocy.adapter.GroceryAdapter;
import com.hms.grocy.model.Grocery;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceriesActivity extends AppCompatActivity {

    private GetDataService getDataService;

    private TextView tvNoProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries);

        tvNoProduct = findViewById(R.id.tv_no_product);
        tvNoProduct.setVisibility(View.GONE);

        String keyword = getIntent().getStringExtra("keyword");

        if(keyword.trim().equals("")) {
            tvNoProduct.setVisibility(View.VISIBLE);
        }
        else {
            getGroceriesByKeyword(keyword);
        }

    }

    private void getGroceriesByKeyword(String keyword) {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ArrayList<Grocery>> call = getDataService.searchGrocery(keyword);
        call.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                if(response.body().isEmpty()) {
                    tvNoProduct.setVisibility(View.VISIBLE);
                    return;
                }

                viewGroceries(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

            }
        });
    }

    private void viewGroceries(ArrayList<Grocery> groceries) {
        RecyclerView rvGroceries = findViewById(R.id.rv_groceries);
        GroceryAdapter adapter = new GroceryAdapter(groceries);
        rvGroceries.setAdapter(adapter);
        rvGroceries.setLayoutManager(new GridLayoutManager(this, 2));
    }

    public void doBack(View view) {
        finish();
    }
}