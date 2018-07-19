package com.foretree.sample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.twitter.text.Constants;
import com.twitter.text.Extractor;
import com.twitter.text.Type;

import java.util.List;

/**
 * 使用Twitter基础处理模式
 * Created by silen on 18/07/2018
 */
@SuppressLint("AppCompatCustomView")
public class EditingEditText2 extends EditText implements TextWatcher {
    private OnEditChangedListener mChangeListener;
    private OnCommentTouchListener<String> mItemTouchListener;
    private Extractor mExtractor;
    private TouchMovementMethod mMovementMethod;
    private String mPreContent;

    public EditingEditText2(Context context) {
        this(context, null);
    }

    public EditingEditText2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditingEditText2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditingEditText2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setUp(context, attrs);
    }

    private void setUp(Context context, AttributeSet attrs) {
        mMovementMethod = new TouchMovementMethod(context);
        setMovementMethod(mMovementMethod);
        mExtractor = new Extractor();
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isTextChange()) {
            if (mChangeListener != null) {
                String text = s.subSequence(start, s.length()).toString();
                switch (text) {
                    case Constants.MENTION_TAG: {
                        mChangeListener.OnExtractor(Type.CASHTAG);
                        break;
                    }
                    case Constants.HASH_TAG: {
                        mChangeListener.OnExtractor(Type.HASHTAG);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isTextChange()) {
            mPreContent = getText().toString();
            notifyText(getText().toString());
            if (mChangeListener != null) {
                mChangeListener.OnAfterText(editable);
            }
        }
    }

    public void notifyText(final String content) {
        List<Extractor.Entity> cashTagsEntities = mExtractor.extractMentionsOrListsWithIndices(content);
        List<Extractor.Entity> HashTagsEntities = mExtractor.extractHashtagsWithIndices(content);

        SpannableString commentSpan = CommentSpanUtil.getCommentSpan(content,
                getResources().getColor(R.color.colorPrimary),
                cashTagsEntities, HashTagsEntities,
                mItemTouchListener);
        int selectionStart = getSelectionStart();
        setText(commentSpan);
        setSelection(selectionStart);

        //单击
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMovementMethod.isPassToTv()) {
                    if (mItemTouchListener != null) {
                        mItemTouchListener.onCommentItemClick(v, Type.MENTION, content);
                    }
                }
            }
        });
    }

    private boolean isTextChange() {
        return mPreContent == null || !mPreContent.equals(getText().toString());
    }

    public void resolveText(String content) {
        int index = this.getSelectionStart();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.getText());
        Spanned htmlText = Html.fromHtml(String.format("<font color='%s'>" + content + "</font>", "#f3ba43"));
        spannableStringBuilder.insert(index, htmlText);
        spannableStringBuilder.insert(index + htmlText.length(), "\b");
        this.setText(spannableStringBuilder);
        this.setSelection(index + htmlText.length() + 1);
    }
}
