package com.github.phuonghuynh.main;

import com.github.phuonghuynh.model.Status;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 * Created by kervin on 2016-07-22.
 */
@Configuration
@EnableJms
public class JMSConfig implements JmsListenerConfigurer
{
    private static final Logger LOGGER = LogManager.getLogger(JMSConfig.class);

    /**
     * Get a copy of the application context
     */
    @Autowired
    ConfigurableApplicationContext context;

    //@JmsListener(destination = "status-destination", containerFactory = "myJmsContainerFactory")
    @JmsListener( id = "receive.status", destination = "status.queue", containerFactory = "listenerContainerFactory")
    public void receiveStatus(Status message)
    {
        System.out.println("Received <" + message + ">");
//        context.close();
//        FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }

    @Bean(name = "jmsConnectionFactory")
    public ConnectionFactory connectionFactory()
    {
        ActiveMQConnectionFactory myConnectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
//        ActiveMQConnectionFactory myConnectionFactory = new ActiveMQConnectionFactory("vm://localhost");
        myConnectionFactory.setTrustAllPackages(true);
        //myConnectionFactory.setBrokerURL("");

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(myConnectionFactory);
        connectionFactory.setSessionCacheSize(5);
        connectionFactory.setCacheProducers(false);

        return connectionFactory;
    }

    @Bean
    public JmsListenerContainerFactory listenerContainerFactory()
    {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("5-10");

        return factory;
    }

    @Bean(name = "myJMSTemplateMgmt")
    public JmsTemplate jmsTemplate()
    {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setDefaultDestination(new ActiveMQQueue("default.queue"));
        jmsTemplate.setConnectionFactory(this.connectionFactory());

        // To configure JMS point-to-point queue
        jmsTemplate.setPubSubDomain(false);

        return jmsTemplate;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar)
    {
        //registrar.setContainerFactory(mainJMSFactory());
        LOGGER.debug("configureJmsListeners()");
    }

  @Bean
  public Queue jmsQueue() {
    return new ActiveMQQueue("jms.queue");
  }
}
