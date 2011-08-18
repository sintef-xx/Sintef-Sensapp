package sintef.envision.sensor.swe.bean;

public class SESNotification {
	String resourceId;
	double result;
	long time;
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String toString()
	{
		return result+"*"+time+"*"+resourceId+"";
	}
}
