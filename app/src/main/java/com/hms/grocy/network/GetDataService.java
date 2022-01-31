package com.hms.grocy.network;

import com.hms.grocy.model.Carousel;
import com.hms.grocy.model.Cart;
import com.hms.grocy.model.Category;
import com.hms.grocy.model.Grocery;
import com.hms.grocy.model.GroceryStore;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/api/carousel")
    Call<ArrayList<Carousel>> getAllCarousels();

    @GET("/api/category")
    Call<ArrayList<Category>> getAllCategories();

    @GET("/api/category/{id}")
    Call<Category> getCategory(@Path("id") int categoryId);

    @GET("/api/grocery/recommended")
    Call<ArrayList<Grocery>> getRecommendedGroceries();

    @GET("/api/grocery/{id}")
    Call<Grocery> getGrocery(@Path("id") int groceryId);

    @GET("/api/search/grocery")
    Call<ArrayList<Grocery>> searchGrocery(@Query("keyword") String keyword);

    @GET("/api/store")
    Call<ArrayList<GroceryStore>> getAllGroceryStores();

    @GET("/api/store/top")
    Call<ArrayList<GroceryStore>> getTopGroceryStores(@Query("city") String city);

    @GET("/api/store/{id}")
    Call<GroceryStore> getGroceryStore(@Path("id") int storeId);

    @GET("/api/cart/{id}")
    Call<Cart> getAllCarts(@Path("id") int userId);

}
