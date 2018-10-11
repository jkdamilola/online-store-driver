package com.ninepmonline.ninepmdriver.WebServices;

import com.ninepmonline.ninepmdriver.helper.Constants;
public enum URL {
    BASEURL("https://9pmonline.com"),
    LOGIN(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("/api/user/generate_auth_cookie/?username=%1$s&password=%2$s").toString()),
    UPDATE_PASSWORDS(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/account/change-password").toString()),
    GETUSER(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/user").toString()),
    UPDATEACCOUNT(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/user").toString()),
    LOGOUT(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/account/logout").toString()),
    GETUPCOMINGS(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("/wp-json/wc/v2/orders?status=processing&consumer_key=" + Constants.CONSUMER_KEY + "&consumer_secret=" + Constants.CONSUMER_SECRET).toString()),
    ACCEPTORDERS(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("/wp-json/wc/v2/orders/%1$s?consumer_key=" + Constants.CONSUMER_KEY + "&consumer_secret=" + Constants.CONSUMER_SECRET).toString()),
    COMPLETES(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/order/mark-as-complete").toString()),
    UPCOMMINGSR(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/driver/reject-order-request").toString()),
    GETACCEPTEDS(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("/wp-json/wc/v2/orders?status=shipped&consumer_key=" + Constants.CONSUMER_KEY + "&consumer_secret=" + Constants.CONSUMER_SECRET).toString()),
    VERIFICATIONCODES(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/account/reset-password").toString()),
    CHECKEMAIL(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/account/check-user-type-by-mail").toString()),
    REVIEW_ORDER(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/driver/review-order").toString()),
    FORGOTPASSWORD(new StringBuilder(String.valueOf(BASEURL.getUrl())).append("web/index.php/v1/account/reset-password-request").toString());
    
    private String callingURL;

    private URL(String url) {
        this.callingURL = url;
    }

    public String getUrl() {
        return this.callingURL;
    }
}
