package com.Fleming.sharemarket.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Fleming.sharemarket.common.AppUrl;
import com.Fleming.sharemarket.common.ChainlistClass;
import com.Fleming.sharemarket.common.ExpirationsClass;
import com.Fleming.sharemarket.common.ShowDialogClass;
import com.Fleming.sharemarket.common.StringsClass;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.toshiba.sharemarket.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static com.Fleming.sharemarket.Activities.GraphActivity.MY_COLORS;
import static com.Fleming.sharemarket.Activities.GraphActivity.Share_name;

public class CallPutsGraphActivity extends AppCompatActivity {

    private LinearLayoutManager mLayoutManager, mLayoutManager1;

    private RecyclerView mRecyclerView;
    String puts, calls;
    BarChart barChart1, barChart2;
    static ArrayList<ChainlistClass> listoperatorlist;
    static JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_puts_graph);

        //.......................get intent values..................//


        barChart1 = (BarChart) findViewById(R.id.barchart);
        barChart2 = (BarChart) findViewById(R.id.barchart2);


        get_list();
        ;

        LinearLayout back = (LinearLayout) findViewById(R.id.back_menu_icn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void get_list() {


        //.................show dialog box...............//
        ShowDialogClass.showProgressing(CallPutsGraphActivity.this, StringsClass.loading, StringsClass.plswait);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.OPTION_CHAIN_URL + Share_name + "&output=json",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {

                        listoperatorlist = new ArrayList<>();


                        try {
                            JSONObject json1st = new JSONObject(arg0);
                            JSONObject json2nd = json1st.getJSONObject("expiry");

                            Log.e("graph", "json1st" + json1st);
                            Log.e("graph", "json2nd" + json2nd);




                            JSONArray jsonputs = json1st.getJSONArray("puts");

                            if (json1st.has("puts")) {
                                jsonArray = json1st.getJSONArray("puts");
                                Log.e("graph", "jsonArray" + jsonArray);
                                get_chart_values(jsonArray, barChart2);


                            }

                            if (json1st.has("calls")) {
                                jsonArray = json1st.getJSONArray("calls");
                                Log.e("graph", "jsonArray1" + jsonArray);
                                get_chart_values(jsonArray, barChart1);

                            }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                ChainlistClass beanClass = new ChainlistClass();
                                JSONObject jsonObject = jsonputs.getJSONObject(i);
                                Log.e("shares", "jsonObject" + jsonObject);

                                beanClass.setStrike_price(jsonObject.getString("strike"));
                                beanClass.setPrice_value(jsonObject.getString("p"));
                                beanClass.setChange_price_value(jsonObject.getString("c"));
                                beanClass.setBid_value(jsonObject.getString("b"));
                                beanClass.setAsk_value(jsonObject.getString("a"));
                                beanClass.setVol_value(jsonObject.getString("vol"));
                                beanClass.setOp_int_value(jsonObject.getString("oi"));
                                listoperatorlist.add(beanClass);

                                ShowDialogClass.hideProgressing();


                            }
                            //.............set adapter................//
                            ShowDialogClass.hideProgressing();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        ShowDialogClass.hideProgressing();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(CallPutsGraphActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }


    private void get_chart_values(JSONArray jsonObject, BarChart barChart_main) {
        Log.e("share", "jsonObject" + jsonObject);

        ArrayList<BarEntry> entries = new ArrayList<>();


        try {

            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject json = jsonObject.getJSONObject(i);
                int amount_sum = Integer.parseInt(json.getString("oi"));


                Log.e("share", "amount_sums" + amount_sum);
                if (amount_sum != 0) {
                    entries.add(new BarEntry(amount_sum, i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        BarDataSet bardataset = new BarDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();


        try {
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject json = jsonObject.getJSONObject(i);
                String amount_sum1 = json.getString("strike");
                Log.e("share", "amount_sum1" + amount_sum1);

                if (!jsonObject.getJSONObject(i).equals("0")) {
                    labels.add(amount_sum1);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        bardataset.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        BarData data = new BarData(labels, bardataset);

//        data.setValueTextSize(11f);
        bardataset.setColors(MY_COLORS);


//        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        barChart_main.setData(data); // set the data and list of lables into chart

        // undo all highlights
        barChart_main.highlightValues(null);
        barChart_main.setNoDataText("loading");
        barChart_main.setNoDataTextDescription("");

        // refresh/update pie chart
        barChart_main.invalidate();


        barChart_main.setDrawGridBackground(false);
        barChart_main.getXAxis().setTextColor(Color.WHITE);
        barChart_main.getAxisLeft().setTextColor(Color.TRANSPARENT);
        barChart_main.getAxisRight().setTextColor(Color.WHITE);

        barChart_main.getXAxis().setDrawGridLines(false);
        barChart_main.getXAxis().setDrawAxisLine(true);
        barChart_main.getAxisLeft().setDrawGridLines(false);
        barChart_main.getAxisLeft().setDrawAxisLine(false);
        barChart_main.getAxisRight().setDrawGridLines(false);
        barChart_main.getAxisRight().setDrawAxisLine(true);
        barChart_main.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart_main.animateXY(1400, 1400);


        // Legends to show on bottom of the graph
        Legend l = barChart_main.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setXEntrySpace(10);
        l.setYEntrySpace(10);


    }
}
