package com.hms.grocy.network;

import com.hms.grocy.model.Cart;
import com.hms.grocy.model.CartItem;
import com.hms.grocy.model.Consumer;

import java.util.ArrayList;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostDataService {

    @FormUrlEncoded
    @POST("/api/user/insert")
    Call<Consumer> insertConsumer(@Field("email") String email, @Field("name") String name, @Field("image") String image);

    @FormUrlEncoded
    @POST("/api/user")
    Call<Consumer> getConsumer(@Field("email") String email);

    @FormUrlEncoded
    @POST("/api/cart/{id}")
    Call<Cart> insertCart(@Path("id") int userId, @Field("grocery_id") int groceryId, @Field("quantity") int quantity);

    @DELETE("/api/cart/{id}")
    Call<Void> deleteCart(@Path("id") int userId, @Query("grocery_id") int groceryId);

    @POST("/api/checkout/{id}")
    Call<Void> checkout(@Path("id") int userId, @Body ArrayList<CartItem> carts, @Query("delivery") String delivery);
}
