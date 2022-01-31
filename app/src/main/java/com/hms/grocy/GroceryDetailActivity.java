package com.hms.grocy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hms.grocy.model.Cart;
import com.hms.grocy.model.Grocery;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.PostDataService;
import com.hms.grocy.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceryDetailActivity extends AppCompatActivity {

    private TextView tvGroceryName, tvGroceryPrice, tvGroceryStock, tvGrocerySold, tvGroceryDescription,
            tvStoreName, tvStoreCity;
    private ImageView imgGrocery;
    private ImageButton btnAddToCart;

    private int groceryId;

    private GetDataService getDataService;
    private PostDataService postDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_detail);

        tvGroceryName = findViewById(R.id.tv_grocery_name_detail);
        tvGroceryPrice = findViewById(R.id.tv_grocery_price_detail);
        tvGroceryStock = findViewById(R.id.tv_grocery_stock_detail);
        tvGrocerySold = findViewById(R.id.tv_grocery_sold_detail);
        tvGroceryDescription = findViewById(R.id.tv_grocery_description_detail);
        tvStoreName = findViewById(R.id.tv_store_name_detail);
        tvStoreCity = findViewById(R.id.tv_store_city_detail);
        imgGrocery = findViewById(R.id.img_grocery_detail);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);

        groceryId = (int) getIntent().getIntExtra("groceryId", 0);

        getGrocery();
    }

    public void doBack(View view) {
        finish();
    }

    public void addToCart(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quantity");
        EditText et_quantity = new EditText(this);
        et_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(et_quantity);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!et_quantity.getText().toString().trim().equals("")) {
                    dialog.dismiss();
                    int qty = Integer.parseInt(et_quantity.getText().toString());
                    if(qty <= 0)
                        return;
                    else {
                        postDataService = RetrofitClientInstance.getRetrofitInstance().create(PostDataService.class);
                        Call<Cart> call = postDataService.insertCart(MainActivity.getConsumer().getId(), groceryId, qty);
                        call.enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                showSuccessMessage();

                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {

                            }
                        });
                    }

                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void showSuccessMessage() {
        Toast.makeText(this,"Successfully add to cart", Toast.LENGTH_LONG).show();
    }

    private void getGrocery() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Grocery> call = getDataService.getGrocery(groceryId);
        call.enqueue(new Callback<Grocery>() {
            @Override
            public void onResponse(Call<Grocery> call, Response<Grocery> response) {
                viewGrocery(response.body());
            }

            @Override
            public void onFailure(Call<Grocery> call, Throwable t) {

            }
        });
    }

    private void viewGrocery(Grocery grocery) {
        if(grocery.getStock() == 0)
            btnAddToCart.setEnabled(false);

        tvGroceryName.setText(grocery.getName());
        tvGroceryPrice.setText("Rp " + grocery.getPrice());
        tvGroceryStock.setText(grocery.getStock() + "");
        tvGrocerySold.setText(grocery.getSold() + "");
        tvGroceryDescription.setText(grocery.getDescription());
        tvStoreName.setText(grocery.getStore().getName());
        tvStoreCity.setText(grocery.getStore().getCity());

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        builder.build().load(grocery.getImage())
                .placeholder(R.color.white)
                .error(R.drawable.unknown)
                .into(imgGrocery);
    }
}