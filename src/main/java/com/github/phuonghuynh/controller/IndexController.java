package com.github.phuonghuynh.controller;

import com.github.phuonghuynh.model.Status;
import com.github.phuonghuynh.service.StatusGateway;
import com.github.phuonghuynh.service.StatusService;
import com.github.phuonghuynh.util.JMSServerUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by kervin on 2016-07-18.
 */
@Controller("/")
public class IndexController
{
    private static final Logger LOGGER = LogManager.getLogger(IndexController.class);

    @Autowired
    StatusService statusService;

    @Resource
    private StatusGateway statusGateway;

    @RequestMapping("/")
    public String index(HttpServletRequest request) throws Exception
    {
        // FIXME: Should be started conditionally
        // FIXME: Should be started in a background thread
        JMSServerUtil.startServer();

        for( int i=0; i<10; i++ )
        {
            try
            {
                Thread.sleep(RandomUtils.nextLong(0, 5000));
            }
            catch( InterruptedException ex )
            {
                LOGGER.debug("Sleep interrupted", ex);
            }

            Status stat = statusService.createStatus();
            statusGateway.sendStatus(stat);
        }

        return "index";
    }
}
