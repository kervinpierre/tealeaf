package com.github.phuonghuynh.util;

import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by kervin on 2016-07-21.
 */
public final class JMSServerUtil
{
    private static final Logger LOGGER = LogManager.getLogger(JMSServerUtil.class);

    // https://activemq.apache.org/artemis/docs/1.0.0/embedding-activemq.html
    public static void startServer() throws Exception
    {
        EmbeddedJMS jms = new EmbeddedJMS();
        jms.start();
    }
}
