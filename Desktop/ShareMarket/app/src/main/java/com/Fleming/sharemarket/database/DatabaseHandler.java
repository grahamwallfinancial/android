package com.Fleming.sharemarket.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ShareMaeket.db";

    public static final String TABLE_NAME_PUTS = "PUTS";
    public static final String TABLE_NAME_CALLS = "CALLS";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_STRIKE_PRICE = "strike_price";
    public static final String KEY_PRICE_VALUE = "price_value";
    public static final String KEY_CHANGE_PRICE_VALUE = "change_price_value";
    public static final String KEY_BIDVALUE = "bid_value";
    public static final String KEY_ASK_VALUE = "ask_value";
    public static final String KEY_VOL_VALUE = "vol_value";
    public static final String KEY_OP_INT_VALUE = "op_int_value";
    public static final String KEY_DATE_TIME = "time";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME_PUTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_PRICE_VALUE + " TEXT," + KEY_CHANGE_PRICE_VALUE + " TEXT," + KEY_BIDVALUE + " TEXT," + KEY_ASK_VALUE + " TEXT," + KEY_VOL_VALUE + " TEXT,"
                + KEY_OP_INT_VALUE + " TEXT," + KEY_DATE_TIME + " TEXT," + KEY_STRIKE_PRICE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public  void addContact(ContentValues contact) {
        SQLiteDatabase db = this.getWritableDatabase();


        // Inserting Row
        db.insert(TABLE_NAME_PUTS, null, contact);
        db.close(); // Closing database connection
    }

    // Getting single contact
  public Cursor getContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_PUTS, new String[]{KEY_ID,
                        KEY_STRIKE_PRICE, KEY_PRICE_VALUE}, KEY_TYPE + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        // return contact
        return cursor;
    }

  /*  // Getting All Contacts
    public Cursor getAllContacts() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_PUTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
        }

        // return contact list
        return cursor;
    }*/




}
