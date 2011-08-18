package sintef.envision.sensor.swe;

public class SESTool {
	String adapter_url="http://localhost:8080/SWEAdaptor/servlet/SintefSensorSES";
	String sesurl="http://localhost:8080/ses-main-1.0-SNAPSHOT";
	String sesversion="0.0.0";
	String listenPort="localhost:9089";
	public String doSubscribe(String procedure)
	{
		String data="sesAction="+"subscribe"+"&url="+sesurl+"&version="+sesversion+"&listenPort="+listenPort+"&procedure="+procedure;
		
		return new RemoteTool().swePost(data,adapter_url );
	}
	
	public void doUnsubscribe(String resourceId)
	{
		String data="sesAction="+"unsubscribe"+"&url="+sesurl+"&version="+sesversion+"&resourceId="+resourceId;
		//System.out.println("doUnsubscribe:"+data);
		new RemoteTool().swePost(data,adapter_url );
	}
	
	public static void main(String[] args)
	{
		new SESTool().doUnsubscribe("MuseResource-4");
		new SESTool().doUnsubscribe("MuseResource-3");

	}
}
