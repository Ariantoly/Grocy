package com.hms.grocy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.CategoryActivity;
import com.hms.grocy.R;
import com.hms.grocy.model.Category;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Category> categories;
    private Context context;

    public CategoryAdapter(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewCategory = inflater.inflate(R.layout.item_rv_categories, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewCategory);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);

        TextView tvCategoryName = holder.tvCategoryName;
        tvCategoryName.setText(category.getName());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(categories.get(position).getImage())
                .placeholder(R.drawable.unknown)
                .error(R.drawable.unknown)
                .into(holder.imgCategory);

        LinearLayout llCategories = holder.llCategories;
        llCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                intent.putExtra("categoryId", category.getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategoryName;
        public ImageView imgCategory;
        public LinearLayout llCategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            imgCategory = itemView.findViewById(R.id.img_category);
            llCategories = itemView.findViewById(R.id.ll_categories);
        }
    }
}
