package com.github.phuonghuynh.service;

import com.github.phuonghuynh.model.Status;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Payload;
/**
 * Created by phuonghqh on 7/16/16.
 */
@MessagingGateway
public interface StatusGateway
{
  @Gateway(requestChannel = "inChannel")
  void send(@Payload Status status);
}
