package sintef.envision.sensor.dwr;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import sintef.envision.sensor.swe.Mysession;
import sintef.envision.sensor.swe.SESTool;

@Service
@RemoteProxy(name="sensorSESDWR")
public class SensorSESDwr {

	@RemoteMethod
	public String doSubscribe(String procedure, String oldId, String portletId)
	{
		doUnsubscribe(oldId);
		SESTool ses=new SESTool();
		String resourceId=ses.doSubscribe(procedure);
		Mysession.addSESSubscribe(resourceId, portletId);
		return resourceId;
	}
	
	@RemoteMethod
	public void doUnsubscribe(String resourceId)
	{
		System.out.println("doing ubsubscribe for "+resourceId);
		if(resourceId!=null&&!(resourceId.equals("")))
		{
			SESTool ses=new SESTool();
			ses.doUnsubscribe(resourceId);
			Mysession.deleteSESSubscribe(resourceId);
		}
	}
}
