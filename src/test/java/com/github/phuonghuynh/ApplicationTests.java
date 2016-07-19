package com.github.phuonghuynh;

import com.github.phuonghuynh.gateway.StatusGateway;
import com.github.phuonghuynh.model.Status;
import com.github.phuonghuynh.service.StatusService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

  @Autowired
  StatusService statusService;

  @Resource
  private StatusGateway statusGateway;

  @Test
  public void testMe() throws InterruptedException {
    for (int i = 1; i <= 400; i++) {
//      Order order = new Order(i);
//      order.addItem(DrinkType.LATTE, 2, false);
//      order.addItem(DrinkType.MOCHA, 3, true);
//      cafe.placeOrder(order);

      Status stat = statusService.createStatus();
      statusGateway.sendStatus(stat);
    }

    Thread.currentThread().sleep(1_000_000);
    System.out.println("DONE");
  }

}
