package com.jerry.bedpad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jerry.bedpad.R;
import com.jerry.bedpad.adapter.CommonCallbackAdapter;
import com.jerry.bedpad.adapter.OfficeAdapter;
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

@ContentView(R.layout.activity_office)
public class OfficeActivity extends AppCompatActivity {


    @ViewInject(R.id.listview)
    private ListView mListView;

    private List<Office> mOffices = new ArrayList<>();
    private OfficeAdapter mAdapter;

    /**
     * 是否已经选择科室了
     */
    private boolean mSelected;

    private int mIndex;


    private String mOfficeId;
    private String mBedHisNumber;


    private int mEnterType = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        mEnterType = getIntent().getIntExtra(DetailActivity.ENTER_TYPE, -1);
        if (mEnterType != DetailActivity.ENTER_TYPE_CHANGE) {
            loadSpInfo();
            if (!TextUtils.isEmpty(mOfficeId) && !TextUtils.isEmpty(mBedHisNumber)) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else if (!TextUtils.isEmpty(mOfficeId) && TextUtils.isEmpty(mBedHisNumber)) {
                finish();
                startActivity(new Intent(this, BindActivity.class));
            } else if (TextUtils.isEmpty(mOfficeId)) {
                loadOfficeData();
            }
        } else {
            loadOfficeData();
        }
    }


    private void loadSpInfo() {
        mOfficeId = (String) SPUtils.get(this, Office.OFFICE_ID, "");
        mBedHisNumber = (String) SPUtils.get(this, Bed.BED_HIS_NUMBER, "");
    }


    private void loadOfficeData() {
        String url = Constant.IP + Constant.SERVICE + Constant.GET_ALL_OFFICE;
        L.i("读取科室： " + url);

        RequestParams params = new RequestParams(url);
        x.http().get(params, new CommonCallbackAdapter<String>() {

            @Override
            protected void success(String result) {
                mOffices = JsonUtil.getOfficeFromJson(result);
                if (!JsonUtil.isEmpty(mOffices)) {
                    setData();
                }
            }

        });

    }


    private void setData() {
        mAdapter = new OfficeAdapter(this, mOffices, R.layout.item_office);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSelected = true;
                mIndex = position;
                for (int i = 0; i < mOffices.size(); i++) {
                    if (i == position) {
                        mOffices.get(i).setCheck(true);
                    } else {
                        mOffices.get(i).setCheck(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Event(R.id.btn_ok)
    private void onOk(View v) {
        if (mSelected) {
            SPUtils.put(this, Office.OFFICE_ID, mOffices.get(mIndex).getId());
            SPUtils.put(this, Office.OFFICE_NAME, mOffices.get(mIndex).getName());
            Intent intent = new Intent(this, BindActivity.class);
            startActivity(intent);
        } else {
            T.showLong(this, "请选择一个科室");
        }

    }
}
