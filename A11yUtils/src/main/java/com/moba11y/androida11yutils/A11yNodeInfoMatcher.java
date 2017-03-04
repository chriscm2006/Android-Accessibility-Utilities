package com.moba11y.androida11yutils;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by chrismcmeeking on 2/27/17.
 */

public class A11yNodeInfoMatcher {

    private String mContentDescription;
    private String mText;
    private Class<? extends View> mClass;

    public A11yNodeInfoMatcher() {}

    public A11yNodeInfoMatcher setText(String text) {
        mText = text;
        return this;
    }

    public A11yNodeInfoMatcher setContentDescription(String contentDescription) {
        mContentDescription = contentDescription;
        return this;
    }

    public A11yNodeInfoMatcher setClass(Class<? extends View> clazz) {
        mClass = clazz;
        return this;
    }

    public boolean match(A11yNodeInfo nodeInfo) {
        if (mContentDescription != null &&
                (nodeInfo.getContentDescription() == null
                        || !mContentDescription.contentEquals(nodeInfo.getContentDescription()))) return false;

        if (mText != null && (nodeInfo.getText() == null || !mText.contentEquals(nodeInfo.getText()))) return false;

        if (mClass != null && !mClass.getName().contentEquals(nodeInfo.getClassName())) return false;

        return true;
    }
}
