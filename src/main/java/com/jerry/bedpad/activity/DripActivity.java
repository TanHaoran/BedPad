package com.jerry.bedpad.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jerry.bedpad.R;
import com.jerry.bedpad.app.MyApplication;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_drip)
public class DripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
    }
}
