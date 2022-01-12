package com.hms.grocy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {

    private CarouselView carouselView;

    private int[] images = {R.drawable.banner, R.drawable.ic_grocy};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        carouselView = view.findViewById(R.id.carousel);
        carouselView.setPageCount(2);

        carouselView.setImageListener(imageListener);

        return view;
    }

    ImageListener imageListener = new ImageListener() {

        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(images[position]);
            imageView.setAdjustViewBounds(true);
            imageView.setBackgroundColor(getResources().getColor(R.color.white));
        }
    };




}
