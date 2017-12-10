package com.bignerdranch.android.callblocker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by brycesulin
 */

public class BlocklistDataSource {

    private SQLiteDatabase database;
    private Database dbHelper;

    public BlocklistDataSource(Context context) {
        dbHelper = new Database(context);
        open();
    }

    private void open() throws SQLException {

        database = dbHelper.getWritableDatabase();
    }

    public void close() {

        dbHelper.close();
    }

    // Add a number to the database
    public Blocklist create(final Blocklist blockList) {

        final ContentValues values = new ContentValues();

        values.put("phone_number", blockList.phoneNumber);

        final long id = database.insert(Database.TABLE_BLOCKLIST , null, values);

        blockList.id = id;
        return blockList;
    }

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    public void edit(final Blocklist blockList) {

        ContentValues cv = new ContentValues();

        cv.put("phone_number", blockList.phoneNumber);

        //database.update(Database.TABLE_BLOCKLIST, cv, "phone_number = '" + blockList.phoneNumber + "'", null);
        database.update(Database.TABLE_BLOCKLIST,cv,"phone_number = ?",new String[]{blockList.phoneNumber});
    }

//4$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    // Delete a number from the database
    public void delete(final Blocklist blockList) {
        database.delete(Database.TABLE_BLOCKLIST, "phone_number = '" + blockList.phoneNumber + "'", null);
    }

    public List<Blocklist> getAllBlocklist() {

        final List<Blocklist> blocklistNumbers = new ArrayList<Blocklist>();

        final Cursor cursor = database.query(Database.TABLE_BLOCKLIST, new String[]{"id","phone_number"}, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final Blocklist number = new Blocklist();

            number.id = cursor.getLong(0);
            number.phoneNumber = cursor.getString(1);

            blocklistNumbers.add(number);

            cursor.moveToNext();
        }
        return blocklistNumbers;
    }
}