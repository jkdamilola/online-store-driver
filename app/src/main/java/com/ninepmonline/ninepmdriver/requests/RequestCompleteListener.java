package com.ninepmonline.ninepmdriver.requests;

public interface RequestCompleteListener<T> {
    void onTaskComplete(String str, T t);
}
