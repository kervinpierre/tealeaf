package com.github.phuonghuynh.main;

import com.github.phuonghuynh.config.DemoConfig;
import com.github.phuonghuynh.model.Status;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import reactor.io.codec.JavaSerializationCodec;
import reactor.io.queue.spec.PersistentQueueSpec;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by phuonghqh on 7/16/16.
 */
@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackages = "com.github.phuonghuynh.service")
public class IntegrationConfig
{
    private static final Logger LOGGER = LogManager.getLogger(IntegrationConfig.class);

    @Autowired
    DemoConfig demoConfig;

    @Bean
    public QueueChannel inChannel()
    {
        return MessageChannels.queue().get();
    }

    @Bean
    public QueueChannel outChannel()
    {
        return MessageChannels.queue().get();
    }

    @Bean
    public QueueChannel jmsChannel()
    {
        return MessageChannels.queue().get();
    }

    @Bean
    public QueueChannel chronicleChannel()
            throws IOException
    {
        String currPath = demoConfig.getChroniclePath();

        if( StringUtils.isBlank(currPath) )
        {
            currPath = Files.createTempDirectory("demoApp").toString();
        }

        return new QueueChannel(new PersistentQueueSpec<Message<?>>()
                .codec(new JavaSerializationCodec<>())
                .basePath(currPath)
                .get());
    }

    @Bean
    public IntegrationFlow statusOutFlow( QueueChannel outChannel, DemoConfig demoConfig )
    {
        boolean currUseJMS = BooleanUtils.isTrue(demoConfig.getUseJms());

        return IntegrationFlows
                .from(outChannel)
                .<Status, Boolean>route(b -> {
                    return demoConfig.getUseJms();
                }, spec ->
                {
                    spec
                            .channelMapping(Boolean.TRUE.toString(), "jmsChannel")
                            .channelMapping(Boolean.FALSE.toString(), "chronicleChannel");
                })
                .get();
    }

    @Bean
    public IntegrationFlow statusJmsFlow( ConnectionFactory jmsConnectionFactory, QueueChannel jmsChannel, Queue toJmsQueue )
    {
        return IntegrationFlows
                .from(jmsChannel)
                .handle(Jms.outboundAdapter(jmsConnectionFactory).extractPayload(true).destination(toJmsQueue))
                .get();
    }

    @Bean
    public IntegrationFlow statusChronicleFlow( QueueChannel chronicleChannel )
    {
        return IntegrationFlows
                .from(chronicleChannel)
                .handle(new GenericHandler<Object>()
                {
                    @Override
                    public Object handle( Object payload, Map<String, Object> headers )
                    {
                        System.out.println("statusChronicleFlow");
                        return null;
                    }
                })
                .get();
    }

//  @Bean
//  public IntegrationFlow jmsInboundFlow(ConnectionFactory jmsConnectionFactory, Queue toIntQueue, QueueChannel inChannel) {
//    return IntegrationFlows
//      .from(Jms.inboundAdapter(jmsConnectionFactory).destination(toIntQueue))
//      .channel(inChannel)
//      .get();
//  }

//  @Bean
//  public IntegrationFlow jmsOutboundFlow(ConnectionFactory jmsConnectionFactory, Queue toJmsQueue, QueueChannel outChannel) {
//    return IntegrationFlows
//      .from(outChannel)
//      .handle(Jms.outboundAdapter(jmsConnectionFactory).extractPayload(true).destination(toJmsQueue))
//      .get();
//  }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller()
    {
        return Pollers.fixedDelay(100).get();
    }
}
