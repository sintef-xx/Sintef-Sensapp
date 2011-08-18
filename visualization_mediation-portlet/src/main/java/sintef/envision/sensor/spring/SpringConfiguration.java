package sintef.envision.sensor.spring; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.ifgi.envision.resources.ResourceController;
import de.ifgi.envision.resources.jcrclient.JcrConfiguration;

@Configuration
public class SpringConfiguration {

	
	@Bean
	public ResourceController initResourceController() {
		//JcrConfiguration config = new JcrConfiguration("http://giv-wfs.uni-muenster.de/jcr/", "guest", "guest", "testing"); 
		return new ResourceController(); 
	}
}
