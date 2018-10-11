package com.ninepmonline.ninepmdriver.helper;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.ninepmonline.ninepmdriver.requests.products;
import java.util.ArrayList;

public class UpcommingsBean implements Parcelable {
    public static final Creator<UpcommingsBean> CREATOR = new Creator<UpcommingsBean>() {
        public UpcommingsBean createFromParcel(Parcel in) {
            return new UpcommingsBean(in);
        }

        public UpcommingsBean[] newArray(int size) {
            return new UpcommingsBean[size];
        }
    };
    String assign_count;
    String created_at;
    String customer_name;
    String is_reviewed = "";
    String logo_url;
    String order_id;
    String product_count;
    ArrayList<products> products;
    String shipping_address_1;
    String shipping_address_2;
    String shipping_city;
    String shipping_firstname;
    String shipping_lastname;
    String shipping_lat;
    String shipping_long;
    String shipping_postcode;
    String status;
    String store_address;
    String store_lat;
    String store_long;
    String store_name;
    String telephone;
    String total;

    public UpcommingsBean(String order_id, String customer_name, String store_address, String store_lat, String store_long, String logo_url, String total, String store_name, String status, String telephone, String shipping_firstname, String shipping_lastname, String shipping_address_1, String shipping_address_2, String shipping_city, String shipping_postcode, String assign_count, String product_count, String created_at, String shipping_lat, String shipping_long, String is_reviewed, ArrayList<products> products) {
        this.order_id = order_id;
        this.customer_name = customer_name;
        this.store_address = store_address;
        this.store_lat = store_lat;
        this.store_long = store_long;
        this.logo_url = logo_url;
        this.total = total;
        this.store_name = store_name;
        this.status = status;
        this.telephone = telephone;
        this.shipping_firstname = shipping_firstname;
        this.shipping_lastname = shipping_lastname;
        this.shipping_address_1 = shipping_address_1;
        this.shipping_address_2 = shipping_address_2;
        this.shipping_city = shipping_city;
        this.shipping_postcode = shipping_postcode;
        this.assign_count = assign_count;
        this.product_count = product_count;
        this.created_at = created_at;
        this.shipping_lat = shipping_lat;
        this.shipping_long = shipping_long;
        this.is_reviewed = is_reviewed;
        this.products = products;
    }

    public ArrayList<products> getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<products> products) {
        this.products = products;
    }

    public String getIs_reviewed() {
        return this.is_reviewed;
    }

    public void setIs_reviewed(String is_reviewed) {
        this.is_reviewed = is_reviewed;
    }

    public String getAssign_count() {
        return this.assign_count;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public String getCustomer_name() {
        return this.customer_name;
    }

    public String getOrder_id() {
        return this.order_id;
    }

    public String getProduct_count() {
        return this.product_count;
    }

    public String getShipping_address_1() {
        return this.shipping_address_1;
    }

    public String getShipping_address_2() {
        return this.shipping_address_2;
    }

    public String getShipping_city() {
        return this.shipping_city;
    }

    public String getShipping_firstname() {
        return this.shipping_firstname;
    }

    public String getShipping_lastname() {
        return this.shipping_lastname;
    }

    public String getShipping_postcode() {
        return this.shipping_postcode;
    }

    public String getStatus() {
        return this.status;
    }

    public String getStore_address() {
        return this.store_address;
    }

    public String getLogo_url() {
        return this.logo_url;
    }

    public String getStore_long() {
        return this.store_long;
    }

    public String getStore_lat() {
        return this.store_lat;
    }

    public String getShipping_long() {
        return this.shipping_long;
    }

    public String getShipping_lat() {
        return this.shipping_lat;
    }

    public String getStore_name() {
        return this.store_name;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getTotal() {
        return this.total;
    }

    public void setAssign_count(String assign_count) {
        this.assign_count = assign_count;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public void setShipping_address_1(String shipping_address_1) {
        this.shipping_address_1 = shipping_address_1;
    }

    public void setShipping_address_2(String shipping_address_2) {
        this.shipping_address_2 = shipping_address_2;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public void setShipping_firstname(String shipping_firstname) {
        this.shipping_firstname = shipping_firstname;
    }

    public void setShipping_lastname(String shipping_lastname) {
        this.shipping_lastname = shipping_lastname;
    }

    public void setShipping_postcode(String shipping_postcode) {
        this.shipping_postcode = shipping_postcode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStore_address(String store_address) {
        this.store_address = store_address;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public void setShipping_lat(String shipping_lat) {
        this.shipping_lat = shipping_lat;
    }

    public void setShipping_long(String shipping_long) {
        this.shipping_long = shipping_long;
    }

    public void setStore_lat(String store_lat) {
        this.store_lat = store_lat;
    }

    public void setStore_long(String store_long) {
        this.store_long = store_long;
    }

    public static Creator<UpcommingsBean> getCreator() {
        return CREATOR;
    }

    public UpcommingsBean(Parcel in) {
        this.order_id = in.readString();
        this.customer_name = in.readString();
        this.store_address = in.readString();
        this.store_lat = in.readString();
        this.store_long = in.readString();
        this.logo_url = in.readString();
        this.total = in.readString();
        this.store_name = in.readString();
        this.status = in.readString();
        this.telephone = in.readString();
        this.shipping_firstname = in.readString();
        this.shipping_lastname = in.readString();
        this.shipping_address_1 = in.readString();
        this.shipping_address_2 = in.readString();
        this.shipping_city = in.readString();
        this.shipping_postcode = in.readString();
        this.assign_count = in.readString();
        this.product_count = in.readString();
        this.created_at = in.readString();
        this.shipping_lat = in.readString();
        this.shipping_long = in.readString();
        this.is_reviewed = in.readString();
        this.products = in.readArrayList(products.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.customer_name);
        dest.writeString(this.store_address);
        dest.writeString(this.store_lat);
        dest.writeString(this.store_long);
        dest.writeString(this.logo_url);
        dest.writeString(this.total);
        dest.writeString(this.store_name);
        dest.writeString(this.status);
        dest.writeString(this.telephone);
        dest.writeString(this.shipping_firstname);
        dest.writeString(this.shipping_lastname);
        dest.writeString(this.shipping_address_1);
        dest.writeString(this.shipping_address_2);
        dest.writeString(this.shipping_city);
        dest.writeString(this.shipping_postcode);
        dest.writeString(this.assign_count);
        dest.writeString(this.product_count);
        dest.writeString(this.created_at);
        dest.writeString(this.shipping_lat);
        dest.writeString(this.shipping_long);
        dest.writeString(this.is_reviewed);
        dest.writeList(this.products);
    }
}
