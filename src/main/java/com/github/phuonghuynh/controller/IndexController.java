package com.github.phuonghuynh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kervin on 2016-07-18.
 */
@Controller("/")
public class IndexController
{
    @RequestMapping("/")
    public String index(HttpServletRequest request)
    {
        return "index";
    }
}
