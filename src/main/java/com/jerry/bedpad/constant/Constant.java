package com.jerry.bedpad.constant;

import com.jerry.bedpad.bean.TemperatureDevice;
import com.jerry.bedpad.bean.Patient;

/**
 * Created by Jerry on 2016/3/2.
 */
public class Constant {


    public static Patient PATIENT;

    public static TemperatureDevice TEMPERATURE_DEVICE;


    public static String IP = "";
    public static String TCP = "";


    public static  long firstTime = 0;
    public static  long unTouchTime = 0;


    /**
     * 普通方法服务
     */
    public static final String SERVICE = ":4514/NSIS_WebAPI/";


    /**
     * 物联设备服务
     */
    public static final String DEVICE_SERVICE = ":4519/NSIS_WEBJoint/";

    /**
     * 获取所有科室
     */
    public static final String GET_ALL_OFFICE = "FindOfficeAll";


    /**
     * 获取所有床位信息
     */
    public static final String GET_ALL_BED = "FindBedFileAllByOfficeID";


    /**
     * 获取单个病患信息
     */
    public static final String GET_PATIENTINFO = "GetPatientInfoByBedNo";


    /**
     * 获取病患的物联设备信息
     */
    public static final String GET_DEVICE_INFO = "GetJointDevice";

    /**
     * 获取APK服务版本
     */
    public static final String GET_VERSION = "GetVersion";

    /**
     * 获取APK下载地址
     */
    public static final String APK_URL = "";


    /**
     * 体温数据服务端IP
     */
//    public static final String TEMPERATURE_SERVER_IP = "192.168.0.145";
    public static String TEMPERATURE_SERVER_IP = "192.168.149.201";
    /**
     * 体温数据服务端端口
     */
    public static final int TEMPERATURE_SERVER_PORT = 9005;


}
