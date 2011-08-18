package sintef.envision.sensor.swe.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorObservation {

	String result;
	String samplingTime;
	String offering;
	String procedure;
	String observedProperty;
	String featureOfInterest;
	
	public String toString()
	{
		return procedure+"*"+offering+"*"+observedProperty+"*"+result+"*"+samplingTime+";";
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSamplingTime() {
		return samplingTime;
	}
	public void setSamplingTime(String samplingTime) {
		this.samplingTime = samplingTime;
	}
	public String getOffering() {
		return offering;
	}
	public void setOffering(String offering) {
		this.offering = offering;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public String getObservedProperty() {
		return observedProperty;
	}
	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
	}
	public String getFeatureOfInterest() {
		return featureOfInterest;
	}
	public void setFeatureOfInterest(String featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}
	public long getTimeLong()
	{
		try
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date d=sdf.parse(this.getSamplingTime());
			return d.getTime();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}
}
