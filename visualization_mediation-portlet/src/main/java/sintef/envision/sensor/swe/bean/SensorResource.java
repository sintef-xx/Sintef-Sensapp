package sintef.envision.sensor.swe.bean;

public class SensorResource {
	String procedure;
	String observedProperty;
	String offering;
	String location;
	
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
	public String getOffering() {
		return offering;
	}
	public void setOffering(String offering) {
		this.offering = offering;
	}
	public String getState(){
		return procedure;
	}
	public String getTags(){
		return procedure;
	}
	
	public String toString()
	{
		return offering+"*"+procedure+"*"+observedProperty+"*"+location+";";
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

}
