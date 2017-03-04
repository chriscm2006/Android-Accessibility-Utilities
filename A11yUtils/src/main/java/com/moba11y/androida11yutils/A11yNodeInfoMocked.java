package com.moba11y.androida11yutils;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;

/**
 * Created by chrismcmeeking on 2/27/17.
 */

class A11yNodeInfoMocked extends A11yNodeInfo {

    private ArrayList<A11yNodeInfoMocked> mChildren = new ArrayList<>();

    private String mContentDescription;

    private A11yNodeInfoMocked mParent = null;

    private String mText;

    private Class<? extends View> mClass;

    public static A11yNodeInfoMocked create() { return new A11yNodeInfoMocked();}

    A11yNodeInfoMocked addChild(A11yNodeInfoMocked child) {
        mChildren.add(child);

        child.setParent(this);

        return this;
    }

    A11yNodeInfoMocked addChildren(Iterable<A11yNodeInfoMocked> children) {
        for (A11yNodeInfoMocked child : children) {
            addChild(child);
        }

        return this;
    }

    @Override
    public A11yNodeInfoMocked getChild(final int i) {

        return mChildren.get(i);
    }


    @Override
    public int getChildCount() {
        return mChildren.size();
    }

    @Override
    public String getClassName() {
        return mClass.getName();
    }

    @Override
    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    @Override
    public A11yNodeInfoMocked getParent() {
        return mParent;
    }

    @Override
    public CharSequence getText() { return mText;}

    A11yNodeInfoMocked setClass(Class <? extends View> clazz) {
        mClass = clazz;
        return this;
    }

    A11yNodeInfoMocked setContentDescription(String contentDescription) {
        mContentDescription = contentDescription;
        return this;
    }

    private A11yNodeInfoMocked setParent(A11yNodeInfoMocked parent) {
        mParent = parent;
        return this;
    }

    A11yNodeInfoMocked setText(String text) {
        mText = text;
        return this;
    }

    @Override
    public String toString() {
        return "Hashcode: " + Integer.toString(this.hashCode());
    }
}
