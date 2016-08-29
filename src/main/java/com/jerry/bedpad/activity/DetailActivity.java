package com.jerry.bedpad.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bedpad.R;
import com.jerry.bedpad.app.MyApplication;
import com.jerry.bedpad.bean.TemperatureDevice;
import com.jerry.bedpad.bean.Office;
import com.jerry.bedpad.constant.Constant;
import com.jerry.bedpad.listener.OnDoubleClickListener;
import com.jerry.bedpad.util.DensityUtils;
import com.jerry.bedpad.util.SPUtils;
import com.jerry.bedpad.view.RippleBackground;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

@ContentView(R.layout.activity_detail)
public class DetailActivity extends AppCompatActivity {

    /**
     * 进入方式，用来识别是否重新绑定科室
     */
    public static final String ENTER_TYPE = "enter";
    /**
     * 表示未绑定科室
     */
    public static final int ENTER_TYPE_INIT = 0;
    /**
     * 表示已经有绑定的科室了
     */
    public static final int ENTER_TYPE_CHANGE = 1;

    @ViewInject(R.id.tv_hospital)
    private TextView mTextHospital;

    @ViewInject(R.id.tv_office)
    private TextView mTextOffice;

    @ViewInject(R.id.tv_number)
    private TextView mTextNumber;

    @ViewInject(R.id.tv_hos_id)
    private TextView mTextHosId;

    @ViewInject(R.id.tv_name)
    private TextView mTextName;
    @ViewInject(R.id.tv_sex)
    private TextView mTextSex;
    @ViewInject(R.id.tv_age)
    private TextView mTextAge;
    @ViewInject(R.id.tv_diagnosis)
    private TextView mTextDiagnosis;
    @ViewInject(R.id.tv_history)
    private TextView mTextHistory;
    @ViewInject(R.id.tv_doctor)
    private TextView mTextDoctor;
    @ViewInject(R.id.tv_nurse)
    private TextView mTextNurse;
    @ViewInject(R.id.tv_in_date)
    private TextView mTextInDate;
    @ViewInject(R.id.tv_level)
    private TextView mTextLevel;
    @ViewInject(R.id.tv_food)
    private TextView mTextFood;

    @ViewInject(R.id.ll_note)
    private LinearLayout mNoteLayout;

    @ViewInject(R.id.ripple)
    private RippleBackground mRipple;

    /**
     * 体温值TextView
     */
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


    /**
     * 蓝牙广播接收器
     */
    private BluetoothReceiver mReceiver;
    /**
     * 蓝牙广播接受字符串
     */
    public static final String GET_BLUETOOTH_VALUE = "com.jerry.bluetooth.value";


    /**
     * 设备信息
     */
    private TemperatureDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        // 初始化界面
        setPatientData();
        initView();
        // 注册广播用来接收蓝牙数据
        initReceiver();
    }

    /**
     * 初始化界面
     */
    private void setPatientData() {
        // 初始化科室数据
        String office = (String) SPUtils.get(this, Office.OFFICE_NAME, "");
        mTextOffice.setText(office);
        // 初始化病患数据
        if (Constant.PATIENT != null) {
            mTextNumber.setText(Constant.PATIENT.getBed());
            mTextHosId.setText(Constant.PATIENT.getId());
            mTextName.setText(Constant.PATIENT.getName());
            if ("女".equals(Constant.PATIENT.getSex())) {
                mTextName.setTextColor(getResources().getColor(R.color.name_text_female));
            } else {
                mTextName.setTextColor(getResources().getColor(R.color.name_text_male));
            }
            mTextSex.setText(Constant.PATIENT.getSex());
            mTextAge.setText(Constant.PATIENT.getAge());
            mTextDiagnosis.setText(Constant.PATIENT.getDiagnosis());
            mTextHistory.setText(Constant.PATIENT.getHistory());
            mTextDoctor.setText(Constant.PATIENT.getDoctor());
            mTextNurse.setText(Constant.PATIENT.getNurse());
            mTextInDate.setText(Constant.PATIENT.getInDate());
            mTextLevel.setText(Constant.PATIENT.getLevel());
            mTextFood.setText("无");
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        // 如果传过来的时候已经有了设备信息就显示出体温数据
        Bundle b = getIntent().getBundleExtra("bundle");
        if (b != null) {
            TemperatureDevice d = (TemperatureDevice) b.getSerializable(TemperatureDevice.DEVICE);
            if (d != null) {
                mDevice = d;
                setTemperatureData(d);
            }
        }
        // 初始化中间便签布局
        initNoteLayout();

        /*******************************************************/
        /*******************************************************/
        /***********注册医院文字的双击事件，用来修改绑定科室***********/
        /*******************************************************/
        /*******************************************************/
        mTextHospital.setOnTouchListener(new OnDoubleClickListener() {
            @Override
            protected void onDoubleClick() {
                MyApplication.clearAll();
                Intent intent = new Intent(DetailActivity.this, OfficeActivity.class);
                intent.putExtra(ENTER_TYPE, ENTER_TYPE_CHANGE);
                startActivity(intent);
            }
        });
    }


    /**
     * 初始化中间便签布局
     */
    private void initNoteLayout() {
        if (Constant.PATIENT != null) {
            // 护理等级的添加
            mNoteLayout.removeAllViews();
            TextView levelText = new TextView(this);
            levelText.setText(Constant.PATIENT.getLevel());
            // 护理事件的添加
            String event = Constant.PATIENT.getEvent();
            if (!TextUtils.isEmpty(event)) {
                String[] events = event.split("\\ |");
                int length = events.length;
                if (length > 0) {
                    fillData(levelText, 0xbbf93c3c, false);
                    for (int i = 0; i < length; i++) {
                        TextView textView = new TextView(this);
                        textView.setText(events[i]);
                        if (i == length - 1) {
                            fillData(textView, 0xbbf93c3c, true);
                        } else {

                            fillData(textView, 0xbbf93c3c, false);
                        }
                    }
                } else {
                    fillData(levelText, 0xbbf93c3c, true);
                }
            } else {
                fillData(levelText, 0xbbf93c3c, true);
            }
        }
    }


    /**
     * 给NoteView填充数据
     *
     * @param v       需要填充的View
     * @param bgColor 背景颜色
     * @param isLast  是否是最后一个元素
     */
    private void fillData(View v, int bgColor, boolean isLast) {
        v.setBackgroundColor(bgColor);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        if (!isLast) {
            lp.setMargins(0, 0, 0, DensityUtils.dp2px(this, 15));
        }
        v.setLayoutParams(lp);
        TextView textView = (TextView) v;
        textView.setTextSize(22);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        mNoteLayout.addView(v);
    }

    /**
     * 注册广播用来接收蓝牙数据
     */
    private void initReceiver() {
        mReceiver = new BluetoothReceiver();
        IntentFilter filter = new IntentFilter(GET_BLUETOOTH_VALUE);
        registerReceiver(mReceiver, filter);
    }

    /**
     * 蓝牙广播接受类
     */
    class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getBundleExtra("bundle");
            if (b != null) {
                TemperatureDevice d = (TemperatureDevice) b.getSerializable(TemperatureDevice.DEVICE);
                if (d != null) {
                    mDevice = d;
                    setTemperatureData(d);
                }
            }
        }
    }


    /**
     * 设置体温数据
     *
     * @param d 体温设备
     */
    private void setTemperatureData(TemperatureDevice d) {
        // 设置不可见
        mImageBluetooth.setVisibility(View.INVISIBLE);
        // 设置可见
        mBatteryLayout.setVisibility(View.VISIBLE);
        mTextTemperatureState.setVisibility(View.VISIBLE);
        // 设置体温值
        String value = formatValue(d.getTemperature());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除监听
        unregisterReceiver(mReceiver);
    }


    /**
     * 体温检测
     *
     * @param v
     */
    @Event(R.id.ll_temperature)
    private void onTemperature(View v) {
        Intent intent = new Intent(this, TemperatureActivity.class);
        if (mDevice != null) {
            Bundle b = new Bundle();
            b.putSerializable(TemperatureDevice.DEVICE, mDevice);
            intent.putExtra("bundle", b);
        }
        startActivity(intent);
    }


    /**
     * 静滴详细
     *
     * @param v
     */
    @Event(R.id.ll_drip)
    private void onDrip(View v) {
//        Intent intent = new Intent(this, DripActivity.class);
//        startActivity(intent);
    }


    @Event(R.id.ll_main)
    private void onMain(View v) {
        finish();
    }

}
