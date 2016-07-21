package com.github.phuonghuynh.service;

import com.github.phuonghuynh.model.Status;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Service;

/**
 * Created by phuonghqh on 7/16/16.
 */
@Service
@MessagingGateway
public interface StatusGateway {

  @Gateway(requestChannel = "status.input")
  void sendStatus(Status status);
}
