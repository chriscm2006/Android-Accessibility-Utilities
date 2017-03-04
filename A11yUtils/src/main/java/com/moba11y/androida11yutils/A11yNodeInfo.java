package com.moba11y.androida11yutils;

import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chrismcmeeking on 2/25/17.
 */

public class A11yNodeInfo implements Iterable<A11yNodeInfo> {

    public static A11yNodeInfo wrap(AccessibilityNodeInfo node) {
        return new A11yNodeInfo(node);
    }

    public static A11yNodeInfo wrap(AccessibilityNodeInfoCompat node) {
        return new A11yNodeInfo(node);
    }

    private static final ArrayList<Class<? extends View>> ACTIVE_CLASSES;

    static {
        ACTIVE_CLASSES = new ArrayList<>();
        ACTIVE_CLASSES.add(Button.class);
        ACTIVE_CLASSES.add(Switch.class);
        ACTIVE_CLASSES.add(CheckBox.class);
        ACTIVE_CLASSES.add(EditText.class);
    }

    enum Actions {

        ACCESSIBILITY_FOCUS(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS),
        CLEAR_ACCESSIBILITY_FOCUS(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS),
        CLEAR_FOCUS(AccessibilityNodeInfo.ACTION_CLEAR_FOCUS),
        CLEAR_SELECTION(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION),
        CLICK(AccessibilityNodeInfo.ACTION_CLICK),
        COLLAPSE(AccessibilityNodeInfo.ACTION_COLLAPSE),
        COPY(AccessibilityNodeInfo.ACTION_COPY),
        CUT(AccessibilityNodeInfo.ACTION_CUT),
        LONG_CLICK(AccessibilityNodeInfo.ACTION_LONG_CLICK),
        PASTE(AccessibilityNodeInfo.ACTION_PASTE),
        PREVIOUS_AT_MOVEMENT_GRANULARITY(AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY),
        PREVIOUS_HTML_ELEMENT(AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT);

        private final int mAndroidValue;

        Actions(int androidValue) {
            mAndroidValue = androidValue;
        }

        int getAndroidValue() {
            return mAndroidValue;
        }
    }

    private final AccessibilityNodeInfoCompat mNodeInfo;

    //A special constructor for testing.
    A11yNodeInfo() {
        mNodeInfo = null;
    }

    public A11yNodeInfo(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) throw new RuntimeException("Wrapping a null node doesn't make sense.");
        else mNodeInfo = new AccessibilityNodeInfoCompat(nodeInfo);
    }

    public A11yNodeInfo(AccessibilityNodeInfoCompat nodeInfoCompat) {
        if (nodeInfoCompat == null) throw new RuntimeException("Wrapping a null node doesn't make sense");
        mNodeInfo = nodeInfoCompat;
    }

    public List<AccessibilityNodeInfoCompat.AccessibilityActionCompat> getActionList() {
        return mNodeInfo.getActionList();
    }

    public int getActions() {
        return mNodeInfo.getActions();
    }

    /**
     * Callbacks for iterating over the A11yNodeInfo heirarchy.
     */
    public interface OnVisitListener {

        /**
         * Called for every node during heirarchy traversals.
         * @param nodeInfo The node that work will be doneon.
         * @return Return true to stop traversing, false to continue.
         */
        boolean onVisit(A11yNodeInfo nodeInfo);
    }

    public boolean isActiveElement() {
        for (Class<? extends View> clazz : ACTIVE_CLASSES) {
            if (this.getClassName().equalsIgnoreCase(clazz.getName())) return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK)) return true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK)) return true;
            }

            if (getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK)) return true;
            if (getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_SELECT)) return true;
        }

        final int actions = getActions();

        return (actions & AccessibilityNodeInfo.ACTION_CLICK) != 0 ||
                (actions & AccessibilityNodeInfo.ACTION_LONG_CLICK) != 0 ||
                (actions & AccessibilityNodeInfo.ACTION_SELECT) != 0;
    }

    /**
     * Loop over elements in the list, until one of them returns true.  Return the
     * first element where "onVisit" returns true.  This can be used to create a very
     * simple "find first" type of method.  Though most of the time, you likely want
     * to travel all, in which case, just return "false" from your onVisit method, and
     * you will visit every node.
     * @param onVisitListener {@link A11yNodeInfo.OnVisitListener#onVisit(A11yNodeInfo) onVisit}
     * will be alled for every node, until {@link A11yNodeInfo.OnVisitListener#onVisit(A11yNodeInfo) onVisit}
     * returns true.
     * @return The first node for which {@link A11yNodeInfo.OnVisitListener#onVisit(A11yNodeInfo) onVisit}  returns true.
     */
    public A11yNodeInfo visitNodes(OnVisitListener onVisitListener) {

        if (onVisitListener.onVisit(this)) return this;

        for (A11yNodeInfo child : this) {

            if (child != null) {
                A11yNodeInfo result = child.visitNodes(onVisitListener);
                if (result != null) return result;
            }
        }

        return null;
    }

    public boolean performAction(Actions action) {
        return mNodeInfo.performAction(action.getAndroidValue());
    }

    public A11yNodeInfo getChild(final int i) {

        if (i >= mNodeInfo.getChildCount()) throw new IndexOutOfBoundsException();

        return new A11yNodeInfo(mNodeInfo.getChild(i));
    }

    public int getChildCount() {
        return mNodeInfo.getChildCount();
    }

    public String getClassName() {
        return mNodeInfo.getClassName().toString();
    }

    public CharSequence getContentDescription() {
        return mNodeInfo.getContentDescription();
    }

    /**
     * Gets the depth of the child in the node info heirarchy.
     * @return The depth of the node.
     */
    public int getDepthInTree() {

        int result = 0;

        A11yNodeInfo parentNode = getParent();

        while (parentNode != null) {
            parentNode = parentNode.getParent();
            result++;
        }

        return result;
    }

    public A11yNodeInfo getParent() {
        if (mNodeInfo == null) return null;

        return new A11yNodeInfo(mNodeInfo.getParent());
    }

    public CharSequence getText() {
        return mNodeInfo.getText();
    }

    /**
     * Implenting the iterable interface to more easily navigate the node infos children.
     * @return An itarator over the children of this A11yNodeInfo.
     */
    @Override
    public Iterator<A11yNodeInfo> iterator() {
        return new Iterator<A11yNodeInfo>() {
            private int mCurrent;

            @Override
            public boolean hasNext() {
                return mCurrent < getChildCount();
            }

            @Override
            public A11yNodeInfo next() {
                return getChild(mCurrent++);
            }

            @Override
            public void remove() {

            }
        };
    }

    public String toViewHeirarchy() {
        final StringBuilder result = new StringBuilder();

        result.append("--------------- Accessibility Node Hierarchy ---------------\n");

        visitNodes(new A11yNodeInfo.OnVisitListener() {
            @Override
            public boolean onVisit(A11yNodeInfo nodeInfo) {

                for (int i = 0; i < nodeInfo.getDepthInTree(); i++) {
                    result.append("\t");
                }

                result.append(nodeInfo.toString());
                result.append('\n');

                return false;
            }
        });

        result.append("--------------- Accessibility Node Hierarchy ---------------");

        return result.toString();
    }

    @Override
    public String toString() {
        return mNodeInfo.toString();
    }
}
