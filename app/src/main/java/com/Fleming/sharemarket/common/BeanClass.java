package com.Fleming.sharemarket.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by admin on 07-06-2017.
 */

public class BeanClass {

    String Share_name, Share_Change_price, Share_Change_price_percentage, price, share_type;



    public String getShare_type() {
        return share_type;
    }

    public void setShare_type(String share_type1) {
        this.share_type = share_type1;
    }

    public String getShare_name() {
        return Share_name;
    }

    public void setShare_name(String share_name) {
        this.Share_name = share_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price1) {
        this.price = price1;
    }

    public String getShare_Change_price() {
        return Share_Change_price;
    }

    public void setShare_Change_price(String share_Change_price) {
        this.Share_Change_price = share_Change_price;
    }


    public String getShare_Change_price_percentage() {
        return Share_Change_price_percentage;
    }

    public void setShare_Change_price_percentage(String share_Change_price_percentage) {
        this.Share_Change_price_percentage = share_Change_price_percentage;
    }

    public static class ConnectionDetector {
        private Context _context;

        public ConnectionDetector(Context context) {
            this._context = context;
        }

        public boolean isConnectingToInternet() {
            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }

            }
            return false;
        }
    }
}
