package com.Fleming.sharemarket.common;

import org.json.JSONObject;

/**
 * Created by admin on 09-06-2017.
 */

public class ChainlistClass {
    String strike_price, price_value, change_price_value, bid_value, ask_value, vol_value, op_int_value, time, type;
    int _id;
    String jsonObject;

    // Empty constructor
    public ChainlistClass() {

    }

    public ChainlistClass(String strike_price,String op_int_value) {
        this.strike_price = strike_price;
        this.op_int_value = op_int_value;

    }

    public ChainlistClass( String strike_price, String price_value ,String change_price_value, String bid_value,
                          String ask_value,
                          String vol_value,String op_int_value,String time){

        this.strike_price = strike_price;
        this.price_value = price_value;
        this.change_price_value = change_price_value;
        this.bid_value = bid_value;
        this.ask_value = ask_value;
        this.vol_value = vol_value;
        this.op_int_value = op_int_value;
        this.time = time;
    }

    // constructor
    public ChainlistClass(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getStrike_price() {
        return strike_price;
    }

    public void setStrike_price(String strike_price1) {
        this.strike_price = strike_price1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type1) {
        this.type = type1;
    }

    public int getId() {
        return this._id;
    }

    // setting id
    public void setId(int id) {
        this._id = id;
    }


    public String getPrice_value() {
        return price_value;
    }

    public void setPrice_value(String price_value1) {
        this.price_value = price_value1;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time1) {
        this.time = time1;
    }

    public String getChange_price_value() {
        return change_price_value;
    }

    public void setChange_price_value(String change_price_value1) {
        this.change_price_value = change_price_value1;
    }

    public String getBid_value() {
        return bid_value;
    }

    public void setBid_value(String bid_value1) {
        this.bid_value = bid_value1;
    }

    public String getAsk_value() {
        return ask_value;
    }

    public void setAsk_value(String ask_value1) {
        this.ask_value = ask_value1;
    }

    public String getVol_value() {
        return vol_value;
    }

    public void setVol_value(String vol_value1) {
        this.vol_value = vol_value1;
    }

    public String getOp_int_value() {
        return op_int_value;
    }

    public void setOp_int_value(String op_int_value1) {
        this.op_int_value = op_int_value1;
    }

}
