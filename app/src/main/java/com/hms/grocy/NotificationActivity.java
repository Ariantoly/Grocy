package com.hms.grocy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.hms.grocy.adapter.NotificationAdapter;
import com.hms.grocy.database.DatabaseHelper;
import com.hms.grocy.model.GrocyNotification;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    ArrayList<GrocyNotification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        dbHelper = DatabaseHelper.getInstance(this);

        getAllNotifications();
    }

    public void getAllNotifications() {
        notifications = dbHelper.getAllNotifications();
        RecyclerView rvNotifications = findViewById(R.id.rv_notifications);
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        rvNotifications.setAdapter(adapter);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public void doBack(View view) {
        finish();
    }
}