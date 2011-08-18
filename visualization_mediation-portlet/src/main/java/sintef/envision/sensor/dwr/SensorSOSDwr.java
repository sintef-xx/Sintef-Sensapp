package sintef.envision.sensor.dwr;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import sintef.envision.sensor.swe.SOSTool;
import sintef.envision.sensor.swe.bean.SensorObservation;
import sintef.envision.sensor.swe.bean.SensorResource;

@Service
@RemoteProxy(name="sensorSOSDWR")
public class SensorSOSDwr {
	@RemoteMethod
	public String listObservation(String url, String version, String offering, String procedure,String observedProperty)
	{
		String s=new SOSTool().sendPostToSWEAdapterForObservation(url,version,offering,procedure,observedProperty);		
		System.out.println("resultS:"+s);
//		List<SensorObservation> l=new SOSTool().parseObservation(s);
//		String result="";
//		for(int i=0;i<l.size();i++)
//		{
//			result+=l.toString();
//		}
//		System.out.println("resultSOS:"+result);
		return s;
	}
}
