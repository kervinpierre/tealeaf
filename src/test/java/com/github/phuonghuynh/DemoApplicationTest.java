package com.github.phuonghuynh;

import com.github.phuonghuynh.config.DemoConfig;
import com.github.phuonghuynh.jms.JmsProducer;
import com.github.phuonghuynh.main.DemoApplication;
import com.github.phuonghuynh.model.Status;
import com.github.phuonghuynh.service.StatusGateway;
import com.github.phuonghuynh.service.StatusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebAppConfiguration
public class DemoApplicationTest
{
    private static final Logger LOGGER
            = LogManager.getLogger(DemoApplicationTest.class);

    @Resource
    private JmsProducer producer;

    @Autowired
    StatusService statusService;

    @Autowired
    DemoConfig demoConfig;

    @Autowired
    private StatusGateway statusGateway;

    @Rule
    public TestWatcher m_testWatcher = new DemoTestWatcher();

//  @Test
//  public void testSendAndReceive() throws InterruptedException, RemoteException {
//    producer.convertAndSendMessage("in.queue", status);
//    Thread.sleep(200000);
//  }

    @Test
    public void A001_testStatusGateway()
            throws InterruptedException, IOException
    {
        String testName = "A001_testStatusGateway";
        LOGGER.debug(String.format("Starting '%s'", testName));

        Path testDir = Files.createTempDirectory(testName);

        Path chronicleDir = Files.createDirectory(testDir.resolve("chronicleDir"));

        LOGGER.debug(String.format("ChronicleDir is '%s'", chronicleDir));
        demoConfig.setChroniclePath(chronicleDir.toString());

        Status statusMessage = statusService.createStatus();

        demoConfig.setUseJms(false);
        statusMessage.setDesc("test");
        statusGateway.send(statusMessage);

        Thread.sleep(5000);

        demoConfig.setUseJms(true);
        statusMessage = statusService.createStatus();
        statusMessage.setDesc("test 2");

        statusGateway.send(statusMessage);

        Thread.sleep(200000);
    }

    @Test
    public void A002_testMe()
            throws InterruptedException
    {
        demoConfig.setUseJms(false);
        for( int i = 1; i <= 400; i++ )
        {
//      Order order = new Order(i);
//      order.addItem(DrinkType.LATTE, 2, false);
//      order.addItem(DrinkType.MOCHA, 3, true);
//      cafe.placeOrder(order);

            Status stat = statusService.createStatus();
            statusGateway.send(stat);
        }

        Thread.currentThread().sleep(1_000_000);
        System.out.println("DONE");
    }

}
