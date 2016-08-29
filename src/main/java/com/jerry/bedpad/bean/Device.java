package com.jerry.bedpad.bean;

/**
 * Created by Jerry on 2016/8/19.
 */
public class Device {


    public static final String TYPE_TEMPERATURE_RAINBOW_ID_PREFIX = "000057400";
    public static final String TYPE_TEMPERATURE = "体温";

    protected String mac;
    protected String type;




    /**
     * 设备名称
     */
    protected String name;
    /**
     * 设备id
     */
    protected String id;


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
