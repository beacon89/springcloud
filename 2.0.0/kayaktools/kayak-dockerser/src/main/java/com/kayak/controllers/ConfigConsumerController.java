package com.kayak.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigConsumerController {

    @Autowired
    private AuthorConfig authorconfig;

    @RequestMapping("/getAuthorInfo")
    @ResponseBody
    public String getAuthorInfo(){
        return " author信息是丛git上加载下来的 ：" + authorconfig.getName().toString();
    }

}
