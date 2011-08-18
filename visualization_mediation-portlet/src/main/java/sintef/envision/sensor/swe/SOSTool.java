package sintef.envision.sensor.swe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import sintef.envision.sensor.swe.bean.SensorObservation;
import sintef.envision.sensor.swe.bean.SensorResource;


public class SOSTool {
	
	public static void main(String[] args)
	{
//		String s=new SOSTool().sendPostToSWEAdapter("http://localhost:8080/52nSOSv3_WAR/sos","1.0.0");		
////		sendPost("http://localhost:8080/SWEAdaptor/servlet/SintefSensorResource", 
////				"http://localhost:8080/52nSOSv3_WAR/sos","1.0.0");
//		List<SensorResource> l=new SOSTool().parseSensors(s);
		String s=new SOSTool().sendPostToSWEAdapterForObservation("http://localhost:8080/52nSOSv3_WAR/sos","1.0.0","Temperature_Measurement_in","urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-temperature-in","urn:ogc:def:phenomenon:OGC:1.0.30:temperature");		

		List<SensorResource> l=new SOSTool().parseSensors(s);
		for(int i=0;i<l.size();i++)
		{
			System.out.println("test:"+l.get(i).toString());
		}
	}
	
	public void getObservation()
	{
		//procedure+"*"+offering+"*"+observedProperty+"*"+result+"*"+this.getTimeLong()+";";
	}
	
	public List<SensorObservation> parseObservation(String observations)
	{
		List<SensorObservation> obList= new ArrayList<SensorObservation>();
		if(observations==null)
			return obList;
		String[] ObservationList=observations.split(";");
		if(ObservationList!=null)
			for(int i=0;i<ObservationList.length;i++)
			{
				String[] observationProperty=ObservationList[i].split("\\*");
				if(observationProperty.length>=5)
				{
					SensorObservation s=new SensorObservation();
					s.setProcedure(observationProperty[0]);
					s.setOffering(observationProperty[1]);					
					s.setObservedProperty(observationProperty[2]);
					s.setResult(observationProperty[3]);
					s.setSamplingTime(observationProperty[4]);
					obList.add(s);
				}
			}
						
		return obList;
	}
	
	public List<SensorResource> parseSensors(String sensors)
	{
		List<SensorResource> resList=new ArrayList<SensorResource>();
		
		if(sensors==null)
		{
			return resList;
		}
		String[] sensorList=sensors.split(";");
		if(sensorList!=null)
		for(int i=0;i<sensorList.length;i++)
		{
			String[] sensorProperty=sensorList[i].split("\\*");
			if(sensorProperty.length>=5)
			{
				SensorResource s=new SensorResource();
				s.setOffering(sensorProperty[0]);
				s.setProcedure(sensorProperty[1]);
				s.setObservedProperty(sensorProperty[2]);
				s.setLocation(sensorProperty[3]+"*"+sensorProperty[4]);
				resList.add(s);
			}
		}
		
		
		return resList;
	}
	
	String adapter_url="http://localhost:8080/SWEAdaptor/servlet/SintefSensorResource";
	String adapter_url_observation="http://localhost:8080/SWEAdaptor/servlet/SintefSensorObservation";
	public String sendPostToSWEAdapter(String sos_url, String version)
	{
		String data="url="+sos_url+"&version="+version;
		return new RemoteTool().swePost(data,adapter_url );
	}
		
	public String sendPostToSWEAdapterForObservation(String sos_url, String version,String offering,String procedure,String observedProperty)
	{
		String data="url="+sos_url+"&version="+version+"&offering="+offering+"&procedure="+procedure+"&observedProperty="+observedProperty;
		
		return new RemoteTool().swePost(data,adapter_url_observation );
	}
	
	
	


}
