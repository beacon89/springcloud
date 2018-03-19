package cn.com.beacon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

	@Autowired
	private RegisterIntegerFaces registerintegerfaces;
	
	@RequestMapping(value = "/add" , method = RequestMethod.GET)
	  public String add(@RequestParam("a") Integer a,@RequestParam("b") Integer b) {
	    String string = this.registerintegerfaces.add(a,b);
	    return string;
	  }
	
}
