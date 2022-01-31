package com.hms.grocy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hms.grocy.model.GrocyNotification;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GrocyDB";
    private static final int DATABASE_VERSION = 1;
    
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String COLUMN_NOTIFICATION_ID = "id";
    private static final String COLUMN_NOTIFICATION_TITLE = "title";
    private static final String COLUMN_NOTIFICATION_BODY = "body";

    private static DatabaseHelper dbHelper;

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_create_table_notification =
                "CREATE TABLE " + TABLE_NOTIFICATION + "("
                        + COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_NOTIFICATION_TITLE + " TEXT,"
                        + COLUMN_NOTIFICATION_BODY + " TEXT"
                        + ")";

        db.execSQL(query_create_table_notification);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE " + TABLE_NOTIFICATION;
        db.rawQuery(query, null);
        onCreate(db);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(dbHelper == null)
            dbHelper = new DatabaseHelper(context);

        return dbHelper;
    }

    public void addNotification(GrocyNotification notification) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTIFICATION_TITLE, notification.getTitle());
        cv.put(COLUMN_NOTIFICATION_BODY, notification.getBody());

        DB.insert(TABLE_NOTIFICATION, null, cv);
        DB.close();
        Log.v("Grocy", "Insert");
    }

    public ArrayList<GrocyNotification> getAllNotifications() {
        ArrayList<GrocyNotification> notifications = new ArrayList<GrocyNotification>();
        SQLiteDatabase DB = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NOTIFICATION;
        Cursor cursor = DB.rawQuery(query, null);

        if(cursor.moveToFirst() ) {
            do {
                GrocyNotification notification = new GrocyNotification();
                notification.setId(Integer.parseInt(cursor.getString(0)));
                notification.setTitle(cursor.getString(1));
                notification.setBody(cursor.getString(2));
                notifications.add(notification);
            } while(cursor.moveToNext());
        }

        return notifications;
    }
}
