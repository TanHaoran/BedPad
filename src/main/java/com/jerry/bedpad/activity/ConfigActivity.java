package com.jerry.bedpad.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jerry.bedpad.R;
import com.jerry.bedpad.app.MyApplication;
import com.jerry.bedpad.constant.Constant;
import com.jerry.bedpad.util.SPUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_config)
public class ConfigActivity extends AppCompatActivity {

    @ViewInject(R.id.edit)
    private EditText mTextIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        loadConfig();
    }

    private void loadConfig() {
        String ip = (String) SPUtils.get(this, "ip", "");
        if (TextUtils.isEmpty(ip)) {
            ip = "http://192.168.0.100";
        }
        mTextIp.setText(ip);
    }

    @Event(R.id.btn_ok)
    private void onOk(View v) {
        saveConfig();
    }

    private void saveConfig() {
        String ip = mTextIp.getText().toString();
        SPUtils.put(this, "ip", ip);
        Constant.IP = ip;
        finish();
    }
}
