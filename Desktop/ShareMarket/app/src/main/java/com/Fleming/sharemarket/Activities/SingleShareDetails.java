package com.Fleming.sharemarket.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SingleShareDetails extends AppCompatActivity {

    private List<String> fileList;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlesharedetail);
        fileList = new ArrayList<>();

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){

        getFileList();
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_rv);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


    }
    private void getFileList(){

        File dir = new File(((SRApplication)getApplication()).getDirectory());
        if (dir.exists()){

            for(String file : dir.list()){
                file = ((SRApplication)getApplication()).getDirectory() + file;

                Log.e("xpwaalet","fileList"+fileList);
                fileList.add(file);
            }

            int datePos = ((SRApplication)getApplication()).getDirectory().length();
            final int start = datePos;
            final int end = datePos + 13;
            final SimpleDateFormat format = new SimpleDateFormat("yy MM dd - HH mm ss");

            Collections.sort(fileList, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {

                    String fileName1 = lhs.substring(start, end);
                    String fileName2 = rhs.substring(start,end);
                    try {

                        Date date1 = format.parse(fileName1);
                        Date date2 = format.parse(fileName2);

                        if (date1.getTime() > date2.getTime()){
                            return -1;
                        } else {
                            return 1;
                        }

                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        fileList.clear();
        getFileList();
    }

}
