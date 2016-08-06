package com.github.phuonghuynh.util;

import com.github.phuonghuynh.main.DemoApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by kervin on 2016-07-22.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
class JMSServerUtilTest
{
    private static final Logger LOGGER
            = LogManager.getLogger(JMSServerUtilTest.class);


    @Before
    public void setUp()
    {

    }

    @After
    public void tearDown()
    {

    }

    @Test
    @Ignore
    public void A001_startServer() throws Exception
    {
//        JMSServerUtil.startServer();
    }
}