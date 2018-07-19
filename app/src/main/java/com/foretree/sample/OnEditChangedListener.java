package com.foretree.sample;

import android.text.Editable;

import com.twitter.text.Type;

/**
 * 监听评论框输入问题监听
 * Created by silen on 04/12/2017.
 */

public interface OnEditChangedListener {
    void OnExtractor(Type type);
    void OnAfterText(Editable editable);
}
