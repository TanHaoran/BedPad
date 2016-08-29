package com.jerry.bedpad.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bedpad.R;
import com.jerry.bedpad.util.DensityUtils;

/**
 * Created by Jerry on 2016/8/1.
 */
public class MessageView extends LinearLayout {

    private static final int DEFAULT_ROUND_SIZE = 10;
    private static final int DEFAULT_MARGIN_TOP = 5;
    private static final int DEFAULT_COLOR = 0xffffffff;
    private static final int DEFAULT_TEXT_SIZE = 22;
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;

    private int mColor;
    private String mText;
    private float mTextSize;
    private int mTextColor;

    private View mRound;
    private TextView mTextView;

    public MessageView(Context context) {
        this(context, null);
    }

    public MessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MessageView);

        // 取得自定义的属性
        mColor = ta.getColor(R.styleable.MessageView_round_color, DEFAULT_COLOR);
        mText = ta.getString(R.styleable.MessageView_message_text);
        mTextSize = ta.getDimension(R.styleable.MessageView_message_size, DensityUtils.dp2px(context, DEFAULT_TEXT_SIZE));
        mTextColor = ta.getColor(R.styleable.MessageView_message_color, DEFAULT_TEXT_COLOR);

        initView(context);

        ta.recycle();
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        initRound(context);
        initText(context);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initRound(Context context) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(mColor);
        gd.setCornerRadius(DEFAULT_ROUND_SIZE / 2);
        mRound = new View(context);
        LinearLayout.LayoutParams lp = new LayoutParams((int) DensityUtils.px2dp(context, DEFAULT_ROUND_SIZE),
                (int) DensityUtils.px2dp(context, DEFAULT_ROUND_SIZE));
        mRound.setLayoutParams(lp);
        mRound.setBackground(gd);
        addView(mRound);
    }


    private void initText(Context context) {
        mTextView = new TextView(context);
        LinearLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) DensityUtils.px2dp(context, 8), 0, 0, 0);
        mTextView.setLayoutParams(lp);
        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(DensityUtils.px2dp(context, mTextSize));
        mTextView.setText(mText);
        addView(mTextView);
    }
}
