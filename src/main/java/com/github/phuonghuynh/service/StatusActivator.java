package com.github.phuonghuynh.service;

import com.github.phuonghuynh.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 7/25/16.
 */
@Slf4j
@Component
@MessageEndpoint
public class StatusActivator {
  private static final Logger LOGGER = LogManager.getLogger(StatusActivator.class);

  @ServiceActivator(inputChannel = "inChannel", outputChannel = "routeChannel")
  public Status processStatus(Status status) {
    LOGGER.info("processing Status: {}", status);
    status.setDesc("processStatus");
    return status;
  }

  @Profile("ChronicleConsumer")
  @ServiceActivator(inputChannel = "chronicleChannel")
  public void receiveStatusFromChronicle(Status status) {
    LOGGER.info("receiveStatus Status: {}", status);
    status.setDesc("receiveStatusFromChronicle");
  }

  @Profile("JmsConsumer")
  @ServiceActivator(inputChannel = "jmsInChannel")
  public void receiveStatusFromJms(Status status) {
    LOGGER.info("receiveStatus Status: {}", status);
    status.setDesc("receiveStatusFromChronicle");
  }

}
