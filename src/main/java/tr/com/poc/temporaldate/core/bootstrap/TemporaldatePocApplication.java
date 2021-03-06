package tr.com.poc.temporaldate.core.bootstrap;

import static tr.com.poc.temporaldate.common.CommonConstants.SCAN_PATH_JPA_ENTITIES;
import static tr.com.poc.temporaldate.common.Constants.SCAN_PATH_CORE_JPA_ENTITIES;
import static tr.com.poc.temporaldate.common.CommonConstants.SCAN_PATH_SPRING_COMPONENTS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Bootstrap class for boot application
 * @author umutaskin
 *
 */
@SpringBootApplication
@ComponentScan(SCAN_PATH_SPRING_COMPONENTS)
@EntityScan( basePackages = {SCAN_PATH_CORE_JPA_ENTITIES, SCAN_PATH_JPA_ENTITIES} )
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TemporaldatePocApplication
{
	public static void main(String[] args) 
	{
		SpringApplication.run(TemporaldatePocApplication.class, args);
	}
}
