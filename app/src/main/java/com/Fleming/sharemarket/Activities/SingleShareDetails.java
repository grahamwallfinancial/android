package com.Fleming.sharemarket.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Fleming.sharemarket.common.AppUrl;
import com.Fleming.sharemarket.common.BeanClass;
import com.Fleming.sharemarket.common.ShowDialogClass;
import com.Fleming.sharemarket.common.StringsClass;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.toshiba.sharemarket.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleShareDetails extends AppCompatActivity {
    String Share_name,share_price,share_type,share_subtype,share_change_price;

    TextView Share_name_txt, Share_type_txt, Share_price_txt, Share_change_price_txt, Share_range_txt,
            Share_div_txt, Share_week_txt, Share_open_txt, Share_eps_txt, Share_shares_txt, Share_vol_txt,
            Share_beta_txt, Share_market_txt, Share_inst_txt;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlesharedetail);

        //..................get intent values................//
        Share_name = getIntent().getStringExtra("Share_name");
        Log.e("share", "Share_name" + Share_name);

        //....................get single share details...............//
        getsharedetails();

        Share_name_txt = (TextView) findViewById(R.id.Share_name_txt);
        Share_type_txt = (TextView) findViewById(R.id.Share_name_txt2);
        Share_price_txt = (TextView) findViewById(R.id.Share_price_txt);
        Share_change_price_txt = (TextView) findViewById(R.id.Share_change_price_txt);
        Share_range_txt = (TextView) findViewById(R.id.Share_range_txt);
        Share_div_txt = (TextView) findViewById(R.id.Share_div_txt);
        Share_week_txt = (TextView) findViewById(R.id.Share_week_txt);
        Share_eps_txt = (TextView) findViewById(R.id.Share_eps_txt);
        Share_open_txt = (TextView) findViewById(R.id.Share_open_txt);
        Share_shares_txt = (TextView) findViewById(R.id.Share_shares_txt);
        Share_vol_txt = (TextView) findViewById(R.id.Share_vol_txt);
        Share_beta_txt = (TextView) findViewById(R.id.Share_beta_txt);
        Share_market_txt = (TextView) findViewById(R.id.Share_market_txt);
        Share_inst_txt = (TextView) findViewById(R.id.Share_inst_txt);


        LinearLayout back = (LinearLayout) findViewById(R.id.back_menu_icn);
        Button Ok = (Button) findViewById(R.id.ok);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SingleShareDetails.this, GraphActivity.class);
                i.putExtra("Share_name_SINGLE",Share_name);
                i.putExtra("Share_price_SINGLE",share_price);
                i.putExtra("Share_price_change_SINGLE",share_change_price);
                i.putExtra("Share_type",share_type);
                i.putExtra("Share_subtype_SINGLE",share_subtype);

                startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getsharedetails() {
        ShowDialogClass.showProgressing(SingleShareDetails.this, StringsClass.loading, StringsClass.plswait);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.SHARE_LIST + Share_name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {


                        if (arg0 != null) {
                            try {

                              jsonObject = new JSONObject(arg0);
                                Log.e("share", "get single share details:" + jsonObject);
                                Share_name_txt.setText(jsonObject.getString("name"));
                                share_change_price=jsonObject.getString("c");
                                share_price=jsonObject.getString("l");
                                share_subtype=jsonObject.getString("name");
                                share_type=jsonObject.getString("e");

                                Log.e("sadskd", "shage" + Share_name_txt.getText().toString());
                                Share_type_txt.setText(jsonObject.getString("e") + " : " + jsonObject.getString("t"));
                                Share_price_txt.setText("$" + jsonObject.getString("l"));
                                Share_change_price_txt.setText(jsonObject.getString("c"));
                                Share_range_txt.setText(jsonObject.getString("name"));
                                Share_div_txt.setText(jsonObject.getString("div") + "/" + jsonObject.getString("yld"));
                                Share_week_txt.setText(jsonObject.getString("fwpe"));
                                Share_eps_txt.setText(jsonObject.getString("eps"));
                                Share_open_txt.setText(jsonObject.getString("op"));
                                Share_shares_txt.setText(jsonObject.getString("shares"));
                                Share_vol_txt.setText(jsonObject.getString("vo"));
                                Share_beta_txt.setText(jsonObject.getString("beta"));
                                Share_market_txt.setText(jsonObject.getString("mc"));
                                Share_inst_txt.setText(jsonObject.getString("inst_own"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(SingleShareDetails.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();


    }
}
