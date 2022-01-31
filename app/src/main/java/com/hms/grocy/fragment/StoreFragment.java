package com.hms.grocy.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.R;
import com.hms.grocy.adapter.GroceryStoreAdapter;
import com.hms.grocy.model.GroceryStore;
import com.hms.grocy.network.GetDataService;
import com.hms.grocy.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreFragment extends Fragment {

    private View view;
    private GetDataService getDataService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store, null);

        getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<ArrayList<GroceryStore>> call = getDataService.getAllGroceryStores();
        call.enqueue(new Callback<ArrayList<GroceryStore>>() {
            @Override
            public void onResponse(Call<ArrayList<GroceryStore>> call, Response<ArrayList<GroceryStore>> response) {
                viewGroceryStores(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<GroceryStore>> call, Throwable t) {

            }
        });

        return view;
    }

    public void viewGroceryStores(ArrayList<GroceryStore> groceryStores) {
        RecyclerView rvGroceryStores = view.findViewById(R.id.rv_stores);
        GroceryStoreAdapter adapter = new GroceryStoreAdapter(groceryStores);
        rvGroceryStores.setAdapter(adapter);
        rvGroceryStores.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
    }
}
