package com.ninepmonline.ninepmdriver.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.ninepmonline.ninepmdriver.NavigationActivity;
import com.ninepmonline.ninepmdriver.R;

public class Help extends Fragment {
    private ImageView iv_menu;
    ProgressBar progressBar1 = null;
    String url = "";
    private View view;
    private WebView webview = null;

    private class MyWebViewClient extends WebChromeClient {
        private MyWebViewClient() {
        }

        public void onProgressChanged(WebView view, int newProgress) {
            Help.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.help, container, false);
        this.webview = (WebView) this.view.findViewById(R.id.webView1);
        this.progressBar1 = (ProgressBar) this.view.findViewById(R.id.progressBar1);
        this.iv_menu = (ImageView) this.view.findViewById(R.id.iv_menu);
        this.iv_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationActivity.resideMenu.openMenu(0);
            }
        });
        this.progressBar1.setMax(100);
        this.webview.setWebChromeClient(new MyWebViewClient());
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.loadUrl("file:///android_asset/AppHelp.html");
        this.progressBar1.setProgress(0);
        return this.view;
    }

    public void setValue(int progress) {
        this.progressBar1.setProgress(progress);
    }
}
