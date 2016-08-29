package com.jerry.bedpad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jerry.bedpad.R;
import com.jerry.bedpad.adapter.BedAdapter;
import com.jerry.bedpad.adapter.CommonCallbackAdapter;
import com.jerry.bedpad.app.MyApplication;
import com.jerry.bedpad.bean.Bed;
import com.jerry.bedpad.bean.Office;
import com.jerry.bedpad.constant.Constant;
import com.jerry.bedpad.util.JsonUtil;
import com.jerry.bedpad.util.L;
import com.jerry.bedpad.util.SPUtils;
import com.jerry.bedpad.util.T;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_bind)
public class BindActivity extends AppCompatActivity {

    @ViewInject(R.id.gridview)
    private GridView mGridView;

    private List<Bed> mBeds = new ArrayList<>();
    private BedAdapter mAdapter;

    /**
     * 是否已经选择床位了
     */
    private boolean mSelected;

    private String mOfficeId;

    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        loadSpInfo();
        if (!TextUtils.isEmpty(mOfficeId)) {
            loadBedData(mOfficeId);
        } else {
            T.showLong(this, "科室未绑定！");
        }
    }

    private void loadSpInfo() {
        mOfficeId = (String) SPUtils.get(this, Office.OFFICE_ID, "");
    }

    private void loadBedData(String officeId) {
        String url = Constant.IP + Constant.SERVICE + Constant.GET_ALL_BED +
                "?OfficeID=" + officeId;
        L.i("读取床位： " + url);

        RequestParams params = new RequestParams(url);
        x.http().get(params, new CommonCallbackAdapter<String>() {

            @Override
            protected void success(String result) {
                mBeds = JsonUtil.getBedInfolFromJson(result);
                if (!JsonUtil.isEmpty(mBeds)) {
                    setBedData();
                }
            }

        });

    }

    /**
     * 设置床位信息
     */
    private void setBedData() {
        mAdapter = new BedAdapter(this, mBeds, R.layout.item_bed);
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSelected = true;
                mIndex = position;
                for (int i = 0; i < mBeds.size(); i++) {
                    if (i == position) {
                        mBeds.get(i).setCheck(true);
                    } else {
                        mBeds.get(i).setCheck(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Event(R.id.btn_ok)
    private void onOk(View v) {
        if (mSelected) {
            MyApplication.clearAll();
            SPUtils.put(this, Bed.BED_HIS_NUMBER, mBeds.get(mIndex).getHisBedNo());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            T.showLong(this, "请选择一个需要绑定的床位！");
        }

    }
}
