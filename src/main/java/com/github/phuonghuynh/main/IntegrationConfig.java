package com.github.phuonghuynh.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.phuonghuynh.model.Status;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.scheduling.PollerMetadata;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 7/16/16.
 */
@Configuration
public class IntegrationConfig {

    @Autowired
    private ObjectMapper jacksonObjectMapper;

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
      return IntegrationFlows.from("status")
      .channel(c -> c.executor(Executors.newCachedThreadPool()))
      .<OrderItem, Boolean>route(OrderItem::isIced, mapping -> mapping
        .subFlowMapping("true", sf -> sf
          .channel(c -> c.queue(10))
          .publishSubscribeChannel(c -> c
            .subscribe(s -> s.handle(m ->
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch( InterruptedException e )
                {
                    ;
                }
            }))
            .subscribe(sub -> sub
              .<OrderItem, String>transform(p -> {
                String msg = Thread.currentThread().getName()
                  + " prepared cold drink #" + this.coldDrinkCounter.incrementAndGet()
                  + " for order #" + p.getOrderNumber() + ": " + p;

                // write one object
                ExcerptAppender appender = chronicleQueue.acquireAppender();
                appender.writeText(msg);
                return msg;
              })
              .handle(m -> {
                ExcerptTailer tailer = chronicleQueue.createTailer();
                System.out.println("from `chronicleQueue`: " + tailer.readText());
              })
            )
          )
        )
        .subFlowMapping("false", sf -> sf
          .channel(c -> c.queue(10))
          .publishSubscribeChannel(c -> c
            .subscribe(s -> s.handle(m -> {
                try
                {
                    Thread.sleep(5000);
                }
                catch( InterruptedException e )
                {
                    ;
                }
            }))
            .subscribe(sub -> sub
              .<OrderItem, String>transform(p ->
                Thread.currentThread().getName()
                  + " prepared hot drink #" + this.hotDrinkCounter.incrementAndGet()
                  + " for order #" + p.getOrderNumber() + ": " + p)
              .handle(m -> System.out.println(m.getPayload()))))))
      .<OrderItem, Status>transform(orderItem ->
        new Status(orderItem.getOrderNumber(),
          orderItem.getDrinkType(),
          orderItem.isIced(),
          orderItem.getShots()))
      .aggregate(aggregator -> aggregator
        .outputProcessor(g ->
          new Delivery(g.getMessages()
            .stream()
            .map(message -> (Status) message.getPayload())
            .collect(Collectors.toList())))
        .correlationStrategy(m -> ((Status) m.getPayload()).getOrderNumber()))
      .handle(CharacterStreamWritingMessageHandler.stdout());
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
                            catch( JsonProcessingException e )
                            {
                                //.debug("", e);
                            }
                            appender.writeText(jsonStr);
                        })
                .get();
    }

    @Bean
    public IntegrationFlow jmsStatusesInbound(SingleChronicleQueue chronicleQueue)
    {
        return IntegrationFlows
                .from(Jms.inboundAdapter(this.jmsConnectionFactory)
                        .configureJmsTemplate(t ->
                                t.deliveryPersistent(true)
                                        .jmsMessageConverter(myMessageConverter()))
                        .destination("jmsInbound"))
                .transform(...)
            .channel(...)
            .get();
    }

    @Bean
    public IntegrationFlow jmsStatusesOutbound(SingleChronicleQueue chronicleQueue)
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
