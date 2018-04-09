package cn.com.beacon.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegisterController {

	private static Logger log = LoggerFactory.getLogger(RegisterController.class);
	
	@Autowired
	private RegisterIntegerFaces registerintegerfaces;
	
	@RequestMapping(value = "/add" , method = RequestMethod.GET)
	  public String add(@RequestParam("a") Integer a,@RequestParam("b") Integer b) {
		log.info("============");
	    return this.registerintegerfaces.add(a,b);
	  }
	
}
