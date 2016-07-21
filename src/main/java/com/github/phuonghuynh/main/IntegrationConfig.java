package com.github.phuonghuynh.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.jms.connection.CachingConnectionFactory;

import java.util.concurrent.Executors;

/**
 * Created by phuonghqh on 7/16/16.
 */
@Configuration
@EnableIntegration
public class IntegrationConfig
{
    private static final Logger LOGGER = LogManager.getLogger(IntegrationConfig.class);

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private CachingConnectionFactory jmsConnectionFactory;

  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  public PollerMetadata poller() {
    return Pollers.fixedDelay(10).get();
  }

  @Bean
  public SingleChronicleQueue chronicleQueue() {
    String basePath = System.getProperty("java.io.tmpdir") + "/SimpleChronicle";
    return ChronicleQueueBuilder.single(basePath).build();
  }

  @Bean
  public IntegrationFlow chronicleStatusesInbound(SingleChronicleQueue chronicleQueue)
  {
      // FIXME : Read from chronicle queue
      return IntegrationFlows.from("status")
              .channel(MessageChannels.queue("statusesInboundChannel"))
              .get();
  }

    @Bean
    public IntegrationFlow chronicleStatusesOutbound(SingleChronicleQueue chronicleQueue)
    {
        return IntegrationFlows.from("status")
                .channel(c -> c.executor(Executors.newCachedThreadPool()))
                .channel(c -> c.queue(10))
                .handle(m -> {
                            // write one object
                            ExcerptAppender appender = chronicleQueue.acquireAppender();
                            String jsonStr = null;
                            try
                            {
                                jsonStr = jacksonObjectMapper.writeValueAsString(m.getPayload());
                            }
                            catch( JsonProcessingException ex )
                            {
                                LOGGER.debug("Error converting status to JSON", ex);
                            }
                            appender.writeText(jsonStr);
                        })
                .get();
    }

    @Bean
    public IntegrationFlow jmsStatusesInbound()
    {
        // FIXME : Should this be a Gateway instead?
        return IntegrationFlows
                .from(Jms.inboundAdapter(this.jmsConnectionFactory)
                        .configureJmsTemplate(t ->
                                                t.deliveryPersistent(true))
                        .destination("jmsInbound"))
                .channel(MessageChannels.queue("statusesInboundChannel"))
            .get();
    }

    @Bean
    public IntegrationFlow jmsStatusesOutbound()
    {
        return IntegrationFlows.from("jmsOutboundGatewayChannel")
                .handle(Jms.outboundGateway(this.jmsConnectionFactory)
                        .replyContainer(c ->
                                c.concurrentConsumers(3)
                                        .sessionTransacted(true))
                        .requestDestination("jmsPipelineTest"))
                .get();
    }
}
