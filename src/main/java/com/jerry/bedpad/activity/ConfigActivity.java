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

    @ViewInject(R.id.edit_ip)
    private EditText mTextService;

    @ViewInject(R.id.edit_tcp)
    private EditText mTextTcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        loadConfig();
    }

    private void loadConfig() {
        String ip = (String) SPUtils.get(this, "ip", "");
        String tcp = (String) SPUtils.get(this, "tcp", "");
        if (TextUtils.isEmpty(ip)) {
            ip = "http://192.168.0.100";
        }

        if (TextUtils.isEmpty(tcp)) {
            tcp = "192.168.149.201";
        }
        mTextService.setText(ip);
        mTextTcp.setText(tcp);
    }

    @Event(R.id.btn_ok)
    private void onOk(View v) {
        saveConfig();
    }

    private void saveConfig() {
        String ip = mTextService.getText().toString();
        String tcp = mTextTcp.getText().toString();
        SPUtils.put(this, "ip", ip);
        SPUtils.put(this, "tcp", tcp);
        Constant.IP = ip;
        Constant.TEMPERATURE_SERVER_IP = tcp;
        finish();
    }


}
