package com.jerry.bedpad.adapter;

import android.content.Context;

import com.jerry.bedpad.R;
import com.jerry.bedpad.bean.Bed;

import java.util.List;

/**
 * Created by Jerry on 2016/7/27.
 */
public class BedAdapter extends CommonAdapter<Bed> {

    public BedAdapter(Context context, List<Bed> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Bed item) {
        if (item.isCheck()) {
            helper.getView(R.id.tv_number).setBackgroundResource(R.drawable.bed_bule);
        } else {
            helper.getView(R.id.tv_number).setBackgroundResource(R.drawable.bed);
        }
        helper.setText(R.id.tv_number, String.valueOf(item.getNo()));
    }
}
