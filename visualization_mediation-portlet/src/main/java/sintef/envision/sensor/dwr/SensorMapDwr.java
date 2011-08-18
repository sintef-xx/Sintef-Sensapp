package sintef.envision.sensor.dwr;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ifgi.envision.resources.ResourceController;

@Service
@RemoteProxy(name="sensorMap")
public class SensorMapDwr {
	private @Autowired ResourceController rc; 
	
	@RemoteMethod
	public void loadSensors()
	{
		
	}
}
