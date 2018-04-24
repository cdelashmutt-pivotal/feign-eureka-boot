package io.pivotal.pa.auditserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class AuditServerApplication {
	@Autowired
	Registration registration;

	@RequestMapping("/")
	public String hello() {
		return "Hello World: "+ registration.getServiceId()+":"+registration.getHost()+":"+registration.getPort();
	}

	public static void main(String[] args) {
		SpringApplication.run(AuditServerApplication.class, args);
	}

	//Turns on security only for the actuators
	@Configuration
	public static class ActuatorSecurity extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests()
					.anyRequest().hasRole("ENDPOINT_ADMIN")
					.and()
					.httpBasic();
		}
	}

}
