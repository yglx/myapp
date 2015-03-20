package com.example.myapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by jiangwan on 2015/3/14.
 */
public class MyButton extends Button {
    public MyButton(Context context) {
        super(context);
        this.setText("iasdf");

    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
