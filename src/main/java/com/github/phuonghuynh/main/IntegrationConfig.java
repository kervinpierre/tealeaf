package com.github.phuonghuynh.main;

import com.github.phuonghuynh.config.DemoConfig;
import com.github.phuonghuynh.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import reactor.io.encoding.JavaSerializationCodec;
import reactor.queue.PersistentQueue;
import reactor.queue.spec.PersistentQueueSpec;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import java.io.File;
import java.io.IOException;

/**
 * Created by phuonghqh on 7/16/16.
 */
@Slf4j
@Configuration
@EnableIntegration
@IntegrationComponentScan(basePackages = "com.github.phuonghuynh.service")
public class IntegrationConfig {
  private static final Logger LOGGER = LogManager.getLogger(IntegrationConfig.class);

  @Autowired
  DemoConfig demoConfig;

  @Bean//TODO chronicle folder must be shared between process, otherwise they not see items
  public PersistentQueue<Message<?>> chronicleQueue() throws IOException {
//    String testName = "A001_testStatusGateway";
//    Path testDir = Files.createTempDirectory(testName);
//        Path chronicleDir = Files.createDirectory(testDir.resolve("chronicleDir"));
    new File("chronicleDir/").mkdir();
    return new PersistentQueueSpec<Message<?>>()
      .codec(new JavaSerializationCodec<>())
//      .codec(new JavaSerializationCodec<>())
      .basePath("chronicleDir/")
      .dataBlockSize(10000)
//      .basePath(testDir.toString() + "chronicleDir")
      .get();
  }

  @Bean
  public QueueChannel inChannel(PersistentQueue<Message<?>> chronicleQueue) {
//    return new QueueChannel(chronicleQueue);
    return MessageChannels.queue().get();
  }

  @Bean
  public QueueChannel routeChannel(PersistentQueue<Message<?>> chronicleQueue) {
//    return new QueueChannel(chronicleQueue);
    return MessageChannels.queue().get();
  }

  @Bean
  public QueueChannel jmsChannel(PersistentQueue<Message<?>> chronicleQueue) {
    return MessageChannels.queue().get();
//    return new QueueChannel(chronicleQueue);
  }

  @Bean
  public QueueChannel jmsInChannel(PersistentQueue<Message<?>> chronicleQueue) {
    return MessageChannels.queue().get();
//    return new QueueChannel(chronicleQueue);
  }

  @Bean
  public QueueChannel chronicleChannel(PersistentQueue<Message<?>> chronicleQueue) {
    return new QueueChannel(chronicleQueue);
  }

//  @Bean
//  public QueueChannel chronicleInChannel(PersistentQueue<Message<?>> chronicleQueue) {
//    return new QueueChannel(chronicleQueue);
//  }

  @Bean
  public IntegrationFlow routeFlow(QueueChannel routeChannel, DemoConfig demoConfig) {
    return IntegrationFlows
      .from(routeChannel)
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
  public IntegrationFlow jmsOutIntegrationFlow(ConnectionFactory jmsConnectionFactory, QueueChannel jmsChannel, Queue jmsQueue) {
    return IntegrationFlows
      .from(jmsChannel)
      .handle((payload, headers) -> {
        log.info("writing to Jms, payload: {}", payload);
        return payload;
      })
      .handle(Jms.outboundAdapter(jmsConnectionFactory).extractPayload(true).destination(jmsQueue))
      .get();
  }

  @Profile("JmsConsumer")
  @Bean
  public IntegrationFlow jmsInIntegrationFlow(ConnectionFactory jmsConnectionFactory, Queue jmsQueue, QueueChannel jmsInChannel) {
    return IntegrationFlows
      .from(Jms.inboundAdapter(jmsConnectionFactory).destination(jmsQueue))
      .channel(jmsInChannel)
      .handle((payload, headers) -> {
        log.info("reading from jms queue, payload: {}", payload);
        return payload;
      })
      .get();
  }

  @Bean
  public IntegrationFlow chronicleIntegrationFlow(QueueChannel chronicleChannel) {
    return IntegrationFlows
      .from(chronicleChannel)
      .handle((payload, headers) -> {
        log.info("writing to chronicle queue, payload: {}", payload);
        return payload;
      })
//      .channel(chronicleInChannel)
      .get();
  }

//  @Profile("ChronicleConsumer")
//  @Bean
//  public IntegrationFlow chronicleOutIntegrationFlow(QueueChannel chronicleChannel) {
//    return IntegrationFlows
//      .from(chronicleChannel)
//      .handle((payload, headers) -> {
//        log.info("reading from chronicle queue, payload: {}", payload);
//        return payload;
//      })
//      .get();
//  }

  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  public PollerMetadata poller() {
    return Pollers.fixedDelay(100).get();
  }
}
