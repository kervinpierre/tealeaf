package com.github.phuonghuynh.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.config.JmsListenerEndpointRegistry;

/**
 * Created by kervin on 2016-07-21.
 */
public final class JMSServerUtil
{
    private static final Logger LOGGER = LogManager.getLogger(JMSServerUtil.class);

    private final JmsListenerEndpointRegistry registry;

    @Autowired
    public  JMSServerUtil(JmsListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    public void stopListener()
    {
        this.registry.getListenerContainer("receive.status").stop();
    }

    public void startListener()
    {
        this.registry.getListenerContainer("receive.status").start();
    }

    // https://activemq.apache.org/artemis/docs/1.0.0/embedding-activemq.html
//    public static void startServer() throws Exception
//    {
//        // Step 1. Create Apache ActiveMQ Artemis core configuration, and set the properties accordingly
//        Configuration configuration = new ConfigurationImpl();
//        configuration.setPersistenceEnabled(false);
//        configuration.setSecurityEnabled(false);
//        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
//
//        // Step 2. Create the JMS configuration
//        JMSConfiguration jmsConfig = new JMSConfigurationImpl();
//
//        // Step 3. Configure the JMS ConnectionFactory
//        TransportConfiguration connectorConfig = new TransportConfiguration(NettyConnectorFactory.class.getName());
//        ConnectionFactoryConfiguration cfConfig
//                //= new ConnectionFactoryConfigurationImpl("cf", connectorConfig, "/cf");
//                = new ConnectionFactoryConfigurationImpl();
//        jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);
//
//        // Step 4. Configure the JMS Queue
//        JMSQueueConfiguration queueConfig
//                //= new JMSQueueConfigurationImpl("queue1", null, false, "/queue/queue1");
//                = new JMSQueueConfigurationImpl();
//        jmsConfig.getQueueConfigurations().add(queueConfig);
//
//        // Step 5. Start the JMS Server using the Apache ActiveMQ Artemis core server and the JMS configuration
//        EmbeddedJMS jmsServer = new EmbeddedJMS();
//        jmsServer.setConfiguration(configuration);
//        jmsServer.setJmsConfiguration(jmsConfig);
//        jmsServer.start();
//    }
}
