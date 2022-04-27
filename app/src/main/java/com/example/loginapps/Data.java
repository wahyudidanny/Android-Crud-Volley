package com.example.loginapps;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Data implements Parcelable {

    private String id;
    private String item_code;
    private String item_name;
    private double item_price;
    private int item_stock;

    Data(String id,String item_code,String item_name,double item_price,int item_stock){
        this.setId(id);
        this.setItem_code(item_code);
        this.setItem_name(item_name);
        this.setItem_price(item_price);
        this.setItem_stock(item_stock);
    }


    protected Data(Parcel in) {
        id = in.readString();
        item_code = in.readString();
        item_name = in.readString();
        item_price = in.readDouble();
        item_stock = in.readInt();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }


    public int getStock() {
        return item_stock;
    }

    public void setItem_stock(int stock) {
        this.item_stock = stock;
    }

    public double getItemPrice() {
        return item_price;
    }

    public void setItem_price(double price) {
        this.item_price = price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(item_code);
        parcel.writeString(item_name);
        parcel.writeDouble(item_price);
        parcel.writeInt(item_stock);
    }
}
