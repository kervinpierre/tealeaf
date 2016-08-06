package demo;

import com.github.phuonghuynh.config.DemoConfig;
import com.github.phuonghuynh.main.DemoApplication;
import com.github.phuonghuynh.model.Status;
import com.github.phuonghuynh.service.StatusGateway;
import com.github.phuonghuynh.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Phuonghqh on 8/5/16.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class SendChronicleTest {

  @Resource
  private StatusGateway statusGateway;

  @Resource
  private DemoConfig demoConfig;

  @Resource
  private StatusService statusService;

  @Before
  public void before() {
    demoConfig.setUseJms(false);
  }

  @Test
  public void testSendStatus() throws InterruptedException {
    Status statusMessage = statusService.createStatus();
    statusGateway.send(statusMessage);
    log.info("Send Status to Chronicle Queue, see {} for processing function", "com.github.phuonghuynh.service.StatusActivator.sendStatus");
    Thread.sleep(5000);
  }
}
