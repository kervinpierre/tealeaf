package com.github.phuonghuynh.spring;

import com.github.phuonghuynh.model.Delivery;
import com.github.phuonghuynh.model.Drink;
import com.github.phuonghuynh.model.Order;
import com.github.phuonghuynh.model.OrderItem;
import com.google.common.util.concurrent.Uninterruptibles;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.stream.CharacterStreamWritingMessageHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 7/16/16.
 */
@Configuration
public class IntegrationConfig {

  private AtomicInteger hotDrinkCounter = new AtomicInteger();

  private AtomicInteger coldDrinkCounter = new AtomicInteger();

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
  public IntegrationFlow orders(SingleChronicleQueue chronicleQueue) {
    return f -> f
      .split(Order.class, Order::getItems)
      .channel(c -> c.executor(Executors.newCachedThreadPool()))
      .<OrderItem, Boolean>route(OrderItem::isIced, mapping -> mapping
        .subFlowMapping("true", sf -> sf
          .channel(c -> c.queue(10))
          .publishSubscribeChannel(c -> c
            .subscribe(s -> s.handle(m -> Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS)))
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
            .subscribe(s -> s.handle(m -> Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS)))
            .subscribe(sub -> sub
              .<OrderItem, String>transform(p ->
                Thread.currentThread().getName()
                  + " prepared hot drink #" + this.hotDrinkCounter.incrementAndGet()
                  + " for order #" + p.getOrderNumber() + ": " + p)
              .handle(m -> System.out.println(m.getPayload()))))))
      .<OrderItem, Drink>transform(orderItem ->
        new Drink(orderItem.getOrderNumber(),
          orderItem.getDrinkType(),
          orderItem.isIced(),
          orderItem.getShots()))
      .aggregate(aggregator -> aggregator
        .outputProcessor(g ->
          new Delivery(g.getMessages()
            .stream()
            .map(message -> (Drink) message.getPayload())
            .collect(Collectors.toList())))
        .correlationStrategy(m -> ((Drink) m.getPayload()).getOrderNumber()))
      .handle(CharacterStreamWritingMessageHandler.stdout());
  }
}
