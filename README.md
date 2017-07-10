# SPRINGBOOT_JBOSS7.1


This is sample spring boot application for running a Spring Boot application on JBoss 7.1.x

Steps :
1. Create a spring boot startr project with java v 1.7 (jboss7 does not support java8) and packaging as war.
2. After creating project change pom.xml
	a) exclude embedded tomcat
	b) war file name change in build - <finalName>SPRINGBOOTJBOSS</finalName> 
3. create test RestController (HelloController.java)
	a)  test by run as spring boot 
		- which starts embedded tomcat.
		- localhost:8080/test.spring (or) localhost:8080/testJboss7 works fine


# Code Snippet

- HelloController.java
```
package com.itbrothers;


import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping({"/test.spring","testJboss7"})
	public String testController(){
		System.out.println("testController ...");
		return "Hello";
	}
	
}
```
- pom.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.itbrothers</groupId>
	<artifactId>citadel</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>war</packaging>

	<name>SpringBootJboss7</name>
	<description>Spring boot Deployment with jboss 7.1</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.5.4.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.7</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-tomcat</artifactId>
				</exclusion>
			</exclusions>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-tomcat</artifactId>
			<scope>provided</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<finalName>SPRINGBOOTJBOSS</finalName>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>


</project>
```


# Test in external JBOSS
 - 	deploy the war file in jboss 7.1 
 - in web browser access localhost:8080/test.spring (or) localhost:8080/testJboss7
 - Error HTTP - Status 404 - /SPRINGBOOTJBOSS/test.spring 
 - Error HTTP - Status 404 - /SPRINGBOOTJBOSS/testJboss7

# Changes for JBOSS7.1 deployment

- Following changes required to successfully access restController end points.

 1) Delete or comment class ServletInitializer.java which was generated by STS as a part of spring boot starter(war package)
 2) create class JbossWebAppInitializer which implements WebApplicationInitializer
 		a) Register spring AnnotationConfigWebApplicationContext with configuration and its component scan.
 		
 - # Code Snippet for JBOSS7.1 deployment
```
   - a ) comment existing or delete class ServletInitializer
   
   //package com.itbrothers;
//
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.context.web.SpringBootServletInitializer;
//
//public class ServletInitializer extends SpringBootServletInitializer {
//
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(SpringBootJboss7Application.class);
//	}
//
//}
```   
   
   
   - (b) AppConfs.java 
```
   package com.itbrothers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages="com.itbrothers")
public class AppConfs {
	public AppConfs() {
		System.out.println("AppConfs constructor..");
	}

	/*	@Bean
	public MyBean1 MyBean1(){
		
	}*/
}
```
    
   - (c) JbossWebAppInitializer.java
   ```
package com.itbrothers;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class JbossWebAppInitializer implements WebApplicationInitializer {

	public JbossWebAppInitializer() {
		System.out.println("JbossWebAppInitializer constructor");
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println("JbossWebAppInitializer.onStartup()");
		
        AnnotationConfigWebApplicationContext ctx = getContext();
        ctx.setServletContext(servletContext);
        Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        //servlet.addMapping("*.spring");
        servlet.addMapping("/*");
        servlet.setLoadOnStartup(1);
	}
	private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = null;
        try {
                context = new AnnotationConfigWebApplicationContext();
                //context.register(WebConfig.class);
                context.register(AppConfs.class);
        } catch (Exception e) {
     	   e.printStackTrace();
        }
     return context;
 }

}
   ```
- ### (d) /pom.xml Exclude embedded tomcat if not done already.
```
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
		<exclusions>
			<exclusion>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-starter-tomcat</artifactId>
			</exclusion>
		</exclusions>
	</dependency>
```
   
   	

