package com.Fleming.sharemarket.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.Fleming.sharemarket.adapter.Expirationgraph;
import com.Fleming.sharemarket.adapter.Refreshstate;
import com.Fleming.sharemarket.adapter.graph_adapter;
import com.Fleming.sharemarket.adapter.graph_adapter2;
import com.Fleming.sharemarket.common.AppUrl;
import com.Fleming.sharemarket.common.BeanClass;
import com.Fleming.sharemarket.common.ChainlistClass;
import com.Fleming.sharemarket.common.ExpirationsClass;
import com.Fleming.sharemarket.common.ShowDialogClass;
import com.Fleming.sharemarket.common.StringsClass;
import com.Fleming.sharemarket.common.Utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.toshiba.sharemarket.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GraphActivity extends AppCompatActivity {
    static String Share_name;
    String share_price;
    String share_type;
    String share_subtype;
    String share_change_price;
    static TextView expire_text;
    TextView share_name;
    TextView Share_price;
    TextView Share_cp;
    TextView Share_type;
    TextView share_refresh_state;
    static ArrayList<ChainlistClass> listoperatorlist, listoperatorlist2;
    static ArrayList<ExpirationsClass> expiration_list;
    static ArrayList<String> listoperatorlist1;
    private static RecyclerView mRecyclercalls;
    private static RecyclerView mRecyclerputs;
    //    private RecyclerView Recycler_view_exp;
    private LinearLayoutManager mLayoutManager, mLayoutManager1, mLayoutManager1_exp;
    private static graph_adapter adapter;
    private static graph_adapter2 adapter2;

    static Context ctx;
    Timer timer;

    public static final int[] MY_COLORS = {
            Color.rgb(84, 124, 101)
    };


    private static Spinner mySpinner, Refresh_spinner;
    private ArrayList<String> students;
    private static JSONArray result;
    int click = 0;
    String day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ctx = this;

        //..................get intent values................//
        Share_name = getIntent().getStringExtra("Share_name");

        //.................get sharedetails................//

        getSharedetails();

        //.................get option_chain_list................//
        get_list();

        share_name = (TextView) findViewById(R.id.share_name);
        Share_price = (TextView) findViewById(R.id.share_price);
        Share_type = (TextView) findViewById(R.id.share_type);
        Share_cp = (TextView) findViewById(R.id.share_change_price);
        expire_text = (TextView) findViewById(R.id.exp_text);


        mRecyclercalls = (RecyclerView) findViewById(R.id.recycle_view_calls);
        mRecyclerputs = (RecyclerView) findViewById(R.id.recycle_view_puts);
        mRecyclercalls.setHasFixedSize(true);
        mRecyclerputs.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager1 = new LinearLayoutManager(this);
        mLayoutManager1_exp = new LinearLayoutManager(this);

        mRecyclercalls.setLayoutManager(mLayoutManager);
        mRecyclerputs.setLayoutManager(mLayoutManager1);
        mRecyclercalls.setNestedScrollingEnabled(false);
        mRecyclerputs.setNestedScrollingEnabled(false);


        mySpinner = (Spinner) findViewById(R.id.mySpinner);
        Refresh_spinner = (Spinner) findViewById(R.id.mySpinner2);
        ArrayList<String> list = new ArrayList<String>();
        list.add("Select Refresh Rate");
        list.add("2 sec");
        list.add("5 sec");
        list.add("20 sec");
        list.add("1 min");
        list.add("5 min");

        Refreshstate adapter = new Refreshstate(ctx, 0, list);
        Refresh_spinner.setAdapter(adapter);

        Refresh_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int postion_value = Utils.get_postion(position);

                Log.e("spinner", "spinner_pos" + position);


                if (postion_value > 0) {
                    Log.e("postion_value", "postion_value" + postion_value);

                    get_timer(postion_value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Initializing the ArrayList
        students = new ArrayList<String>();

        //Initializing Spinner
        mySpinner = (Spinner) findViewById(R.id.mySpinner);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                click = 1;


                day = expiration_list.get(position).getDay();
                month = expiration_list.get(position).getMonth();
                year = expiration_list.get(position).getYear();

                get_list();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LinearLayout back = (LinearLayout) findViewById(R.id.back_menu_icn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout graph = (LinearLayout) findViewById(R.id.logout);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GraphActivity.this, CallPutsGraphActivity.class);
                startActivity(i);
            }
        });


    }

    private void getSharedetails() {
        Log.e("dddd", "dddddddddddddd");

        //.................show dialog box...............//
//        ShowDialogClass.showProgressing(GraphActivity.this, StringsClass.loading, StringsClass.plswait);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrl.SHARE_LIST + Share_name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {


                        if (arg0 != null) {
                            try {

                                JSONObject json = new JSONObject(arg0);
                                //..........................set data....................//
                                share_name.setText(json.getString("name"));
                                Share_price.setText("$" + json.getString("l"));
                                Share_type.setText(json.getString("e") + ":" + Share_name);
                                Share_cp.setText(json.getString("c"));

//                                ShowDialogClass.hideProgressing();


                            } catch (Exception e) {
                                e.printStackTrace();
//                                ShowDialogClass.hideProgressing();

                            }
                        }
//                        ShowDialogClass.hideProgressing();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(GraphActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }


    private void get_list() {

        Log.e("spinneda", "click" + click);

        String url_main = null;
        if (click == 1) {
            url_main = AppUrl.OPTION_CHAIN_URL + "AAPL" + "&expd=" + day + "&expm=" + month + "&expy=" + year + "&output=json";


        } else {
            url_main = AppUrl.OPTION_CHAIN_URL + Share_name + "&output=json";

        }


        Log.e("spinneda", "mail" + url_main);
        //.................show dialog box...............//
        ShowDialogClass.showProgressing(GraphActivity.this, StringsClass.loading, StringsClass.plswait);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url_main,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {

                        listoperatorlist = new ArrayList<>();
                        listoperatorlist2 = new ArrayList<>();

                        try {
                            JSONObject json1st = new JSONObject(arg0);
                            JSONObject json2nd = json1st.getJSONObject("expiry");


                            if (click == 0) {
                                result = json1st.getJSONArray("expirations");
                                getStudents(result);

                            }
//                            else{
//                            }

                            JSONArray jsoncalls = null;
                            try {
                                jsoncalls = json1st.getJSONArray("calls");
                                Log.e("spinneda", "jsoncalls" + jsoncalls);

                                for (int i = 0; i < jsoncalls.length(); i++) {
                                    ChainlistClass beanClass = new ChainlistClass();
                                    JSONObject jsonObject = jsoncalls.getJSONObject(i);

                                    beanClass.setStrike_price(jsonObject.getString("strike"));
                                    beanClass.setPrice_value(jsonObject.getString("p"));
                                    beanClass.setChange_price_value(jsonObject.getString("c"));
                                    beanClass.setBid_value(jsonObject.getString("b"));
                                    beanClass.setAsk_value(jsonObject.getString("a"));
                                    beanClass.setVol_value(jsonObject.getString("vol"));
                                    beanClass.setOp_int_value(jsonObject.getString("oi"));
                                    listoperatorlist2.add(beanClass);

                                    ShowDialogClass.hideProgressing();


                                }
                                //.............set adapter................//
                                set_adapter();
                                ShowDialogClass.hideProgressing();


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            JSONArray jsonputs = null;
                            try {
                                jsonputs = json1st.getJSONArray("puts");
                                for (int i = 0; i < jsonputs.length(); i++) {
                                    ChainlistClass beanClass = new ChainlistClass();
                                    JSONObject jsonObject = jsonputs.getJSONObject(i);

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
                                set_adapter2();
                                ShowDialogClass.hideProgressing();


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


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
        RequestQueue requestQueue = Volley.newRequestQueue(GraphActivity.this);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    private static void getStudents(JSONArray jsonArray) {
        listoperatorlist1 = new ArrayList<>();
        expiration_list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                ExpirationsClass expirationClass = new ExpirationsClass();
                JSONObject json = jsonArray.getJSONObject(i);

                expirationClass.setMonth(json.getString("m"));
                expirationClass.setYear(json.getString("y"));
                expirationClass.setDay(json.getString("d"));
                expiration_list.add(expirationClass);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Expirationgraph adapter = new Expirationgraph(ctx, 0, expiration_list);
        mySpinner.setAdapter(adapter);
        mySpinner.setSelection(0, false);


    }

    public static void set_adapter2() {
        adapter = new graph_adapter(ctx, listoperatorlist);
        mRecyclerputs.setAdapter(adapter);

    }


    private static void set_adapter() {
        adapter2 = new graph_adapter2(ctx, listoperatorlist2);

        mRecyclercalls.setAdapter(adapter2);


    }


    public static void get_expiration_date(String expiration) {

        //.................show dialog box...............//
        ShowDialogClass.showProgressing(ctx, StringsClass.loading, StringsClass.plswait);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, expiration,


                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {

                        listoperatorlist = new ArrayList<>();
                        listoperatorlist2 = new ArrayList<>();

                        try {
                            JSONObject json1st = new JSONObject(arg0);
                            JSONObject json2nd = json1st.getJSONObject("expiry");
                            Log.e("seeee", "seeeee" + json1st);
                            Log.e("graph", "json2nd" + json2nd);

                            result = json1st.getJSONArray("expirations");

//                            getStudents(result);


                            JSONArray jsoncalls = null;
                            try {
                                jsoncalls = json1st.getJSONArray("calls");

                                for (int i = 0; i < jsoncalls.length(); i++) {
                                    ChainlistClass beanClass = new ChainlistClass();
                                    JSONObject jsonObject = jsoncalls.getJSONObject(i);

                                    beanClass.setStrike_price(jsonObject.getString("strike"));
                                    beanClass.setPrice_value(jsonObject.getString("p"));
                                    beanClass.setChange_price_value(jsonObject.getString("c"));
                                    beanClass.setBid_value(jsonObject.getString("b"));
                                    beanClass.setAsk_value(jsonObject.getString("a"));
                                    beanClass.setVol_value(jsonObject.getString("vol"));
                                    beanClass.setOp_int_value(jsonObject.getString("oi"));
                                    listoperatorlist2.add(beanClass);

                                    ShowDialogClass.hideProgressing();


                                }
                                //.............set adapter................//
                                set_adapter();
                                ShowDialogClass.hideProgressing();


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            JSONArray jsonputs = null;
                            try {
                                jsonputs = json1st.getJSONArray("puts");

                                for (int i = 0; i < jsonputs.length(); i++) {
                                    ChainlistClass beanClass = new ChainlistClass();
                                    JSONObject jsonObject = jsonputs.getJSONObject(i);

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
                                set_adapter2();
                                ShowDialogClass.hideProgressing();


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


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
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();


    }


    //...................set Refresh tym................//
    public void get_timer(int delay) {

        Log.e("delay", "delay" + delay);
        timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {


                //.................get option_chain_list................/;
                get_list();
                click = 0;


            }
        }, delay);


    }

}
