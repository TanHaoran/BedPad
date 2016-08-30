package com.jerry.bedpad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bedpad.R;
import com.jerry.bedpad.adapter.CommonCallbackAdapter;
import com.jerry.bedpad.app.MyApplication;
import com.jerry.bedpad.bean.Bed;
import com.jerry.bedpad.bean.Device;
import com.jerry.bedpad.bean.Office;
import com.jerry.bedpad.bean.Patient;
import com.jerry.bedpad.bean.TemperatureDevice;
import com.jerry.bedpad.bluetooth.BluetoothUtil;
import com.jerry.bedpad.constant.Constant;
import com.jerry.bedpad.util.JsonUtil;
import com.jerry.bedpad.util.L;
import com.jerry.bedpad.util.TcpUtil;
import com.jerry.bedpad.util.SPUtils;
import com.jerry.bedpad.util.T;
import com.jerry.bedpad.view.NoteView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    /**
     * 科室id
     */
    private String mOfficeId;
    /**
     * 床号his的id
     */
    private String mBedHisNumber;
    /**
     * 病患信息
     */
    private Patient mPatient;

    @ViewInject(R.id.ll_note)
    private LinearLayout mNoteLayout;

    @ViewInject(R.id.tv_office)
    private TextView mTextOffice;

    @ViewInject(R.id.tv_number)
    private TextView mTextNumber;

    @ViewInject(R.id.tv_name)
    private TextView mTextName;

    @ViewInject(R.id.tv_age)
    private TextView mTextAge;
    @ViewInject(R.id.tv_in_date)
    private TextView mTextDate;
    @ViewInject(R.id.tv_hos_id)
    private TextView mTextHosId;
    @ViewInject(R.id.tv_history)
    private TextView mTextHistory;
    @ViewInject(R.id.tv_doctor)
    private TextView mTextDoctor;
    @ViewInject(R.id.tv_nurse)
    private TextView mTextNurse;


    /**
     * 设备Id
     */
    private String mDeviceId;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        // 读取配置信息
        loadConfig();
        // 读取各项数据
        loadData();
        // 蓝牙相关操作
        bluetoothWork();
//        // TCP连接
        TcpUtil.getInstance(this).connect();
    }

    /**
     * 读取配置信息
     */
    private void loadConfig() {
        String ip = (String) SPUtils.get(this, "ip", "");
        Constant.IP = ip;
    }


    /**
     * 读取各项数据
     */
    private void loadData() {
        // 读取应用数据中绑定的科室id和床号hisId
        loadOfficeAndBedSp();
        // 当科室id和床号hisId中有一个没有值时，就认为没有绑定数据
        if (TextUtils.isEmpty(mOfficeId) || TextUtils.isEmpty(mBedHisNumber)) {
            T.showLong(this, "设备未绑定");
        } else {
            // 读取病患信息
            loadPatientInfo(mOfficeId, mBedHisNumber);
        }
        // 读取绑定的设备信息
        loadDeviceSp(mOfficeId, mBedHisNumber);
    }

    /**
     * 蓝牙相关操作
     */
    private void bluetoothWork() {
        // 检测设备是否支持蓝牙4.0
        if (!BluetoothUtil.checkBluetoothEnvironment(this)) {
            T.showLong(this, "设备不支持蓝牙4.0");
            // 不支持就退出应用
            MyApplication.clearAll();
        }
    }


    /**
     * 读取应用数据中的科室id和床号hisid
     */
    private void loadOfficeAndBedSp() {
        mOfficeId = (String) SPUtils.get(this, Office.OFFICE_ID, "");
        mBedHisNumber = (String) SPUtils.get(this, Bed.BED_HIS_NUMBER, "");
    }

    /**
     * 读取病患信息
     *
     * @param officeId
     * @param bedHisNumber
     */
    private void loadPatientInfo(String officeId, String bedHisNumber) {

        String url = Constant.IP + Constant.SERVICE + Constant.GET_PATIENTINFO +
                "?OfficeID=" + officeId + "&hisbedno=" + bedHisNumber;
        L.i("病患信息： " + url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new CommonCallbackAdapter<String>() {

            @Override
            protected void success(String result) {
                mPatient = JsonUtil.getPatientInfolFromJson(result);
                Constant.PATIENT = mPatient;
                // 设置病患数据
                setPatientData();
            }

        });
    }


    /**
     * 读取绑定的设备信息
     */
    private void loadDeviceSp(String officeId, String bedHisId) {
        //   mDeviceId = "000057400352";
//        mDeviceId = "000057400311";

        String url = Constant.IP + Constant.DEVICE_SERVICE + Constant.GET_DEVICE_INFO +
                "?OfficeID=" + officeId + "&hisbedno=" + bedHisId;
        L.i("物联设备信息： " + url);
        RequestParams params = new RequestParams(url);
        x.http().get(params, new CommonCallbackAdapter<String>() {

            @Override
            protected void success(String result) {
                if (!TextUtils.isEmpty(result)) {
                    List<Device> devices = JsonUtil.getDeviceFromJson(result);
                    if (!JsonUtil.isEmpty(devices)) {
                        // 根据查询到的设备信息获取体温设备信息
                        getTemperatureDevice(devices);
                    }
                }


            }

        });
    }

    /**
     * 获取体温设备信息
     *
     * @param devices
     */
    private void getTemperatureDevice(List<Device> devices) {
        // 查找匹配体温设备信息
        for (Device d : devices) {
            if (d.getType().equals(Device.TYPE_TEMPERATURE)) {
                mDeviceId = Device.TYPE_TEMPERATURE_RAINBOW_ID_PREFIX + d.getId();
                startMonitor();
            }
        }
    }


    /**
     * 开始搜索蓝牙设备
     */
    private void startMonitor() {
        // 如果蓝牙准备好就开始监测
        if (BluetoothUtil.ready(this, mDeviceId)) {
            // 开始搜索蓝牙设备
            mHandler.removeCallbacks(mMonitor);
            mHandler.post(mMonitor);
        }

    }

    /**
     * 默认每10秒更新一次体温数据
     */
    private static final int DEFAULT_MONITOR = 10 * 1000;

    /**
     * 定时监听体温线程
     */
    private Runnable mMonitor = new Runnable() {
        @Override
        public void run() {
            BluetoothUtil.getInstance().startMonitorBroadcast(new BluetoothUtil.OnDeviceFound() {

                @Override
                public void onLeScan(TemperatureDevice blueInfo) {
                    // 如果搜索到的设备id和保存的保定的设备id一致，则发送广播
                    if (mDeviceId.equals(blueInfo.getId())) {
                        Constant.DEVICE = blueInfo;
                        Intent intent = new Intent(TemperatureActivity.GET_BLUETOOTH_VALUE);
                        Bundle b = new Bundle();
                        b.putSerializable(TemperatureDevice.DEVICE, blueInfo);
                        intent.putExtra("bundle", b);
                        sendBroadcast(intent);
                    }
                }
            });
            // 每隔DEFAULT_MONITOR时间，进行重新获取温度数据
            mHandler.postDelayed(mMonitor, DEFAULT_MONITOR);
        }
    };

    /**
     * 设置病患数据
     */
    private void setPatientData() {
        // 设置科室数据
        String officeName = (String) SPUtils.get(this, Office.OFFICE_NAME, "");
        mTextOffice.setText(officeName);
        // 设置病患数据
        if (mPatient != null) {
            mTextNumber.setText(mPatient.getBed());
            mTextName.setText(mPatient.getName());
            if ("女".equals(mPatient.getSex())) {
                mTextName.setTextColor(getResources().getColor(R.color.name_text_female));
            } else {
                mTextName.setTextColor(getResources().getColor(R.color.name_text_male));
            }
            mTextAge.setText(mPatient.getAge());
            mTextDate.setText(mPatient.getInDate());
            mTextHosId.setText(mPatient.getId());
            mTextHistory.setText(mPatient.getHistory());
            mTextDoctor.setText(mPatient.getDoctor());
            mTextNurse.setText(mPatient.getNurse());
            // 初始化右侧便签区域
            initNoteLayout(mPatient);
        }
    }

    /**
     * 初始化右侧便签区域
     *
     * @param patient
     */
    private void initNoteLayout(Patient patient) {
        // 护理等级
        NoteView levelView = new NoteView(this);
        levelView.setDuration(800);
        levelView.setFromColor(0xfff93c3c);
        levelView.setToColor(0xffed7878);
        levelView.setText(patient.getLevel());
        levelView.startChange();
        mNoteLayout.addView(levelView);
        // 护理事件的构造
        String event = mPatient.getEvent();
        if (!TextUtils.isEmpty(event)) {
            String[] events = event.split("\\|");
            if (events.length > 0) {
                for (String e : events) {
                    NoteView note = new NoteView(this);
                    note.setDuration(3000);
                    note.setFromColor(0xfff93c3c);
                    note.setToColor(0xffed7878);
                    note.setText(e);
                    note.startChange();
                    mNoteLayout.addView(note);
                }
            }
        }
    }


    /**
     * 点击任意区域，跳转页面
     *
     * @param v
     */
    @Event(R.id.ll_main)
    private void onMain(View v) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(TemperatureDevice.DEVICE, Constant.DEVICE);
        intent.putExtra("bundle", b);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TcpUtil.getInstance(this).close();
    }


    @Event(R.id.tv_hospital)
    private void onHospital(View v) {
        boolean isConnected = TcpUtil.getInstance(this).isServerClose();
        L.i("连接是否连接：" + isConnected);

    }
}
