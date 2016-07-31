package com.github.phuonghuynh;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 *
 * @author kervin
 */
public class DemoTestWatcher extends TestWatcher
{
    private static final Logger log
            = LogManager.getLogger(DemoTestWatcher.class);

    @Override
    protected void failed(Throwable e, Description description)
    {
        log.info(
                String.format("%s failed %s",
                        description.getDisplayName(), e.getMessage()));

        super.failed(e, description);
    }

    @Override
    protected void succeeded(Description description)
    {
        log.info(
                String.format("%s succeeded.",
                        description.getDisplayName()));

        super.succeeded(description);
    }
}
