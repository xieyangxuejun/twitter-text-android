package com.foretree.sample;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.twitter.text.Type;

/**
 * Created by silen on 30/11/2017.
 */

public class TouchableSpan extends ClickableSpan {
    private String mText;
    private Type mType;
    private OnCommentTouchListener<String> mCallback;

    public TouchableSpan(Type type, String text, OnCommentTouchListener callback) {
        this.mText = text;
        this.mType = type;
        this.mCallback = callback;
    }

    @Override
    public void onClick(View widget) {
        if (mCallback != null) {
            mCallback.onCommentItemClick(widget, mType, mText);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setAntiAlias(true);
        ds.setUnderlineText(false);
    }
}