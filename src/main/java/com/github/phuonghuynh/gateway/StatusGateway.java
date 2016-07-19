package com.github.phuonghuynh.gateway;

import com.github.phuonghuynh.model.Status;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * Created by phuonghqh on 7/16/16.
 */
@MessagingGateway
public interface StatusGateway {

  @Gateway(requestChannel = "status.input")
  void sendStatus(Status status);
}
