package com.github.phuonghuynh.gateway;

import com.github.phuonghuynh.model.Order;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * Created by phuonghqh on 7/16/16.
 */
@MessagingGateway
public interface Cafe {

  @Gateway(requestChannel = "orders.input")
  void placeOrder(Order order);
}
