package com.jerry.bedpad.adapter;

import android.content.Context;

import com.jerry.bedpad.R;
import com.jerry.bedpad.bean.Office;

import java.util.List;

/**
 * Created by Jerry on 2016/8/4.
 */
public class OfficeAdapter extends CommonAdapter<Office> {

    public OfficeAdapter(Context context, List<Office> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Office item) {
        if (item.isCheck()) {
            helper.getView(R.id.ll_office).setBackgroundResource(R.color.office_check);
        } else {
            helper.getView(R.id.ll_office).setBackgroundResource(R.color.transparent);
        }
        helper.setText(R.id.tv_office, item.getName());
    }
}
