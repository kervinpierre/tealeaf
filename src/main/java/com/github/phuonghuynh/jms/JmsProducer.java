package com.github.phuonghuynh.jms;

import com.github.phuonghuynh.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 7/25/16.
 */
@Component
public class JmsProducer {

  @Resource
  private JmsTemplate jmsTemplate;

  public void convertAndSendMessage(Status status) {
    jmsTemplate.convertAndSend(status);
  }

  public void convertAndSendMessage(String destination, Status status) {
    jmsTemplate.convertAndSend(destination, status);
  }
}
