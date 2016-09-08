package com.jerry.bedpad.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bedpad.R;
import com.jerry.bedpad.app.MyApplication;
import com.jerry.bedpad.bean.TemperatureDevice;
import com.jerry.bedpad.constant.Constant;
import com.jerry.bedpad.view.RippleBackground;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

@ContentView(R.layout.activity_temperature)
public class TemperatureActivity extends AppCompatActivity {


    @ViewInject(R.id.tv_number)
    private TextView mTextNumber;

    @ViewInject(R.id.tv_name)
    private TextView mTextName;

    @ViewInject(R.id.ripple)
    private RippleBackground mRipple;

    @ViewInject(R.id.tv_value)
    private TextView mValue;

    @ViewInject(R.id.iv_bluetooth)
    private ImageView mImageBluetooth;

    @ViewInject(R.id.tv_temperature_battery)
    private TextView mTemperatureBattery;

    @ViewInject(R.id.tv_temperature_state)
    private TextView mTemperatureState;

    @ViewInject(R.id.iv_temperature_battery)
    private ImageView mTemperatureBatteryImage;

    @ViewInject(R.id.tv_temperature_unit)
    private TextView mTextTemperatureState;

    @ViewInject(R.id.ll_battery)
    private LinearLayout mBatteryLayout;

    @ViewInject(R.id.tv_device_id)
    private TextView mTextDeviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
        // 注册广播用来接收蓝牙数据
        registerBroadcastReceiver();
    }

    /**
     * 注册广播用来接收蓝牙数据
     */
    private void registerBroadcastReceiver() {
        mReceiver = new BluetoothReceiver();
        IntentFilter filter = new IntentFilter(GET_BLUETOOTH_VALUE);
        registerReceiver(mReceiver, filter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle b = getIntent().getBundleExtra("bundle");
        if (b != null) {
            TemperatureDevice d = (TemperatureDevice) b.getSerializable(TemperatureDevice.DEVICE);
            if (d != null) {
                setTemperatureData(d);
            }
        }
        if (Constant.PATIENT != null) {
            mTextNumber.setText(Constant.PATIENT.getBed());
            mTextName.setText(Constant.PATIENT.getName());
        }
    }


    /**
     * 设置体温数据
     *
     * @param d
     */
    private void setTemperatureData(TemperatureDevice d) {
        // 设置不可见
        mImageBluetooth.setVisibility(View.INVISIBLE);
        // 设置可见
        mBatteryLayout.setVisibility(View.VISIBLE);
        mTextTemperatureState.setVisibility(View.VISIBLE);
        // 设置设备Id
        mTextDeviceId.setText(Constant.DEVICE.getId());
        // 设置体温值
        String value = formatValue(d.getTemperature());
        if (d.getTemperature() <= 36) {
            mValue.setTextColor(getResources().getColor(R.color.white));
        } else if (d.getTemperature() >= 38) {
            mValue.setTextColor(getResources().getColor(R.color.super_high));
        } else if (d.getTemperature() >= 37.5) {
            mValue.setTextColor(getResources().getColor(R.color.high));
        } else {
            mValue.setTextColor(getResources().getColor(R.color.normal));
        }
        mValue.setText(value);
        // 设置电量值
        int battery = d.getBatteryPower();
        // 设置电量图标
        if (battery == -128) {
            mTemperatureBattery.setText("充电中");
        } else {
            mTemperatureBattery.setText(battery + "%");
            if (battery < 20) {
                mTemperatureBatteryImage.setImageResource(R.drawable.battery_1);
            } else if (battery < 40) {
                mTemperatureBatteryImage.setImageResource(R.drawable.battery_2);
            } else if (battery < 60) {
                mTemperatureBatteryImage.setImageResource(R.drawable.battery_3);
            } else if (battery < 80) {
                mTemperatureBatteryImage.setImageResource(R.drawable.battery_4);
            } else {
                mTemperatureBatteryImage.setImageResource(R.drawable.battery_5);
            }
        }
        // 设置连接状态
        mTemperatureState.setText("设备已连接");

        if (!mRipple.isRippleAnimationRunning()) {
            mRipple.startRippleAnimation();
        }
    }

    /**
     * 格式化体温数据
     *
     * @param temperature
     * @return
     */
    private String formatValue(float temperature) {
        return new DecimalFormat("##.#").format(temperature);
    }


    @Event(R.id.rl_main)
    private void onMain(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private BluetoothReceiver mReceiver;
    public static final String GET_BLUETOOTH_VALUE = "com.jerry.bluetooth.value";


    class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("bundle");
            if (b != null) {
                TemperatureDevice d = (TemperatureDevice) b.getSerializable(TemperatureDevice.DEVICE);
                if (d != null) {
                    setTemperatureData(d);
                }
            }
        }
    }
}
