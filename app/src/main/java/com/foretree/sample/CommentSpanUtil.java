package com.foretree.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.twitter.text.Extractor;
import com.twitter.text.Type;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Html标签工具类
 * </p>
 */
public class CommentSpanUtil {


    public static SpannableString getCommentSpan(String content, int selectColor,
                                                 @NonNull List<Extractor.Entity> cashTags,
                                                 @NonNull List<Extractor.Entity> hashTags,
                                                 OnCommentTouchListener callback) {
        SpannableString ss = new SpannableString(content);
        //@
        setListSpan(selectColor, cashTags, callback, ss);
        //#
        setListSpan(selectColor, hashTags, callback, ss);
        return ss;
    }

    /**
     * 描述: 每一个spannable都需要单独的spannable对象设置,
     * 不然多个循环使用同一个只有last现实.
     * @param selectColor
     * @param list
     * @param callback
     * @param ss
     */
    private static void setListSpan(int selectColor, @NonNull List<Extractor.Entity> list,
                                    OnCommentTouchListener callback, SpannableString ss) {
        for (Extractor.Entity en : list) {
            TouchableSpan touchableSpan = new TouchableSpan(Type.from(en.getType().ordinal()), en.getValue(), callback);
            ss.setSpan(touchableSpan, en.getStart(), en.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(selectColor);
            ss.setSpan(foregroundColorSpan, en.getStart(), en.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 文字内容加粗
     *
     * @param content 文字内容
     * @return 返回Spannable
     */
    public static Spannable dealContentForBold(String content) {
        if (TextUtils.isEmpty(content)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(content);
        setBoldLinkSpan(spannableString, 0, spannableString.length());
        return spannableString;
    }

    /**
     * 设置加粗的style
     *
     * @param SS    内容
     * @param start 开始位置 [
     * @param end   结束位置 )
     */
    public static void setBoldLinkSpan(Spannable SS, int start, int end) {
        SS.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static Spannable getBoldSpan(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableStringBuilder formatUrlString(String contentStr){
        SpannableStringBuilder sp;
        if(!TextUtils.isEmpty(contentStr)){

            sp = new SpannableStringBuilder(contentStr);
            try {
                //处理url匹配
                Pattern urlPattern = Pattern.compile("(http|https|ftp|svn)://([a-zA-Z0-9]+[/?.?])" +
                        "+[a-zA-Z0-9]*\\??([a-zA-Z0-9]*=[a-zA-Z0-9]*&?)*");
                Matcher urlMatcher = urlPattern.matcher(contentStr);

                while (urlMatcher.find()) {
                    final String url = urlMatcher.group();
                    if(!TextUtils.isEmpty(url)){
                        sp.setSpan(new ClickableSpan(){
                            @Override
                            public void onClick(View widget) {
                                Uri uri = Uri.parse(url);
                                Context context = widget.getContext();
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                                context.startActivity(intent);
                            }
                        }, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                //处理电话匹配
                Pattern phonePattern = Pattern.compile("[1][34578][0-9]{9}");
                Matcher phoneMatcher = phonePattern.matcher(contentStr);
                while (phoneMatcher.find()) {
                    final String phone = phoneMatcher.group();
                    if(!TextUtils.isEmpty(phone)){
                        sp.setSpan(new ClickableSpan(){
                            @Override
                            public void onClick(View widget) {
                                Context context = widget.getContext();
                                //用intent启动拨打电话
                                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }, phoneMatcher.start(), phoneMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            sp = new SpannableStringBuilder();
        }
        return sp;
    }
}
