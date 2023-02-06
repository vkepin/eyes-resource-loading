package com.eyestest;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.FailureReports;
import com.applitools.eyes.ImageMatchSettings;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.config.ConfigurationProvider;
import com.applitools.eyes.config.ContentInset;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.visualgrid.model.NullDebugResourceWriter;

import java.util.ArrayList;

public class DefaultConfigurationProvider implements ConfigurationProvider {

    private static final int DEFAULT_MATCH_TIMEOUT = 2000;
    private static final int DEFAULT_WAIT_BEFORE_SCREENSHOTS = 100;
    private static final int DEFAULT_STITCH_OVERLAP = 10;

    private Configuration configuration;

    public DefaultConfigurationProvider()
    {
        configuration = new Configuration();
        configuration.setBatch(new BatchInfo(null));
        configuration.setIgnoreDisplacements(false);
        configuration.setDefaultMatchSettings(new ImageMatchSettings());
        configuration.setMatchTimeout(DEFAULT_MATCH_TIMEOUT);
        configuration.setSendDom(true);
        configuration.setFailureReports(FailureReports.ON_CLOSE);
        configuration.setEnablePatterns(false);
        configuration.setUseDom(false);
        configuration.setWaitBeforeScreenshots(DEFAULT_WAIT_BEFORE_SCREENSHOTS);
        configuration.setStitchOverlap(DEFAULT_STITCH_OVERLAP);
        configuration.setStitchMode(StitchMode.SCROLL);
        configuration.setHideScrollbars(true);
        configuration.setHideCaret(true);
        configuration.setIsVisualGrid(false);
        configuration.setDisableBrowserFetching(true);
        configuration.setUseCookies(true);
        configuration.setCaptureStatusBar(false);
        configuration.setContentInset(new ContentInset());
        configuration.setDebugResourceWriter(new NullDebugResourceWriter());
        configuration.setRenderingConfig(false);
        configuration.setBrowsersInfo(new ArrayList<>());
        configuration.setLayoutBreakpoints(false);
        configuration.setSaveFailedTests(false);
        configuration.setSaveNewTests(true);
    }

    @Override
    public Configuration get()
    {
        return configuration;
    }
}
