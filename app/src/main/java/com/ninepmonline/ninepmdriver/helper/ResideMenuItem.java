package com.ninepmonline.ninepmdriver.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ninepmonline.ninepmdriver.R;

public class ResideMenuItem extends LinearLayout {
    private ImageView iv_icon;
    private ImageView iv_line;
    private TextView tv_title;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, int icon, String title, int line) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
        iv_line.setImageResource(line);
    }

    private void initViews(Context context) {
        ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_line = (ImageView) findViewById(R.id.iv_line);
    }

    public void setIcon(int icon) {
        iv_icon.setImageResource(icon);
    }

    public void setTitle(int title) {
        tv_title.setText(title);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }
}
