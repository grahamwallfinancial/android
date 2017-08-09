package com.Fleming.sharemarket;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.Fleming.sharemarket.database.DatabaseHandler;
import com.Fleming.sharemarket.database.SQLiteHelper;
import com.toshiba.sharemarket.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getData("2017/07/20 14:04:40");


    }


    private void getData(String str_ID) {
        try {


            DatabaseHandler sqLiteHelper = new DatabaseHandler(this);
            Cursor cursor = sqLiteHelper.getContact("calls");
            if (cursor != null) {

                if (cursor.moveToFirst()) {
                    do {
                        String str = cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ID));
                        Log.e("data",str);
                    } while (cursor.moveToNext());
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
