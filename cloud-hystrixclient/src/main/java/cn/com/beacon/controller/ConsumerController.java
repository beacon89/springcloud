package cn.com.beacon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.beacon.remote.HelloRemote;

@RestController
@RefreshScope
public class ConsumerController {
	
	@Autowired
    HelloRemote helloremote;
	
    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        return helloremote.hello(name);
    }
}
