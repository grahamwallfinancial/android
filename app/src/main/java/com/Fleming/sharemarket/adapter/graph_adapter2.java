package com.Fleming.sharemarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Fleming.sharemarket.common.ChainlistClass;
import com.toshiba.sharemarket.R;

import java.util.ArrayList;

/**
 * Created by admin on 09-06-2017.
 */

public class graph_adapter2 extends RecyclerView.Adapter<graph_adapter2.ViewHolder> {

    private ArrayList<ChainlistClass> listoperatorlist;
    Context activity;
    String Single_share_name;

    public graph_adapter2(Context activity, ArrayList<ChainlistClass> listoperatorlist) {
        this.listoperatorlist = listoperatorlist;
        this.activity = activity;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView strike_value, price_value, change_price_value, bid_value, ask_value, vol_value, op_int_value;

        public ViewHolder(View convertView) {
            super(convertView);
            //....................initilisation the objects.......................//
            strike_value = (TextView) convertView.findViewById(R.id.strike_value);
            price_value = (TextView) convertView.findViewById(R.id.price_value);
            change_price_value = (TextView) convertView.findViewById(R.id.chnage_value);
            bid_value = (TextView) convertView.findViewById(R.id.bid_value);
            ask_value = (TextView) convertView.findViewById(R.id.ask_value);
            vol_value = (TextView) convertView.findViewById(R.id.vol_value);
            op_int_value = (TextView) convertView.findViewById(R.id.opint_value);

        }
    }

    @Override
    public graph_adapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.optionchainadapter, parent, false);
        return new graph_adapter2.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChainlistClass movie = listoperatorlist.get(position);
//        Single_share_name = movie.getShare_name();
        holder.strike_value.setText(movie.getStrike_price());
        holder.price_value.setText(movie.getPrice_value());
        holder.change_price_value.setText(movie.getChange_price_value());
        holder.bid_value.setText(movie.getBid_value());
        holder.ask_value.setText(movie.getAsk_value());
        holder.vol_value.setText(movie.getVol_value());
        holder.op_int_value.setText(movie.getOp_int_value());
//        holder.share_type.setText(movie.getShare_type());
//        holder.price.setText("$" + movie.getPrice());
//        holder.change_price.setText(movie.getShare_Change_price() + " " + "(" + " " + movie.getShare_Change_price_percentage() + " " + "%" + ")");
        ;


    }


    @Override
    public int getItemCount() {
        return listoperatorlist.size();
    }
}
