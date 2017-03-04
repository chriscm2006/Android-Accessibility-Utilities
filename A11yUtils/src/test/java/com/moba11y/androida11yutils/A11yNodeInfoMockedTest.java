package com.moba11y.androida11yutils;

import android.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = com.moba11y.androida11yutils.BuildConfig.class)
public class A11yNodeInfoMockedTest {
    @Test
    public void getClassName() throws Exception {
        assertEquals(Button.class.getName(), A11yNodeInfoMocked.create().setClass(Button.class).getClassName());
    }
}