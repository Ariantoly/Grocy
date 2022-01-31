package com.hms.grocy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hms.grocy.R;
import com.hms.grocy.model.GrocyNotification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private ArrayList<GrocyNotification> notifications = new ArrayList<>();

    public NotificationAdapter(ArrayList<GrocyNotification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewNotification = inflater.inflate(R.layout.item_rv_notifications, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(viewNotification);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        GrocyNotification notification = notifications.get(position);

        TextView tvTitle = holder.tvTitle;
        tvTitle.setText(notification.getTitle());

        TextView tvBody = holder.tvBody;
        tvBody.setText(notification.getBody());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_notification_title);
            tvBody = itemView.findViewById(R.id.tv_notification_body);
        }
    }
}
