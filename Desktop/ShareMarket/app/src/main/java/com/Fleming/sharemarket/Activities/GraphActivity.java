package com.Fleming.sharemarket.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.Fleming.sharemarket.adapter.Expirationgraph;
import com.Fleming.sharemarket.adapter.Refreshstate;
import com.Fleming.sharemarket.adapter.graph_adapter;
import com.Fleming.sharemarket.adapter.graph_adapter2;
import com.Fleming.sharemarket.common.AppUrl;
import com.Fleming.sharemarket.common.ChainlistClass;
import com.Fleming.sharemarket.common.ExpirationsClass;
import com.Fleming.sharemarket.common.ShowDialogClass;
import com.Fleming.sharemarket.common.StringsClass;
import com.Fleming.sharemarket.common.Utils;

import com.Fleming.sharemarket.database.SQLiteHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.gson.Gson;
import com.toshiba.sharemarket.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.Fleming.sharemarket.common.Utils.getYesterdayValue;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_ASK_VALUE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_BIDVALUE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_CHANGE_PRICE_VALUE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_DATE_TIME;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_ID;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_OP_INT_VALUE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_PRICE_VALUE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_STRIKE_PRICE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_TYPE;
import static com.Fleming.sharemarket.database.SQLiteHelper.KEY_VOL_VALUE;
import static com.Fleming.sharemarket.database.SQLiteHelper.TABLE_NAME_CALLS;
import static com.Fleming.sharemarket.database.SQLiteHelper.TABLE_NAME_PUTS;


public class GraphActivity extends AppCompatActivity {
    static String Share_name;

    static TextView expire_text;
    TextView share_name;
    TextView Share_price;
    TextView Share_cp;
    TextView Share_type;
    static ArrayList<ChainlistClass> listoperatorlist, listoperatorlist2;
    static ArrayList<ExpirationsClass> expiration_list;
    static ArrayList<String> listoperatorlist1;
    private static RecyclerView mRecyclercalls;
    private static RecyclerView mRecyclerputs;
    //    private RecyclerView Recycler_view_exp;
    private LinearLayoutManager mLayoutManager, mLayoutManager1, mLayoutManager1_exp;
    private static graph_adapter adapter;
    private static graph_adapter2 adapter2;
    Handler handler;
    static Context ctx;
    Timer timer;
    static ChainlistClass get_provider_data;
    public static final int[] MY_COLORS = {
            Color.rgb(84, 124, 101)
    };


    private static Spinner mySpinner, Refresh_spinner;
    private ArrayList<String> students;
    private static JSONArray result;
    int click = 0;
    int expDate = -1; //expiration date.
    String day, month, year;
    static SQLiteDatabase sqLiteDatabase;
    static SQLiteHelper sqLiteHelper;
    static String share_type;
    String share_type1;

    private static final String TAG = "Google Drive Activity";
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private static final int REQUEST_CODE_OPENER = 2;
    private static GoogleApiClient mGoogleApiClient;
    private static boolean fileOperation = false;
    private DriveId mFileId;
    public DriveFile file;
    JSONArray jsoncalls = null;
    Gson gson;
    private int year1;
    private int month2;
    private int day1;
    static final int DATE_PICKER_ID = 1111;
    //    ChainlistClass get_provider_data;
    String date_select;
    String new_time2;
    String get_date_value = "";
    int type_value_database = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ctx = this;
        sqLiteHelper = new SQLiteHelper(this);

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
//        Cursor c1=sqLiteHelper.getDatabydate();
//        Log.e("xpwallet", "c" + c1.getCount());

        //..................get intent values................//
        Share_name = getIntent().getStringExtra("Share_name");

        Log.e("pooja", "Share_name" + Share_name);

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

        if (click == 1) {
            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    expDate = position;
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

        }


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


                getLocalSoloQuestions();


//                getLocalSoloQuestions2();


//                sqLiteDatabase.delete(sqLiteHelper.TABLE_NAME_CALLS, null, null);

                Intent i = new Intent(GraphActivity.this, CallPutsGraphActivity.class); //todo call to graphMotionactivity
                startActivity(i);
            }
        });


        // TODO ------------------------------ CALL TO GRAPH ALL ACTIVITY

        LinearLayout video = (LinearLayout) findViewById(R.id.video);

        final Calendar c = Calendar.getInstance();
        year1 = c.get(Calendar.YEAR);
        month2 = c.get(Calendar.MONTH) + 1;
        day1 = c.get(Calendar.DAY_OF_MONTH);

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("Demo testng ", "getAllDatas" + getAllDatas());
//
                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "yyyy/MM/dd"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        Log.e("Demo testng ", "data" + sdf.format(myCalendar.getTime()));
                        Log.e("Demo testng ", "data2" + sdf.format(myCalendar.getTime()));


                        String get_yesterday_date = getYesterdayValue(sdf.format(myCalendar.getTime()));
                        String before_yesterday_date = getYesterdayValue(get_yesterday_date);

                        Log.e("Demo testng ", "get_yesterday_date" + get_yesterday_date);
                        Log.e("Demo testng ", "before_yesterday_date" + before_yesterday_date);


//                        List<ChainlistClass> data = sqLiteHelper.getAllContacts(sdf.format(myCalendar.getTime()));
//                        Log.e("Demo testng ", "data" + data);


                        Intent videoIntent = new Intent(getApplicationContext(), GraphAll.class);
                        videoIntent.putExtra("date", sdf.format(myCalendar.getTime()));
                        videoIntent.putExtra("ydate", get_yesterday_date);
                        videoIntent.putExtra("dydate", before_yesterday_date);
                        startActivity(videoIntent);


//                        your_view.setText(sdf.format(myCalendar.getTime()));

                    }

                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(GraphActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();


            }
        });

    }


    private String getDate(int index) //returns date from expiration list at position index as a string in the format Day/Month/Year
    {
        String date = "";
        try {
            date = expiration_list.get(index).getDay() + "/" + expiration_list.get(index).getMonth() + "/" + expiration_list.get(index).getYear();
        } catch (Exception e) //catches any error in getting the date.
        {
            Log.e("getDate:", "Could not get date!");
            date = "Could not fetch date";
        }
        return date;
    }

    private void drawDate(boolean draw) //draws the data to the txtDate textView when called with draw parameter set to true.
    {
        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        if (draw == true) {
            String date = getDate(expDate);
            txtDate.setText(date);
        } else //if the date does not need to be drawn sets the txtDate to nothing.
        {
            txtDate.setText("");
        }
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
            url_main = AppUrl.OPTION_CHAIN_URL + Share_name + "&expd=" + day + "&expm=" + month + "&expy=" + year + "&output=json";


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
                            String time = Utils.getDate(); //todo can select different date
//                            SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm");
//                            String time2 = localDateFormat.format(new Date(time));
//                            Log.e("see12", "time2" + time2);


                            try {
                                jsoncalls = json1st.getJSONArray("calls");
                                Log.e("spinneda", "jsoncalls" + jsoncalls);


//                                if (Utils.get_current_match_values() == true) {
//
////                                    timer = new Timer();
////                                    TimerTask task = new TimerTask() {
////                                        @Override
////                                        public void run() {
////                                            runOnUiThread(new Runnable() {
////
////                                                @Override
////                                                public void run() {
////
////                                                    String date = Utils.getDate();
////                                                    if (!get_date_value.equals(date)) {
////                                                        get_date_value = date;
//                                                        insertRecordPostpaid2(jsoncalls, "calls");
//
////                                                    } else {
////
////
////                                                    }
////
////                                                }
////                                            });
////                                        }
////                                    };
////                                    timer.schedule(task, 1000, 10000);
//
//
//                                }
                                for (int i = 0; i < jsoncalls.length(); i++) {
                                    ChainlistClass beanClass = new ChainlistClass();
                                    JSONObject jsonObject = jsoncalls.getJSONObject(i);
                                    share_type1 = "calls";


                                    beanClass.setType(share_type1);
                                    beanClass.setStrike_price(jsonObject.getString("strike"));
                                    beanClass.setPrice_value(jsonObject.getString("p"));
                                    beanClass.setChange_price_value(jsonObject.getString("c"));
                                    beanClass.setBid_value(jsonObject.getString("b"));
                                    beanClass.setAsk_value(jsonObject.getString("a"));
                                    beanClass.setVol_value(jsonObject.getString("vol"));
                                    beanClass.setOp_int_value(jsonObject.getString("oi"));
                                    beanClass.setTime(time);


                                    listoperatorlist2.add(beanClass);


//                                    }
                                    ShowDialogClass.hideProgressing();


                                }
                                //.............set adapter................//
                                set_adapter(listoperatorlist2);
                                ShowDialogClass.hideProgressing();

//
//                                fileOperation = true;
//
//                                // create new contents resource
//                                Drive.DriveApi.newDriveContents(mGoogleApiClient)
//                                        .setResultCallback(driveContentsCallback);


                            } catch (JSONException e1) {
                                e1.printStackTrace();


                            }
                            fileOperation = true;

                            Log.e("pooja", "time" + time);

                            JSONArray jsonputs = null;

                            try {
                                jsonputs = json1st.getJSONArray("puts");
                                for (int i = 0; i < jsonputs.length(); i++) {
                                    ChainlistClass beanClass = new ChainlistClass();
                                    final JSONObject jsonObject = jsonputs.getJSONObject(i);
                                    share_type = "puts";
                                    beanClass.setStrike_price(jsonObject.getString("strike"));
                                    beanClass.setPrice_value(jsonObject.getString("p"));
                                    beanClass.setChange_price_value(jsonObject.getString("c"));
                                    beanClass.setBid_value(jsonObject.getString("b"));
                                    beanClass.setAsk_value(jsonObject.getString("a"));
                                    beanClass.setVol_value(jsonObject.getString("vol"));
                                    beanClass.setOp_int_value(jsonObject.getString("oi"));
                                    beanClass.setTime(time);

                                    listoperatorlist.add(beanClass);


                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {

                                            if (Utils.get_current_match_values() == true) {
                                                insertRecordPostpaid(jsonObject, share_type);
                                            } else {

                                            }


                                        }
                                    }, 3000);


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


    public static void set_adapter(ArrayList<ChainlistClass> listoperatorlist2) {
        adapter2 = new graph_adapter2(ctx, GraphActivity.listoperatorlist2);

        mRecyclercalls.setAdapter(adapter2);


    }
//
//
//    public void startTimer(){
//       Timer t = new Timer();
//       TimerTask task = new TimerTask() {
//
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        String date=Utils.getDate();
//                        if (!get_date_value.equals(date)) {
//                            get_date_value = date;
//                        }
//                        else {
//
//                        }
//
//                    }
//                });
//            }
//        };
//        t.schedule(task ,1000, 10000);
//    }
//


    public static void insertRecordPostpaid(JSONObject jsonObject, String type) {


        String type_value_database;
        Log.e("pooja", "share_type" + type);
        Log.e("pooja", "date" + Utils.getDate());

        String time = Utils.getDate();


        ContentValues contentValues = new ContentValues();

        try {
            contentValues.put(KEY_TYPE, type);
            contentValues.put(KEY_STRIKE_PRICE, jsonObject.getString("strike"));
            contentValues.put(KEY_PRICE_VALUE, jsonObject.getString("p"));
            contentValues.put(KEY_CHANGE_PRICE_VALUE, jsonObject.getString("c"));
            contentValues.put(KEY_BIDVALUE, jsonObject.getString("b"));
            contentValues.put(KEY_ASK_VALUE, jsonObject.getString("a"));
            contentValues.put(KEY_VOL_VALUE, jsonObject.getString("vol"));
            contentValues.put(KEY_OP_INT_VALUE, jsonObject.getString("oi"));
            contentValues.put(KEY_DATE_TIME, time);
             sqLiteDatabase.insert(TABLE_NAME_PUTS, null, contentValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void insertRecordPostpaid2(JSONArray jsonObject1, String type) {


        String time = Utils.getDate();


        Log.e("gagan", "sena" + get_date_value);
        Log.e("see3", "sena2" + time);

        Log.e("see4", "type_value_database1oooo" + type_value_database);


        Log.e("see3", "sena3" + get_date_value);


        if (time.equals(get_date_value)) {

        } else {

            for (int i = 0; i < jsonObject1.length(); i++) {
                try {
                    JSONObject jsonObject = jsonObject1.getJSONObject(i);

                    ContentValues contentValues = new ContentValues();

                    contentValues.put(KEY_TYPE, type);
                    contentValues.put(KEY_STRIKE_PRICE, jsonObject.getString("strike"));
                    contentValues.put(KEY_PRICE_VALUE, jsonObject.getString("p"));
                    contentValues.put(KEY_CHANGE_PRICE_VALUE, jsonObject.getString("c"));
                    contentValues.put(KEY_BIDVALUE, jsonObject.getString("b"));
                    contentValues.put(KEY_ASK_VALUE, jsonObject.getString("a"));
                    contentValues.put(KEY_VOL_VALUE, jsonObject.getString("vol"));
                    contentValues.put(KEY_OP_INT_VALUE, jsonObject.getString("oi"));
                    contentValues.put(KEY_DATE_TIME, time);


                    sqLiteDatabase.insert(TABLE_NAME_CALLS, null, contentValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


//        try {
//
//
////            Log.e("result", String.valueOf(row));
//            Log.e("time", time);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


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


    public static void getLocalSoloQuestions() {

        listoperatorlist2.clear();
        Cursor cursor = sqLiteDatabase.query(sqLiteHelper.TABLE_NAME_CALLS,
                new String[]{KEY_ID,KEY_TYPE, KEY_STRIKE_PRICE, KEY_PRICE_VALUE, KEY_CHANGE_PRICE_VALUE, KEY_ASK_VALUE, KEY_BIDVALUE, KEY_VOL_VALUE, KEY_OP_INT_VALUE, KEY_DATE_TIME}, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            get_provider_data = new ChainlistClass();
            get_provider_data.setId(cursor.getColumnIndex(KEY_ID));
            get_provider_data.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            get_provider_data.setStrike_price(cursor.getString(cursor.getColumnIndex(KEY_STRIKE_PRICE)));
            get_provider_data.setPrice_value(cursor.getString(cursor.getColumnIndex(KEY_PRICE_VALUE)));
            get_provider_data.setChange_price_value(cursor.getString(cursor.getColumnIndex(KEY_CHANGE_PRICE_VALUE)));
            get_provider_data.setBid_value(cursor.getString(cursor.getColumnIndex(KEY_BIDVALUE)));
            get_provider_data.setAsk_value(cursor.getString(cursor.getColumnIndex(KEY_ASK_VALUE)));
            get_provider_data.setVol_value(cursor.getString(cursor.getColumnIndex(KEY_VOL_VALUE)));
            get_provider_data.setOp_int_value(cursor.getString(cursor.getColumnIndex(KEY_OP_INT_VALUE)));
            get_provider_data.setTime(cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME)));
            listoperatorlist2.add(get_provider_data);
            cursor.moveToNext();


            String log = " Type: " + get_provider_data.getType() + " ,Strike price : " + get_provider_data.getStrike_price() + " ,price: " + get_provider_data.getPrice_value() +
                    " ,change price value: " + get_provider_data.getChange_price_value() + " ,bid value: " + get_provider_data.getBid_value() + " ,ask value: " + get_provider_data.getAsk_value() +
                    " ,vol: " + get_provider_data.getAsk_value() + " ,op: " + get_provider_data.getOp_int_value() + " ,time: " + get_provider_data.getTime();
            // Writing Contacts to log
            Log.e("Name1: ", log);
//                    username.setText(get_provider_data.getName());


        }
        cursor.close();
        //       sqLiteDatabase.delete(MySQLiteHelper.TABLE_COUPLES,null,null);

    }
//
//    static ChainlistClass get_provider_data() {
//        String type_value_database;
//
//
//        Log.e("see2", "share_type" + share_type);
//        listoperatorlist2.clear();
//        Cursor cursor = sqLiteDatabase.query(sqLiteHelper.TABLE_NAME_CALLS, null, null, null, null, null, null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            ChainlistClass get_provider_data = new ChainlistClass();
//            get_provider_data.setId(cursor.getColumnIndex(KEY_ID));
//            get_provider_data.setType(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_TYPE)));
//            get_provider_data.setStrike_price(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_STRIKE_PRICE)));
//            get_provider_data.setPrice_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_PRICE_VALUE)));
//            get_provider_data.setChange_price_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_CHANGE_PRICE_VALUE)));
//            get_provider_data.setBid_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIDVALUE)));
//            get_provider_data.setAsk_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ASK_VALUE)));
//            get_provider_data.setVol_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_VOL_VALUE)));
//            get_provider_data.setOp_int_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_OP_INT_VALUE)));
//            get_provider_data.setTime(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)));
//            listoperatorlist2.add(get_provider_data);
//            cursor.moveToNext();
//
//
////            String log = " Type: " + get_provider_data.getType() + " ,Strike price : " + get_provider_data.getStrike_price() + " ,price: " + get_provider_data.getPrice_value() +
////                    " ,change price value: " + get_provider_data.getChange_price_value() + " ,bid value: " + get_provider_data.getBid_value() + " ,ask value: " + get_provider_data.getAsk_value() +
////                    " ,vol: " + get_provider_data.getAsk_value() + " ,op: " + get_provider_data.getOp_int_value() + " ,time: " + get_provider_data.getTime();
////            // Writing Contacts to log
////            Log.e("Name: ", log);
////                    username.setText(get_provider_data.getName());
//
//
//        }
//        cursor.close();
//        //       sqLiteDatabase.delete(MySQLiteHelper.TABLE_COUPLES,null,null);
//        return get_provider_data();
//    }


    public static void getLocalSoloQuestions2() {

        String type_value_database;

        String date = "2017/07/21 21:44:04";
        Log.e("see2", "share_type" + share_type);
        listoperatorlist.clear();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_PUTS, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ChainlistClass get_provider_data = new ChainlistClass();
            get_provider_data.setId(cursor.getColumnIndex(KEY_ID));
            get_provider_data.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            get_provider_data.setStrike_price(cursor.getString(cursor.getColumnIndex(KEY_STRIKE_PRICE)));
            get_provider_data.setPrice_value(cursor.getString(cursor.getColumnIndex(KEY_PRICE_VALUE)));
            get_provider_data.setChange_price_value(cursor.getString(cursor.getColumnIndex(KEY_CHANGE_PRICE_VALUE)));
            get_provider_data.setBid_value(cursor.getString(cursor.getColumnIndex(KEY_BIDVALUE)));
            get_provider_data.setAsk_value(cursor.getString(cursor.getColumnIndex(KEY_ASK_VALUE)));
            get_provider_data.setVol_value(cursor.getString(cursor.getColumnIndex(KEY_VOL_VALUE)));
            get_provider_data.setOp_int_value(cursor.getString(cursor.getColumnIndex(KEY_OP_INT_VALUE)));
            get_provider_data.setTime(cursor.getString(cursor.getColumnIndex(KEY_DATE_TIME)));


//            if (date.equals(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)))) {
            listoperatorlist.add(get_provider_data);

//            }
            cursor.moveToNext();


            String log = " Type: " + get_provider_data.getType() + " ,Strike price : " + get_provider_data.getStrike_price() + " ,price: " + get_provider_data.getPrice_value() +
                    " ,change price value: " + get_provider_data.getChange_price_value() + " ,bid value: " + get_provider_data.getBid_value() + " ,ask value: " + get_provider_data.getAsk_value() +
                    " ,vol: " + get_provider_data.getAsk_value() + " ,op: " + get_provider_data.getOp_int_value() + " ,time: " + get_provider_data.getTime();
            // Writing Contacts to log
            Log.e("Name: ", log);
//                    username.setText(get_provider_data.getName());


        }
        cursor.close();
        //       sqLiteDatabase.delete(MySQLiteHelper.TABLE_COUPLES,null,null);

    }
//
//
//    private void get_calles_values(Writer writer2) {
//        Log.e("dddd", "ddddd" + share_type1);
//
//        try {
//            String log = null;
//            listoperatorlist2.clear();
//            Cursor cursor = sqLiteDatabase.query(TABLE_NAME_CALLS, null, null, null, null, null, null);
//            cursor.moveToFirst();
//
//            while (!cursor.isAfterLast()) {
//                ChainlistClass get_provider_data = new ChainlistClass();
//                get_provider_data.setId(cursor.getColumnIndex(KEY_ID));
//                get_provider_data.setType(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_TYPE)));
//                get_provider_data.setStrike_price(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_STRIKE_PRICE)));
//                get_provider_data.setPrice_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_PRICE_VALUE)));
//                get_provider_data.setChange_price_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_CHANGE_PRICE_VALUE)));
//                get_provider_data.setBid_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIDVALUE)));
//                get_provider_data.setAsk_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ASK_VALUE)));
//                get_provider_data.setVol_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_VOL_VALUE)));
//                get_provider_data.setOp_int_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_OP_INT_VALUE)));
//                get_provider_data.setTime(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)));
//                listoperatorlist2.add(get_provider_data);
//                cursor.moveToNext();
//
//
//                log = " Type: " + get_provider_data.getType() + " ,Strike price : " + get_provider_data.getStrike_price() + " ,price: " + get_provider_data.getPrice_value() +
//                        " ,change price value: " + get_provider_data.getChange_price_value() + " ,bid value: " + get_provider_data.getBid_value() + " ,ask value: " + get_provider_data.getAsk_value() +
//                        " ,vol: " + get_provider_data.getAsk_value() + " ,op: " + get_provider_data.getOp_int_value() + " ,time: " + get_provider_data.getTime();
//
//                Log.e("seema2", "log" + log);
//
//                writer2.write(log);
//
//            }
//
//
//            writer2.close();
//
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//
//    }

//    private void get_puts_values(Writer writer) {
//        Log.e("dddd", "ddddd" + share_type);
//        try {
//            String log = null;
//            listoperatorlist.clear();
//            Cursor cursor = sqLiteDatabase.query(sqLiteHelper.TABLE_NAME_PUTS, null, null, null, null, null, null);
//            cursor.moveToFirst();
//
//            while (!cursor.isAfterLast()) {
//                ChainlistClass get_provider_data = new ChainlistClass();
//                get_provider_data.setId(cursor.getColumnIndex(KEY_ID));
//                get_provider_data.setType(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_TYPE)));
//                get_provider_data.setStrike_price(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_STRIKE_PRICE)));
//                get_provider_data.setPrice_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_PRICE_VALUE)));
//                get_provider_data.setChange_price_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_CHANGE_PRICE_VALUE)));
//                get_provider_data.setBid_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_BIDVALUE)));
//                get_provider_data.setAsk_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_ASK_VALUE)));
//                get_provider_data.setVol_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_VOL_VALUE)));
//                get_provider_data.setOp_int_value(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_OP_INT_VALUE)));
//                get_provider_data.setTime(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)));
//                listoperatorlist.add(get_provider_data);
//                cursor.moveToNext();
//
//
//                log = " Type: " + get_provider_data.getType() + " ,Strike price : " + get_provider_data.getStrike_price() + " ,price: " + get_provider_data.getPrice_value() +
//                        " ,change price value: " + get_provider_data.getChange_price_value() + " ,bid value: " + get_provider_data.getBid_value() + " ,ask value: " + get_provider_data.getAsk_value() +
//                        " ,vol: " + get_provider_data.getAsk_value() + " ,op: " + get_provider_data.getOp_int_value() + " ,time: " + get_provider_data.getTime();
//                Log.e("seema", "log" + log);
//
//                writer.write(log);
//
//            }
//
//
//            writer.close();
//
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//    }
//
//    /**
//     * Handle result of Created file
//     */
//    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
//            ResultCallback<DriveFolder.DriveFileResult>() {
//                @Override
//                public void onResult(DriveFolder.DriveFileResult result) {
//                    if (result.getStatus().isSuccess()) {
//
//                        Toast.makeText(getApplicationContext(), "file created: " + "" +
//                                result.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();
//
//                    }
//
//                    return;
//
//                }
//            };
//
//    /**
//     * Handle Response of selected file
//     *
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(final int requestCode,
//                                    final int resultCode, final Intent data) {
//        switch (requestCode) {
//
//            case REQUEST_CODE_OPENER:
//
//                if (resultCode == RESULT_OK) {
//
//                    mFileId = (DriveId) data.getParcelableExtra(
//                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
//
//                    Log.e("file id", mFileId.getResourceId() + "");
//
//                    String url = "https://drive.google.com/open?id=" + mFileId.getResourceId();
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                }
//
//                break;
//
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//                break;
//        }
//    }

//    public Cursor getAllData() {
//
//        String date_value = "2017/07/18";
//
//        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
////        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//        String query = "select * from " + sqLiteHelper.TABLE_NAME_PUTS + " where "
//                + KEY_DATE_TIME + "=" + date_value;
//
//        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//        int length = cursor.getCount();
//        Log.e("seema", "length" + length);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//
//
//            for (int i = 0; i < listoperatorlist2.size(); i++) {
//                ChainlistClass data = new ChainlistClass();
//                data = new ChainlistClass(cursor.getString(0));
//
//            }
//
//
//            cursor.getString(0);
//            // return contact
//
//
//        }
//        return cursor;
//
//    }


    // Getting All Contacts
//    public List<ChainlistClass> getAllDatas() {
//        List<ChainlistClass> dataList = new ArrayList<ChainlistClass>();
//        // Select All Query
//        String date_value = "2017/07/21";
//        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
//
//        String query = "select * from " + sqLiteHelper.TABLE_NAME_PUTS + " where "
//                + KEY_DATE_TIME + "=" + date_value;
//
//        Log.e("seema", "query" + query);
//
//        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                ChainlistClass get_provider_data = new ChainlistClass();
//                get_provider_data.setType(cursor.getString(1));
//                get_provider_data.setStrike_price(cursor.getString(2));
//                get_provider_data.setPrice_value(cursor.getString(3));
//                get_provider_data.setChange_price_value(cursor.getString(4));
//                get_provider_data.setBid_value(cursor.getString(5));
//                get_provider_data.setAsk_value(cursor.getString(6));
//                get_provider_data.setVol_value(cursor.getString(7));
//                get_provider_data.setOp_int_value(cursor.getString(8));
//                get_provider_data.setTime(cursor.getString(9));
//                dataList.add(get_provider_data);
//            } while (cursor.moveToNext());
//        }
//
//        // return contact list
//        return dataList;
//    }
//    public List<ChainlistClass> getAllContacts(String current_date) {
//        List<ChainlistClass> contactList = new ArrayList<ChainlistClass>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME_CALLS;
//
//        ;
//        Cursor cursor = null;
//        try {
//            cursor = sqLiteDatabase.query(CloneAlgodatabase.TABLE_NAME_CALLS,
//                    new String[]{KEY_NEWSID, KEY_TYPE,KEY_STRIKE_PRICE,KEY_PRICE_VALUE,KEY_CHANGE_PRICE_VALUE,KEY_ASK_VALUE, KEY_BIDVALUE,KEY_VOL_VALUE,KEY_OP_INT_VALUE,KEY_DATE},null, null, null, null, null);
////        Cursor cursor = db.rawQuery(selectQuery, null);
//
//            // looping through all rows and adding to list
//
//            int numRows = cursor.getCount();
//            cursor.moveToFirst();
////        if (cursor.moveToFirst()) {
////            do {
////                ChainlistClass get_provider_data = new ChainlistClass();
////                get_provider_data.setId(Integer.parseInt(cursor.getString(0)));
////                get_provider_data.setType(cursor.getString(1));
////                get_provider_data.setStrike_price(cursor.getString(2));
////                get_provider_data.setPrice_value(cursor.getString(3));
////                get_provider_data.setChange_price_value(cursor.getString(4));
////                get_provider_data.setBid_value(cursor.getString(5));
////                get_provider_data.setAsk_value(cursor.getString(6));
////                get_provider_data.setVol_value(cursor.getString(7));
////                get_provider_data.setOp_int_value(cursor.getString(8));
////                get_provider_data.setTime(cursor.getString(9));
//////                if (date_select.equals(cursor.getString(cursor.getColumnIndex(SQLiteHelper.KEY_DATE_TIME)))) {
////                contactList.add(get_provider_data);
//
//
//            for (int i = 0; i < numRows; ++i) {
//                ChainlistClass get_provider_data = new ChainlistClass();
//
////                    if (c.getString(4).equalsIgnoreCase("0")) {
////
////                        Log.e("data", "data" + c.getString(0));
////                        ContentValues args = new ContentValues();
////                        args.put(KEY_STATUS, "1");
////
////                        db.update(DATABASE_TABLE, args, KEY_NEWSID + "=" + c.getString(0), null);
////                    }
//
//                get_provider_data.setId(Integer.parseInt(cursor.getString(0)));
//                get_provider_data.setType(cursor.getString(1));
//                get_provider_data.setStrike_price(cursor.getString(2));
//                get_provider_data.setPrice_value(cursor.getString(3));
//                get_provider_data.setChange_price_value(cursor.getString(4));
//                get_provider_data.setBid_value(cursor.getString(5));
//                get_provider_data.setAsk_value(cursor.getString(6));
//                get_provider_data.setVol_value(cursor.getString(7));
//                get_provider_data.setOp_int_value(cursor.getString(8));
//                get_provider_data.setTime(cursor.getString(9));
//
////				mnewsdata.setMessage_delivertime(c.getString(0));
//
////				mnewsdata.setUserimg(c.getString(0));
//
//                contactList.add(get_provider_data);
//
//                cursor.moveToNext();
//            }
//
//        } catch (SQLException e) {
//
//        } finally {
//            if (cursor != null && cursor.isClosed()) {
//
//                cursor.close();
//            }
//        }
//
//        return contactList;
//    }
}
