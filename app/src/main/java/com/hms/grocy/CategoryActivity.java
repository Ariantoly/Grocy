package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hms.grocy.adapter.GroceryAdapter;
import com.hms.grocy.model.Category;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private TextView tvTitle;

    private int categoryId;

    private GetDataService getDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        categoryId = (int) getIntent().getIntExtra("categoryId", 0);

        getCategory();

    }

    public void doBack(View view) {
        finish();
    }

    private void getCategory() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Category> call = getDataService.getCategory(categoryId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                viewCategory(response.body());
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {

            }
        });
    }

    public void viewCategory(Category category) {
        tvTitle = findViewById(R.id.tv_title_category);
        tvTitle.setText(category.getName());

        RecyclerView rvGroceries = findViewById(R.id.rv_groceries_category);
        GroceryAdapter adapter = new GroceryAdapter(category.getGroceries());
        rvGroceries.setAdapter(adapter);
        rvGroceries.setLayoutManager(new GridLayoutManager(this, 2));
    }
}