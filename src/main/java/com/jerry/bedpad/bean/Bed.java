package com.jerry.bedpad.bean;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/3/7.
 */
public class Bed implements Serializable {

    public static final String BED_HIS_NUMBER = "bed_his_number";
    /**
     * 科室编号
     */
    private String officeId;
    /**
     * HIS床位编号
     */
    private String hisBedNo;
    /**
     * 床号
     */
    private String no;
    /**
     * 是否加床？啥意思
     */
    private boolean add;

    /**
     * 是否选中
     */
    private boolean check;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getHisBedNo() {
        return hisBedNo;
    }

    public void setHisBedNo(String hisBedNo) {
        this.hisBedNo = hisBedNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}


