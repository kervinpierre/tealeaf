package com.github.phuonghuynh;

import com.github.phuonghuynh.gateway.Cafe;
import com.github.phuonghuynh.model.DrinkType;
import com.github.phuonghuynh.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ApplicationTests {

  @Resource
  private Cafe cafe;

  @Test
  public void testMe() throws InterruptedException {
    for (int i = 1; i <= 400; i++) {
      Order order = new Order(i);
      order.addItem(DrinkType.LATTE, 2, false);
      order.addItem(DrinkType.MOCHA, 3, true);
      cafe.placeOrder(order);
    }

    Thread.currentThread().sleep(1_000_000);
    System.out.println("DONE");
  }

}
