package com.moba11y.androida11yutils;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by chrismcmeeking on 2/28/17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.moba11y.androida11yutils.BuildConfig.class)
public class A11yNodeInfoMatcherTest {
    @Test
    public void setText() throws Exception {
        A11yNodeInfoMocked nodeInfoMocked = new A11yNodeInfoMocked();
        A11yNodeInfoMatcher nodeInfoMatcher = new A11yNodeInfoMatcher();

        String text = "Hi there bob!";

        nodeInfoMatcher.setText(text);

        assertFalse("Matcher does not match a null text.", nodeInfoMatcher.match(nodeInfoMocked));
        nodeInfoMocked.setText(text);

        assertTrue("Matcher returns a match.", nodeInfoMatcher.match(nodeInfoMocked));

        nodeInfoMocked.setText("Not matching");

        assertFalse("Matcher does not match.", nodeInfoMatcher.match(nodeInfoMocked));
    }

    @Test
    public void setContentDescription() throws Exception {
        A11yNodeInfoMocked nodeInfoMocked = new A11yNodeInfoMocked();
        A11yNodeInfoMatcher nodeInfoMatcher = new A11yNodeInfoMatcher();

        String text = "Hi there bob!";

        nodeInfoMatcher.setContentDescription(text);
        assertFalse("Matcher does not match a null text.", nodeInfoMatcher.match(nodeInfoMocked));

        nodeInfoMocked.setContentDescription(text);
        assertTrue("Matcher returns a match.", nodeInfoMatcher.match(nodeInfoMocked));

        nodeInfoMocked.setContentDescription("Not matching");
        assertFalse("Matcher does not match.", nodeInfoMatcher.match(nodeInfoMocked));
    }

    @Test
    public void setClass() throws Exception {
        A11yNodeInfoMocked nodeInfoMocked = new A11yNodeInfoMocked();
        A11yNodeInfoMatcher nodeInfoMatcher = new A11yNodeInfoMatcher();

        nodeInfoMatcher.setClass(TextView.class);
        nodeInfoMocked.setClass(TextView.class);
        assertTrue("Same class matches.", nodeInfoMatcher.match(nodeInfoMocked));

        nodeInfoMatcher.setClass(Button.class);
        assertFalse("Different classes don't match.", nodeInfoMatcher.match(nodeInfoMocked));
    }

    @Test
    public void match() throws Exception {
        A11yNodeInfoMocked nodeInfoMocked = new A11yNodeInfoMocked();
        A11yNodeInfoMatcher nodeInfoMatcher = new A11yNodeInfoMatcher();

        assertTrue("Null everything matches.", nodeInfoMatcher.match(nodeInfoMocked));

        nodeInfoMocked.setContentDescription("Desc").setText("Text").setClass(Button.class);
        nodeInfoMatcher.setContentDescription("Desc").setText("Text").setClass(Button.class);
        assertTrue("Everythign set the same matches.", nodeInfoMatcher.match(nodeInfoMocked));

    }
}