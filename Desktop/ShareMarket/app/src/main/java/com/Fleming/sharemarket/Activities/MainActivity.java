package com.Fleming.sharemarket.Activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.Fleming.sharemarket.TestActivity;
import com.Fleming.sharemarket.adapter.MainAdapter;
import com.Fleming.sharemarket.common.AppUrl;
import com.Fleming.sharemarket.common.BeanClass;
import com.Fleming.sharemarket.common.RecyclerItemClickListener;
import com.Fleming.sharemarket.common.ShowDialogClass;
import com.Fleming.sharemarket.common.StringsClass;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toshiba.sharemarket.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    LinearLayout liner1, liner2, liner3, liner4, liner5, liner6, liner7, liner8, liner9, liner10;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MainAdapter adapter;
    ArrayList<BeanClass> listoperatorlist;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getShare_list();

        init();

    }


    private void init() {


        //....................get share_list............//
//        getShare_list();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_1);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent i = new Intent(MainActivity.this, GraphActivity.class);
                        i.putExtra("Share_name", listoperatorlist.get(position).getShare_name());
                        startActivity(i);


                    }
                }));


    }

    //


    private void getShare_list() {
        String all_shares = "AAPL,GOOGL,AABA,MSFT";

        //.................show dialog box...............//
        ShowDialogClass.showProgressing(MainActivity.this, StringsClass.loading, StringsClass.plswait);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.SHARE_LIST + all_shares,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {


                        if (arg0 != null) {
                            try {
                                //..................initilisation list ...

                                listoperatorlist = new ArrayList<>();

                                Log.e("pooja", "" + arg0);
                                String json = arg0.toString();
                                json = json.replace("//", "");
                                JSONArray jsonarray = new JSONArray(json);


                                for (int i = 0; i < jsonarray.length(); i++) {
                                    BeanClass beanClass = new BeanClass();
                                    JSONObject jsonObject = jsonarray.getJSONObject(i);

                                    beanClass.setShare_name(jsonObject.getString("t"));
                                    beanClass.setPrice(jsonObject.getString("l"));
                                    beanClass.setShare_Change_price(jsonObject.getString("c"));
                                    beanClass.setShare_type(jsonObject.getString("name"));
                                    beanClass.setShare_Change_price_percentage(jsonObject.getString("cp"));
                                    listoperatorlist.add(beanClass);

                                    ShowDialogClass.hideProgressing();


                                }

                                //.............set adapter................//
                                set_adapter();
                                ShowDialogClass.hideProgressing();


                            } catch (Exception e) {
                                e.printStackTrace();
                                ShowDialogClass.hideProgressing();

                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    private void set_adapter() {
        adapter = new MainAdapter(MainActivity.this, listoperatorlist);
        mRecyclerView.setAdapter(adapter);


    }

    public void Onclick(View view) {
        startActivity(new Intent(this, TestActivity.class));
    }


}
