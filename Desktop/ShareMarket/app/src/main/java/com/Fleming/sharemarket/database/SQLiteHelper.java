package com.Fleming.sharemarket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.Fleming.sharemarket.common.ChainlistClass;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Harvinder Singh on 28-12-2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
//    private static final String DATABASE_NAME = "contactsManager";
//    private static final String DATABASE_NAME = "testDatabase";
    private static final String DATABASE_NAME = "myDatabase.db";
    // Contacts table name
    public static final String TABLE_NAME_PUTS = "PUTS";
    public static final String TABLE_NAME_CALLS = "CALLS";

    // Contacts Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_TYPE = "type";
    public static final String KEY_STRIKE_PRICE = "strike_price";
    public static final String KEY_PRICE_VALUE = "price_value";
    public static final String KEY_CHANGE_PRICE_VALUE = "change_price_value";
    public static final String KEY_BIDVALUE = "bid_value";
    public static final String KEY_ASK_VALUE = "ask_value";
    public static final String KEY_VOL_VALUE = "vol_value";
    public static final String KEY_OP_INT_VALUE = "op_int_value";
    public static final String KEY_DATE_TIME = "time";

//    private static final String KEY_PH_NO = "phone_number";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME_PUTS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_STRIKE_PRICE + " LONG," +
                KEY_PRICE_VALUE + " LONG," + KEY_CHANGE_PRICE_VALUE + " LONG," + KEY_BIDVALUE + " LONG," +
                KEY_ASK_VALUE + " LONG," + KEY_VOL_VALUE + " LONG," + KEY_OP_INT_VALUE + " LONG," +
                KEY_DATE_TIME + " LONG" + ");";

        String query2 = "CREATE TABLE " + TABLE_NAME_CALLS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_STRIKE_PRICE + " LONG," +
                KEY_PRICE_VALUE + " LONG," + KEY_CHANGE_PRICE_VALUE + " LONG," + KEY_BIDVALUE + " LONG," +
                KEY_ASK_VALUE + " LONG," + KEY_VOL_VALUE + " LONG," + KEY_OP_INT_VALUE + " LONG," +
                KEY_DATE_TIME + " LONG" + ");";
        db.execSQL(query);

        db.execSQL(query2);
//        db.execSQL("CREATE TABLE " + TABLE_NAME_CALLS + " ( " + KEY_ID + "INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT," + KEY_STRIKE_PRICE + " LONG," +
//                KEY_PRICE_VALUE + " LONG," + KEY_CHANGE_PRICE_VALUE + " LONG," + KEY_BIDVALUE + " LONG," +
//                KEY_ASK_VALUE + " LONG," + KEY_VOL_VALUE + " LONG," + KEY_OP_INT_VALUE + " LONG," +
//                KEY_DATE_TIME + " LONG" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CALLS);

        onCreate(db);
    }

    public Cursor getDatabydate() {
        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String query = "SELECT * FROM " + TABLE_NAME_PUTS + " WHERE KEY_TYPE=" +"calls";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void addContact(ContentValues contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putAll(contact);
        // Inserting Row
        long i = db.insert(TABLE_NAME_CALLS, null, contact);
        db.close(); // Closing database connection
    }

    public List<ChainlistClass> getAllContacts(String current_date) {

        List<ChainlistClass> contactList = new ArrayList<ChainlistClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_CALLS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME_CALLS,
                    new String[]{KEY_TYPE, KEY_STRIKE_PRICE, KEY_PRICE_VALUE, KEY_CHANGE_PRICE_VALUE, KEY_ASK_VALUE, KEY_BIDVALUE, KEY_VOL_VALUE, KEY_OP_INT_VALUE, KEY_DATE_TIME}, null, null, null, null,null);
//        Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list

            int numRows = cursor.getCount();
            cursor.moveToFirst();
            int lastTenValue = numRows - 10;
            Log.e("xpwallet", "lastTenValue" + lastTenValue);


            for (int i = 0; i < numRows; ++i) {
                ChainlistClass get_provider_data = new ChainlistClass();


//                    if (c.getString(4).equalsIgnoreCase("0")) {
//
//                        Log.e("data", "data" + c.getString(0));
//                        ContentValues args = new ContentValues();
//                        args.put(KEY_STATUS, "1");
//
//                        db.update(DATABASE_TABLE, args, KEY_NEWSID + "=" + c.getString(0), null);
//                    }

//                    get_provider_data.setId(Integer.parseInt(cursor.getString(0)));
                get_provider_data.setType(cursor.getString(0));
                get_provider_data.setStrike_price(cursor.getString(1));
                get_provider_data.setPrice_value(cursor.getString(2));
                get_provider_data.setChange_price_value(cursor.getString(3));
                get_provider_data.setBid_value(cursor.getString(4));
                get_provider_data.setAsk_value(cursor.getString(5));
                get_provider_data.setVol_value(cursor.getString(6));
                get_provider_data.setOp_int_value(cursor.getString(7));
                get_provider_data.setTime(cursor.getString(8));

                Log.e("seema", "1" + cursor.getString(8));

//				mnewsdata.setMessage_delivertime(c.getString(0));

//				mnewsdata.setUserimg(c.getString(0));


                if (current_date.equalsIgnoreCase(cursor.getString(8))) {
                    contactList.add(get_provider_data);
                    Log.e("seema", "tre" + current_date);

                } else {
                    Log.e("seema", "2" + current_date);

                }
                cursor.moveToNext();

            }

        } catch (SQLException e) {

        } finally {
            if (cursor != null && cursor.isClosed()) {

                cursor.close();
            }
        }

        return contactList;
    }
//
//
//
//        if (cursor.moveToFirst()) {
//            do {
//                ChainlistClass get_provider_data = new ChainlistClass();
//                get_provider_data.setStrike_price(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_STRIKE_PRICE)));
////                get_provider_data.setPrice_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_PRICE_VALUE)));
////                get_provider_data.setChange_price_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_CHANGE_PRICE_VALUE)));
////                get_provider_data.setBid_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIDVALUE)));
////                get_provider_data.setAsk_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ASK_VALUE)));
////                get_provider_data.setVol_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_VOL_VALUE)));
//                get_provider_data.setOp_int_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_OP_INT_VALUE)));
////                get_provider_data.setTime(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)));
////                if (date_select.equals(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)))) {
//                contactList.add(get_provider_data);
////                    listoperatorlist3.add(new ChainlistClass(get_provider_data.getOp_int_value(), get_provider_data.getStrike_price()));
//                Log.e("Name3333: ", "" + get_provider_data.getOp_int_value());
//
////                }
//            } while (cursor.moveToNext());
//        }

    // return contact list
//        return contactList;
//}

}
