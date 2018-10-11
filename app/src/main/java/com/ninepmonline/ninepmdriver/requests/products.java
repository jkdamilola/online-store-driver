package com.ninepmonline.ninepmdriver.requests;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class products implements Parcelable {
    public static final Creator<products> CREATOR = new Creator<products>() {
        public products createFromParcel(Parcel in) {
            return new products(in);
        }

        public products[] newArray(int size) {
            return new products[size];
        }
    };
    String image;
    boolean isselect;
    String name;
    String orderi;
    String price;
    String product_id;
    String quantity;
    String total;

    public products(String product_id, String name, String quantity, String price, String total, String image, String orderi, boolean isselect) {
        this.product_id = product_id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.image = image;
        this.orderi = orderi;
        this.isselect = isselect;
    }

    public static Creator<products> getCreator() {
        return CREATOR;
    }

    public products(Parcel in) {
        this.product_id = in.readString();
        this.name = in.readString();
        this.quantity = in.readString();
        this.price = in.readString();
        this.total = in.readString();
        this.image = in.readString();
        this.orderi = in.readString();
        this.isselect = in.readString() != null;
    }

    public int describeContents() {
        return 0;
    }

    public String getImage() {
        return this.image;
    }

    public String getProduct_id() {
        return this.product_id;
    }

    public String getPrice() {
        return this.price;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public String getTotal() {
        return this.total;
    }

    public String getName() {
        return this.name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getIsselect() {
        return this.isselect;
    }

    public String getorderi() {
        return this.orderi;
    }

    public void setorderi(String orderi) {
        this.orderi = orderi;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.name);
        dest.writeString(this.quantity);
        dest.writeString(this.price);
        dest.writeString(this.total);
        dest.writeString(this.image);
        dest.writeString(this.orderi);
        dest.writeByte((byte) (this.isselect ? 1 : 0));
    }
}
