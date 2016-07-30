package com.github.phuonghuynh.service;

import com.github.phuonghuynh.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 7/25/16.
 */
@Slf4j
@Component
@MessageEndpoint
public class StatusActivator
{
  private static final Logger LOGGER = LogManager.getLogger(StatusActivator.class);

  @ServiceActivator(inputChannel = "inChannel", outputChannel = "outChannel")
  public Status processStatus(Status status)
  {
    LOGGER.info("processing Status: {}", status);
    status.setDesc("done");
    return status;
  }

}
