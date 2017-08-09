package com.Fleming.sharemarket.database;

/**
 * Created by Toshiba- on 09-04-2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper extends  SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Sharemarket.db";
    public static final String CONTACTS_TABLE_NAME = "calls";
    // Contacts table name
    public static final String TABLE_NAME_PUTS = "PUTS";
    public static final String TABLE_NAME_CALLS = "CALS";

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
    SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

        SQLiteDatabase db  = context.openOrCreateDatabase(DATABASE_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY, null);
        String qry = "CREATE TABLE IF NOT EXISTS REMINDERTB(ID INTEGER PRIMARY KEY"
                + ",EVNTDATE TEXT,EVENT TEXT,SRC TEXT,DEST TEXT,EVNTTIME TEXT,MODE TEXT)";
        db.execSQL(qry);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,phone text,email text, street text,place text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }


    public long insertContact  (String name, String user_id,String image)
    {
         db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("userid", user_id);
        contentValues.put("userimage", image);
     long res = db.insert("contacts", null, contentValues);
        return (res);
}

//    public Cursor getData(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
//        return res;
//    }

//    public int numberOfRows(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
//        return numRows;
//    }

    public long updateContact (Integer id, String name, String phone, String email)
    {
         db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);

       long res= db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return (res);
    }

    public Integer deleteContact (Integer id)
    {
         db = this.getWritableDatabase();
       int res= db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
        return (res);
    }
//    public ArrayList<String> getAllCotacts()
//    {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
//            res.moveToNext();
//        }
//        return array_list;
//    }
}
