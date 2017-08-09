package com.Fleming.sharemarket.Activities;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Fleming.sharemarket.common.AppUrl;
import com.Fleming.sharemarket.common.ChainlistClass;
import com.Fleming.sharemarket.common.ShowDialogClass;
import com.Fleming.sharemarket.common.StringsClass;
import com.Fleming.sharemarket.database.SQLiteHelper;
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

import java.util.ArrayList;

import static com.Fleming.sharemarket.Activities.GraphActivity.MY_COLORS;
import static com.Fleming.sharemarket.Activities.GraphActivity.Share_name;

public class GraphAll extends AppCompatActivity {

    //String current_date, yester_date, then_yes_date;
    static SQLiteDatabase sqLiteDatabase;
    static SQLiteHelper sqLiteHelper;
    static BarChart barChart1;
    BarChart barChart2, barChartputs1, barChartputs2, barChartputs3;
    BarChart barChart3;
    static ArrayList<ChainlistClass> listoperatorlist3;
    String current_date, yesterdaydate, beforeyesterday;
    Handler handler;

    private boolean isPaused;
    private boolean isPaused2;
    ImageView playPauseBtn, playPauseBtn2;
    static JSONArray jsonArray, jsonArray1;

    TextView currentdate, yesterdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_all);

        sqLiteHelper = new SQLiteHelper(this);

//        Log.e("seema", "current_date" + GraphActivity.get_provider_data());

        current_date = getIntent().getStringExtra("date");
        yesterdaydate = getIntent().getStringExtra("ydate");
        beforeyesterday = getIntent().getStringExtra("dydate");

        currentdate = (TextView) findViewById(R.id.calls_date);
        yesterdate = (TextView) findViewById(R.id.puts_date);

        currentdate.setVisibility(View.GONE);
        yesterdate.setVisibility(View.GONE);


        barChart1 = (BarChart) findViewById(R.id.barchart);
        barChart2 = (BarChart) findViewById(R.id.barchart2);
        barChart3 = (BarChart) findViewById(R.id.barchart3);
        barChartputs1 = (BarChart) findViewById(R.id.barchartputs1);
        barChartputs2 = (BarChart) findViewById(R.id.barchartputs2);
        barChartputs3 = (BarChart) findViewById(R.id.barchartputs3);

        barChart1.setVisibility(View.VISIBLE);
        barChart2.setVisibility(View.GONE);
        barChart3.setVisibility(View.GONE);
        barChartputs1.setVisibility(View.VISIBLE);
        barChartputs2.setVisibility(View.GONE);
        barChartputs3.setVisibility(View.GONE);


        get_list();


//        Log.e("Name3333: ", "values",barChart1 + contacts);

        playPauseBtn2 = (ImageView) findViewById(R.id.playPauseBtn2);
        playPauseBtn2.setImageResource(R.drawable.pause);
        playPauseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    isPaused = false;
                    playPauseBtn2.setImageResource(R.drawable.pause);
                    playGraphSlide(jsonArray);
                } else {
                    isPaused = true;
                    playPauseBtn2.setImageResource(R.drawable.play);
                }
            }
        });

        playPauseBtn = (ImageView) findViewById(R.id.playPauseBtn);
        playPauseBtn.setImageResource(R.drawable.pause);
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused2) {
                    isPaused2 = false;
                    playPauseBtn.setImageResource(R.drawable.pause);
                    playGraphSlide2(jsonArray1);
                } else {
                    isPaused2 = true;
                    playPauseBtn.setImageResource(R.drawable.play);
                }
            }
        });
    }


    private void playGraphSlide(JSONArray jsonArray) {
        if (barChart1.getVisibility() == View.VISIBLE) {
            loadGraphWithDelay(jsonArray, barChart2, 3000);
        } else if (barChart2.getVisibility() == View.VISIBLE) {
            loadGraphWithDelay(jsonArray, barChart3, 3000);
        } else if (barChart3.getVisibility() == View.VISIBLE) {
//            loadGraphWithDelay(jsonArray, barChart1, 3000);
        }

    }

    private void playGraphSlide2(JSONArray jsonArray1) {
        if (barChartputs1.getVisibility() == View.VISIBLE) {
            loadGraphWithDelay2(jsonArray1, barChartputs2, 3000);
        } else if (barChartputs2.getVisibility() == View.VISIBLE) {
            loadGraphWithDelay2(jsonArray1, barChartputs3, 3000);
        } else if (barChartputs3.getVisibility() == View.VISIBLE) {
//            loadGraphWithDelay(jsonArray1, barChartputs1, 3000);
        }

    }


    private void loadGraphWithDelay(final JSONArray jsonArray, final BarChart barChart, int DELAY_TIME) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isPaused) {
                    // hide all charts
                    barChart1.setVisibility(View.GONE);
                    barChart2.setVisibility(View.GONE);
                    barChart3.setVisibility(View.GONE);

                    // show target chart
                    barChart.setVisibility(View.VISIBLE);
                    get_graph_values(jsonArray, barChart);

                    // recursively load other graphs
                    switch (barChart.getId()) {
                        case R.id.barchart:
                            loadGraphWithDelay(jsonArray, barChart2, 3000);
                            currentdate.setText(yesterdaydate);
                            currentdate.setVisibility(View.VISIBLE);

                            break;
                        case R.id.barchart2:
                            loadGraphWithDelay(jsonArray, barChart3, 3000);
                            currentdate.setText(beforeyesterday);
                            currentdate.setVisibility(View.VISIBLE);


                            break;
                        case R.id.barchart3:
                            loadGraphWithDelay(jsonArray, barChart1, 3000);
                            currentdate.setText(current_date);
                            currentdate.setVisibility(View.VISIBLE);

                            // hide the play pause button after 3rd graph
                            playPauseBtn2.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        }, DELAY_TIME);
    }


    private void loadGraphWithDelay2(final JSONArray jsonArray1, final BarChart barChart, int DELAY_TIME) {
        handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!isPaused2) {
                    // hide all charts
                    barChartputs1.setVisibility(View.GONE);
                    barChartputs2.setVisibility(View.GONE);
                    barChartputs3.setVisibility(View.GONE);

                    // show target chart
                    barChart.setVisibility(View.VISIBLE);
                    get_graph_values(jsonArray1, barChart);

                    // recursively load other graphs
                    switch (barChart.getId()) {
                        case R.id.barchartputs1:
                            loadGraphWithDelay2(jsonArray1, barChartputs2, 3000);
                            yesterdate.setText(yesterdaydate);
                            yesterdate.setVisibility(View.VISIBLE);

                            break;
                        case R.id.barchartputs2:
                            loadGraphWithDelay2(jsonArray1, barChartputs3, 3000);
                            yesterdate.setText(beforeyesterday);
                            yesterdate.setVisibility(View.VISIBLE);

                            break;
                        case R.id.barchartputs3:
                            loadGraphWithDelay2(jsonArray1, barChartputs1, 3000);
                            yesterdate.setText(current_date);
                            yesterdate.setVisibility(View.VISIBLE);

                            // hide the play pause button after 3rd graph
                            playPauseBtn.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        }, DELAY_TIME);
    }

    private static void get_graph_values(JSONArray jsonObject, BarChart barChart_main) {

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

        //data.setValueTextSize(11f);
        bardataset.setColors(MY_COLORS);


        //data.setValueTextSize(11f);
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

        barChart_main.animateXY(1400, 5000);


        // Legends to show on bottom of the graph
        Legend l = barChart_main.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setXEntrySpace(20);
        l.setYEntrySpace(10);
    }


    private void get_graph_values2(JSONArray jsonObject, BarChart barChart_main) {


//        List<ChainlistClass> contacts = sqLiteHelper.getAllContacts(current_date);


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

        //data.setValueTextSize(11f);
        bardataset.setColors(MY_COLORS);


        //data.setValueTextSize(11f);
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

        barChart_main.animateXY(1400, 5000);


        // Legends to show on bottom of the graph
        Legend l = barChart_main.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setXEntrySpace(20);
        l.setYEntrySpace(10);
    }


    private void get_list() {


        //.................show dialog box...............//
        ShowDialogClass.showProgressing(GraphAll.this, StringsClass.loading, StringsClass.plswait);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.OPTION_CHAIN_URL + Share_name + "&output=json",


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {

                        //listoperatorlist = new ArrayList<>();


                        try {
                            final JSONObject json1st = new JSONObject(arg0);
                            JSONObject json2nd = json1st.getJSONObject("expiry");

                            Log.e("graph", "json1st" + json1st);
                            Log.e("graph", "json2nd" + json2nd);


                            String month = com.Fleming.sharemarket.common.Utils.get_month_list(json2nd.getString("m"));


                            JSONArray jsonputs = json1st.getJSONArray("puts");
                            JSONArray jsoncals = json1st.getJSONArray("calls");


                            if (json1st.has("calls")) {
                                Log.e("seea", "ggputs");

                                jsonArray = json1st.getJSONArray("calls"); //todo creating puts graph via callsputsGraph Activity
                                Log.e("graph", "jsonArray2" + jsonArray);

//                                get_graph_values2(jsonArray, barChart1);
                                playGraphSlide(jsonArray);


//                                            get_chart_values(jsonArray, barChart2);
                            }

                            if (json1st.has("puts")) {
                                Log.e("seea", "calls1");

                                jsonArray1 = json1st.getJSONArray("puts"); //todo creating call graph via callsputsGraph Activity
                                Log.e("graph", "jsonArray1" + jsonArray);
//                                get_chart_values(jsonArray, barChart1);


//                                get_graph_values2(jsonArray, barChartputs1);
                                playGraphSlide2(jsonArray1);


                            }


                            //todo this loop is doing nothing, Assuming or can be used to return all values to another activity
                            /*for (int i = 0; i < jsonArray.length(); i++) {
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


                            }*/
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
        RequestQueue requestQueue = Volley.newRequestQueue(GraphAll.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }


}