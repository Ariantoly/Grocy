package com.hms.grocy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.GroceriesActivity;
import com.hms.grocy.MainActivity;
import com.hms.grocy.NotificationActivity;
import com.hms.grocy.adapter.CategoryAdapter;
import com.hms.grocy.R;
import com.hms.grocy.adapter.GroceryAdapter;
import com.hms.grocy.adapter.GroceryStoreAdapter;
import com.hms.grocy.model.Carousel;
import com.hms.grocy.model.Category;
import com.hms.grocy.model.Grocery;
import com.hms.grocy.model.GroceryStore;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.RetrofitClientInstance;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private CarouselView carouselView;

    private ArrayList<String> carouselImages = new ArrayList<String>();
    private View view;

    private GetDataService getDataService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        carouselView = view.findViewById(R.id.carousel);

        EditText et_search = view.findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_GO) {
                    Intent intent = new Intent(getContext(), GroceriesActivity.class);
                    intent.putExtra("keyword", et_search.getText().toString());
                    startActivity(intent);
                    handled = true;
                }

                return handled;
            }
        });

        ImageButton btnNotification = view.findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        setCarousel();

        getCategories();

        getRecommendedGroceries();

        getTopGroceryStores();

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.Builder builder = new Picasso.Builder(view.getContext());
            builder.downloader(new OkHttp3Downloader(view.getContext()));
            builder.build().load(carouselImages.get(position))
                    .placeholder(R.color.white)
                    .error(R.drawable.unknown)
                    .into(imageView);
            imageView.setAdjustViewBounds(true);
            imageView.setBackgroundColor(getResources().getColor(R.color.white));
        }
    };

    private void setCarousel() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ArrayList<Carousel>> call = getDataService.getAllCarousels();
        call.enqueue(new Callback<ArrayList<Carousel>>() {
            @Override
            public void onResponse(Call<ArrayList<Carousel>> call, Response<ArrayList<Carousel>> response) {
                setCarouselImage(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Carousel>> call, Throwable t) {
                setCarouselImage(null);
            }
        });
    }

    private void setCarouselImage(ArrayList<Carousel> carousels) {
        if(carousels != null) {
            for (Carousel c : carousels) {
                carouselImages.add(c.getImage());
            }
            carouselView.setImageListener(imageListener);
            carouselView.setPageCount(carousels.size());
        }
    }

    private void getCategories() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ArrayList<Category>> call = getDataService.getAllCategories();
        call.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                viewCategories(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });
    }

    private void viewCategories(ArrayList<Category> categories) {
        RecyclerView rvCategories = view.findViewById(R.id.rv_categories);
        CategoryAdapter adapter = new CategoryAdapter(categories);
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void getRecommendedGroceries() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ArrayList<Grocery>> call = getDataService.getRecommendedGroceries();
        call.enqueue(new Callback<ArrayList<Grocery>>() {
            @Override
            public void onResponse(Call<ArrayList<Grocery>> call, Response<ArrayList<Grocery>> response) {
                viewRecommendedGroceries(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Grocery>> call, Throwable t) {

            }
        });
    }

    private void viewRecommendedGroceries(ArrayList<Grocery> groceries) {
        RecyclerView rvRecommendedGroceries = view.findViewById(R.id.rv_recommended);
        GroceryAdapter adapter = new GroceryAdapter(groceries);
        rvRecommendedGroceries.setAdapter(adapter);
        rvRecommendedGroceries.setLayoutManager(new GridLayoutManager(view.getContext(), 1, RecyclerView.HORIZONTAL, false));
    }

    private void getTopGroceryStores() {
        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        String city = ((MainActivity) getActivity()).getCity();
        Call<ArrayList<GroceryStore>> call = getDataService.getTopGroceryStores(city);
        call.enqueue(new Callback<ArrayList<GroceryStore>>() {
            @Override
            public void onResponse(Call<ArrayList<GroceryStore>> call, Response<ArrayList<GroceryStore>> response) {
                viewTopGroceryStores(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<GroceryStore>> call, Throwable t) {

            }
        });
    }

    private void viewTopGroceryStores(ArrayList<GroceryStore> groceryStores) {
        RecyclerView rvTopGroceryStores = view.findViewById(R.id.rv_top_grocery_stores);
        GroceryStoreAdapter adapter = new GroceryStoreAdapter(groceryStores);
        rvTopGroceryStores.setAdapter(adapter);
        rvTopGroceryStores.setLayoutManager(new GridLayoutManager(view.getContext(), 1, RecyclerView.HORIZONTAL, false));
    }

}
