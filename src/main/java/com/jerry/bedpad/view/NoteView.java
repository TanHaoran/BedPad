package com.jerry.bedpad.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.bedpad.R;

/**
 * Created by Jerry on 2016/7/28.
 */
public class NoteView extends RelativeLayout {

    private static int DEFAULT_FROM_COLOR = 0x0000aFFE;
    private static int DEFAULT_TO_COLOR = 0x0069C8EC;
    private static float DEFAULT_TEXT_SIZE = 32;
    private static int DEFAULT_TEXT_COLOR = 0xFF000000;
    private static int DEFAULT_TEXT_MARGIN = 15;
    private static int DEFAULT_DURATION = 3000;

    private int mFromColor;
    private int mToColor;
    private String mText;
    private float mTextSize;
    private int mTextColor;
    private int mDuration;

    private View mStartView;
    private TextView mTextView;

    public NoteView(Context context) {
        this(context, null);
    }

    public NoteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NoteView);

        // 取得自定义的属性
        mFromColor = ta.getColor(R.styleable.NoteView_from_color, DEFAULT_FROM_COLOR);
        mToColor = ta.getColor(R.styleable.NoteView_to_color, DEFAULT_TO_COLOR);
        mText = ta.getString(R.styleable.NoteView_text);
        mTextSize = ta.getDimension(R.styleable.NoteView_text_size, sp2px(context, DEFAULT_TEXT_SIZE));
        mTextColor = ta.getColor(R.styleable.NoteView_text_color, DEFAULT_TEXT_COLOR);
        mDuration = ta.getInteger(R.styleable.NoteView_duration, DEFAULT_DURATION);

        initView(context);

        ta.recycle();
    }

    /**
     * 初始化界面
     *
     * @param context
     */
    private void initView(Context context) {
        // 首先设置起始颜色
        setBackgroundResource(R.drawable.note_bg);
        GradientDrawable bgGrad = (GradientDrawable) getBackground();
        bgGrad.setColor(mToColor);

        // 设置变化颜色
        mStartView = new View(context);
        LayoutParams startLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mStartView.setLayoutParams(startLp);
        mStartView.setBackgroundResource(R.drawable.note_bg);
        GradientDrawable startGrad = (GradientDrawable) mStartView.getBackground();
        startGrad.setColor(mFromColor);
        addView(mStartView);

        // 设置文字区域
        mTextView = new TextView(context);
        LayoutParams textLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textLp.setMargins(dp2px(context, DEFAULT_TEXT_MARGIN), dp2px(context, DEFAULT_TEXT_MARGIN),
                dp2px(context, DEFAULT_TEXT_MARGIN), dp2px(context, DEFAULT_TEXT_MARGIN));
        mTextView.setLayoutParams(textLp);
        mTextView.setTextSize(px2dp(context, mTextSize));
        mTextView.setTextColor(mTextColor);
        mTextView.setText(mText);
        mTextView.setGravity(Gravity.BOTTOM);
        addView(mTextView);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp2px(context, 70), ViewGroup.LayoutParams.MATCH_PARENT);

        lp.setMargins(dp2px(context, 20), 0, 0, 0);

        setLayoutParams(lp);

        startChange();
    }


    /**
     * 设置颜色变换时间
     *
     * @param duration
     */
    public void setDuration(int duration) {
        mDuration = duration;
        initView(getContext());
    }

    /**
     * 设置起始颜色
     *
     * @param color
     */
    public void setFromColor(int color) {
        mFromColor = color;
        initView(getContext());
    }

    /**
     * 设置结束颜色
     *
     * @param color
     */
    public void setToColor(int color) {
        mToColor = color;
        initView(getContext());
    }

    /**
     * 设置显示文字
     *
     * @param text
     */
    public void setText(String text) {
        mText = text;
        initView(getContext());
    }

    /**
     * 开始颜色渐变动画
     */
    public void startChange() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mStartView, "alpha", 1.0F, 0.0F).setDuration(mDuration);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();
    }


    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


    /**
     * px转sp
     *
     * @param context
     * @return
     */
    public int px2dp(Context context, float pxVal) {
        return (int) (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
