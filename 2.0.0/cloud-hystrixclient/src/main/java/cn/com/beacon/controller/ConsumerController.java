package cn.com.beacon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.beacon.remote.HelloRemote;

@RestController
public class ConsumerController {
	
	@Autowired
    HelloRemote helloremote;
	
    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
    		System.out.println("1111");
        return helloremote.hello(name);
    }
}
