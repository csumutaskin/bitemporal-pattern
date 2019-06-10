package tr.com.poc.temporaldate.core.configuration.profiling;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("preprod")
@PropertySource("classpath:preprod/application-preprod.properties")
public class PreprodConfiguration 
{
	@PostConstruct
	public void printInfo()
	{
		
	}
}
