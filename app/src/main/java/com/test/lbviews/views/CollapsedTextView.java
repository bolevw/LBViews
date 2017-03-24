package com.test.lbviews.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by liubo on 2017/3/21.
 */

public class CollapsedTextView extends AppCompatTextView {

    private static final String sExpand = "...展开";
    private static final String sClose = "...收起";


    private boolean mIsExpand; //是否展开
    private String mOriginalText;

    public CollapsedTextView(Context context) {
        super(context);
    }

    public CollapsedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            super.setText(text, type);
        } else {
            if (mIsExpand) {
//                this.mOriginalText =
            } else {

            }
        }
    }
}
