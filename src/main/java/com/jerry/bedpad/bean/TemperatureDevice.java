package com.jerry.bedpad.bean;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Jerry on 2016/7/6.
 * 蓝牙设备
 */
public class TemperatureDevice extends Device implements Serializable {


    public static final String DEVICE_ID = "device_id";

    public static final String DEVICE = "device";
    /**
     * 设备序列号
     */
    private int deviceSerialNumber;
    /**
     * 体温数据
     */
    private float temperature;
    /**
     * 原始体温数据
     */
    private String srcTemperature;
    /**
     * 电池电量
     */
    private int batteryPower;

    /**
     * 是否绑定
     */
    private boolean isBind;

    /**
     * 原始接受数据
     */
    private byte[] receive;

    /**
     * 信号强度
     */
    private int rssi;

    /**
     * MAC类型
     */
    private int macType;

    /**
     * MAC地址
     */
    private String address;


    public int getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(int deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getSrcTemperature() {
        return srcTemperature;
    }

    public void setSrcTemperature(String srcTemperature) {
        this.srcTemperature = srcTemperature;
    }

    public int getBatteryPower() {
        return batteryPower;
    }

    public void setBatteryPower(int batteryPower) {
        this.batteryPower = batteryPower;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public byte[] getReceive() {
        return receive;
    }

    public void setReceive(byte[] receive) {
        this.receive = receive;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getMacType() {
        return macType;
    }

    public void setMacType(int macType) {
        this.macType = macType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "TemperatureDevice{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", deviceSerialNumber=" + deviceSerialNumber +
                ", temperature=" + temperature +
                ", srcTemperature='" + srcTemperature + '\'' +
                ", batteryPower=" + batteryPower +
                ", isBind=" + isBind +
                ", receive=" + Arrays.toString(receive) +
                ", rssi=" + rssi +
                ", macType=" + macType +
                ", address='" + address + '\'' +
                '}';
    }
}
