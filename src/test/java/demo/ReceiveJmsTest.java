package demo;

import com.github.phuonghuynh.config.DemoConfig;
import com.github.phuonghuynh.main.DemoApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Phuonghqh on 8/5/16.
 */
@Slf4j
@ActiveProfiles("JmsConsumer")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class ReceiveJmsTest {
  @Resource
  private DemoConfig demoConfig;

  @Before
  public void before() {
    demoConfig.setUseJms(true);
  }


  @Test
  public void testReceiveStatus() throws InterruptedException {
    log.info("see Status received from Jms in function {}", "com.github.phuonghuynh.service.StatusActivator.receiveStatusFromJms");
    Thread.sleep(5000);
  }
}
