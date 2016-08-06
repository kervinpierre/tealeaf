package com.github.phuonghuynh.jms;

import com.github.phuonghuynh.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 7/26/16.
 */
@Slf4j
@Profile("JmsConsumer")
//@Component
public class JmsConsumer {

  @JmsListener(destination = "out.queue")
  public void listen(Status data) {
    log.info("Received data: {}", data);
  }
}
