package com.game.kalahaui;

import java.util.List;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class KalahaUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KalahaUiApplication.class, args);
	}

	@Bean
	ServletRegistrationBean<Servlet> jsfServletRegistration (ServletContext servletContext) {
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

		ServletRegistrationBean<Servlet> srb = new ServletRegistrationBean<>();
		srb.setServlet(new FacesServlet());
		srb.setUrlMappings(List.of("*.xhtml"));
		srb.setLoadOnStartup(1);
		return srb;
	}
}
