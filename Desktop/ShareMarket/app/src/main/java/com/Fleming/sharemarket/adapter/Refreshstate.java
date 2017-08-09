package com.Fleming.sharemarket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Fleming.sharemarket.common.ExpirationsClass;
import com.Fleming.sharemarket.common.Utils;
import com.toshiba.sharemarket.R;

import java.util.ArrayList;



public class Refreshstate extends ArrayAdapter<String> {

    LayoutInflater flater;

    public Refreshstate(Context context, int textviewId, ArrayList<String> list) {

        super(context, textviewId, list);
//        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        final String rowItem = getItem(position);


        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.refresh, null, false);

            holder.date_name = (TextView) rowview.findViewById(R.id.month);
            holder.expiration_details = (LinearLayout) rowview.findViewById(R.id.share_details_line2);

            rowview.setTag(holder);
        } else {

            Log.e("refrebh", "rowItem" + rowItem);
            holder = (viewHolder) rowview.getTag();
        }
        holder.date_name.setText(rowItem);
//        holder.expiration_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("dddd","dsbdjhhhad");
//
////             String   Expiration=  AppUrl.OPTION_CHAIN_URL+"AAPL"+"&expd="+rowItem.getMonth()+"&expm="+rowItem.getMonth()+"&expy="+rowItem.getMonth()+"&output=json";
////
////                get_expiration_date(Expiration);
//
//            }
//        });
        return rowview;
    }

    private class viewHolder {
        TextView date_name;
        LinearLayout expiration_details;
    }
}