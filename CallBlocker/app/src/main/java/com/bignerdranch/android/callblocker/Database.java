package com.bignerdranch.android.callblocker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brycesulin
 */

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "call_blocker.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_BLOCKLIST = "blocklist";

    // Create table SQL statement
    private static final String TABLE_CREATE = "create table "  + TABLE_BLOCKLIST + "( id "
            + " integer primary key autoincrement, phone_number  text not null);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
