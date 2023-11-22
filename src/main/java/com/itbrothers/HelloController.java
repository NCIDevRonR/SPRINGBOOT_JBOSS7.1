package com.itbrothers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author govindaraju.v
 *
 */
@RestController
public class HelloController {

	public HelloController() {
		System.out.println("HelloController ...");
	}
	
	@RequestMapping({ "/test.spring", "testJboss7" })
	public String testController() {
		// From URL: server-name:8080/SPRINGBOOTJBOSS/test.spring
		// Response is "Hello"
		// From URL: server-name:8080/SPRINGBOOTJBOSS/testJboss7
		// Response is "Hello"
		System.out.println("testController ...");
		return "Hello";
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		// From URL: server-name:8080/SPRINGBOOTJBOSS/hello
		// Response is "Hello World!"
		// From URL: server-name:8080/SPRINGBOOTJBOSS/hello?name=Anna
		// Response is "Hello Anna!"
		return String.format("Hello %s!", name);
	}
	
}
