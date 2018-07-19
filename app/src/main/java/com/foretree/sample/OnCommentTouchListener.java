package com.foretree.sample;

import android.view.View;

import com.twitter.text.Type;

/**
 * 包含点击和长按,后续加上其他的事件
 * Created by silen on 30/11/2017.
 */

public interface OnCommentTouchListener<T> {
    void onCommentItemClick(View v, Type type, T item);
}
