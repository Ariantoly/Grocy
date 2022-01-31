package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hms.grocy.adapter.GroceryAdapter;
import com.hms.grocy.model.GroceryStore;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceryStoreActivity extends AppCompatActivity {

    private TextView tvStoreName, tvStoreCity;

    private int storeId;

    private GetDataService getDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_store);

        tvStoreName = findViewById(R.id.tv_store_name);
        tvStoreCity = findViewById(R.id.tv_store_city);

        storeId = (int) getIntent().getIntExtra("storeId", 0);

        getStore();
    }

    public void doBack(View view) {
        finish();
    }

    private void getStore() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<GroceryStore> call = getDataService.getGroceryStore(storeId);
        call.enqueue(new Callback<GroceryStore>() {
            @Override
            public void onResponse(Call<GroceryStore> call, Response<GroceryStore> response) {
                viewStore(response.body());
            }

            @Override
            public void onFailure(Call<GroceryStore> call, Throwable t) {

            }
        });
    }

    private void viewStore(GroceryStore store) {
        tvStoreName.setText(store.getName());
        tvStoreCity.setText(store.getCity());

        RecyclerView rvGroceries = findViewById(R.id.rv_groceries_grocery_store);
        GroceryAdapter adapter = new GroceryAdapter(store.getGroceries());
        rvGroceries.setAdapter(adapter);
        rvGroceries.setLayoutManager(new GridLayoutManager(this, 2));
    }
}