package com.Fleming.sharemarket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Fleming.sharemarket.Activities.MainActivity;
import com.Fleming.sharemarket.common.BeanClass;
import com.toshiba.sharemarket.R;

import java.util.ArrayList;

/**
 * Created by admin on 07-06-2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<BeanClass> listoperatorlist;
    Context activity;
    String Single_share_name;

    public MainAdapter(MainActivity activity, ArrayList<BeanClass> listoperatorlist) {
        this.listoperatorlist = listoperatorlist;
        this.activity = activity;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView share_name, price, change_price, share_type;
        LinearLayout share_details_line;

        public ViewHolder(View convertView) {
            super(convertView);
            //....................initilisation the objects.......................//
            share_name = (TextView) convertView.findViewById(R.id.share_name);
            price = (TextView) convertView.findViewById(R.id.share_price);
            change_price = (TextView) convertView.findViewById(R.id.change_share_price);
            share_type = (TextView) convertView.findViewById(R.id.type_name);
//            share_details_line = (LinearLayout) convertView.findViewById(R.id.share_details_line);
//            //......................click listener on single line................//
//            share_details_line.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent i = new Intent(activity, SingleShareDetails.class);
//                    i.putExtra("Share_name", Single_share_name);
//                    activity.startActivity(i);
//                }
//            });


        }


    }


    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mainadapter, parent, false);

        return new MainAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanClass movie = listoperatorlist.get(position);
        Single_share_name = movie.getShare_name();
        holder.share_name.setText(movie.getShare_name());
        holder.share_type.setText(movie.getShare_type());
        holder.price.setText("$" + movie.getPrice());
        holder.change_price.setText(movie.getShare_Change_price() + " " + "(" + " " + movie.getShare_Change_price_percentage() + " " + "%" + ")");
        ;


    }


    @Override
    public int getItemCount() {
        return listoperatorlist.size();
    }
}

