package com.github.phuonghuynh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 7/30/2016.
 */
@ConfigurationProperties(prefix="pdemo")
public class DemoConfig
{
    private Boolean useJms;
    private Boolean jmsBroker;

    public Boolean getUseJms()
    {
        return useJms;
    }

    public void setUseJms( Boolean useJms )
    {
        this.useJms = useJms;
    }
}
