package com.github.phuonghuynh.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages="com.github.phuonghuynh")
@EnableJpaRepositories("com.github.phuonghuynh.repository")
@EntityScan(basePackages = "com.github.phuonghuynh.model")
public class DemoApplication extends SpringBootServletInitializer
{
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
  {
    return application.sources(DemoApplication.class);
  }

  public static void main(String[] args)
  {
    SpringApplication.run(DemoApplication.class, args);
  }
}
