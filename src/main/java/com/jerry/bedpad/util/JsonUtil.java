package com.jerry.bedpad.util;

import com.jerry.bedpad.bean.Bed;
import com.jerry.bedpad.bean.Device;
import com.jerry.bedpad.bean.Note;
import com.jerry.bedpad.bean.Office;
import com.jerry.bedpad.bean.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/3/2.
 */
public class JsonUtil {

    /**
     * 将json字符串解析为科室集合
     *
     * @param json
     * @return
     */
    public static List<Office> getOfficeFromJson(String json) {
        json = filterJson(json);
        List<Office> officeList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Office item = new Office();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("OFFICEID"));
                item.setName(o.getString("OFFICENAME"));
                item.setSpell(o.getString("SPELLNO"));
                item.setHisId(o.getString("HISID"));
                officeList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return officeList;
    }


    /**
     * 将json字符串解析为床位集合
     *
     * @param json
     * @return
     */
    public static List<Bed> getBedInfolFromJson(String json) {
        json = filterJson(json);
        List<Bed> bedList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Bed item = new Bed();
                JSONObject o = data.getJSONObject(i);
                item.setOfficeId(o.getString("OfficeID"));
                item.setHisBedNo(o.getString("HISBedNo"));
                item.setAdd(Boolean.parseBoolean(o.getString("BedAdded")));
                item.setNo(o.getString("BedNo"));
                bedList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bedList;
    }


    /**
     * 将json字符串解析为病患信息
     *
     * @param json
     * @return
     */
    public static Patient getPatientInfolFromJson(String json) {
        json = filterJson(json);
        Patient p = new Patient();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject("data");
            // 住院号
            String hosId = data.getString("InhosID");
            if (!"null".equals(hosId)) {
                p.setId(hosId);
            } else {
                p.setId("");
            }
            // 住院次数
            int times = data.getInt("InHosTimes");
            p.setTimes(times);
            // 床号
            String bedNo = data.getString("BedNo");
            if (!"null".equals(bedNo)) {
                p.setBed(bedNo);
            } else {
                p.setBed("");
            }
            // 姓名
            String name = data.getString("Name");
            if (!"null".equals(name)) {
                p.setName(name);
            } else {
                p.setName("");
            }
            // 性别
            String sex = data.getString("Sex");
            if (!"null".equals(sex)) {
                p.setSex(sex);
            } else {
                p.setSex("");
            }
            // 年龄
            String age = data.getString("Age");
            if (!"null".equals(age)) {
                p.setAge(age);
            } else {
                p.setAge("");
            }
            // 护理等级
            String level = data.getString("NurseLevel");
            if (!"null".equals(level)) {
                p.setLevel(level);
            } else {
                p.setLevel("");
            }
            // 诊断
            String diagnosis = data.getString("Diagnosis");
            if (!"null".equals(diagnosis)) {
                p.setDiagnosis(diagnosis);
            } else {
                p.setDiagnosis("");
            }
            // 住院日期
            String date = data.getString("InHosDate");
            if (!"null".equals(date)) {
                p.setInDate(date);
            } else {
                p.setInDate("");
            }
            // 医生
            String doctor = data.getString("Doctor");
            if (!"null".equals(doctor)) {
                p.setDoctor(doctor);
            } else {
                p.setDoctor("");
            }
            // 护士
            String nurse = data.getString("Nurse");
            if (!"null".equals(nurse)) {
                p.setNurse(nurse);
            } else {
                p.setNurse("");
            }
            // 病重情况
            String state = data.getString("IllnessState");
            if (!"null".equals(state)) {
                p.setState(state);
            } else {
                p.setState("");
            }
            // 护理事件
            String event = data.getString("EventState");
            p.setEvent(event);
            // 过敏史
            String allergic = data.getString("Allergic");
            if (!"null".equals(allergic)) {
                p.setHistory(allergic);
            } else {
                p.setHistory("");
            }
            // 饮食
            String food = data.getString("Diet");
            if (!"null".equals(food)) {
                p.setFood(food);
            } else {
                p.setHistory("");
            }
            // 获取标签
            JSONArray notes = data.getJSONArray("lblmsg");
            for (int i = 0; i < notes.length(); i++) {
                Note n = new Note();
                String noteName = notes.getJSONObject(i).getString("LableText");
                String noteColor = notes.getJSONObject(i).getString("LableColor");
                n.setName(noteName);
                n.setColor(noteColor);
                p.getNoteList().add(n);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }


    /**
     * 将json字符串解析为物联设备集合
     *
     * @param json
     * @return
     */
    public static List<Device> getDeviceFromJson(String json) {
        json = filterJson(json);
        List<Device> devices = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray data = object.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                Device item = new Device();
                JSONObject o = data.getJSONObject(i);
                item.setId(o.getString("DeviceID"));
                item.setName(o.getString("DeviceName"));
                item.setMac(o.getString("Device_MacAddress"));
                item.setType(o.getString("JointDeviceType"));
                devices.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devices;
    }


    /**
     * 过滤不必要的字符串
     *
     * @param json
     * @return
     */
    public static String filterJson(String json) {
        String result = "";
        if (json.length() > 0) {
            if (json.startsWith("\"")) {
                result = json.substring(1, json.length() - 1);
            }
            result = result.replace("\\", "");
        }
        return result;
    }

    public static boolean isEmpty(List list) {
        return (list == null || list.size() == 0);
    }

}
