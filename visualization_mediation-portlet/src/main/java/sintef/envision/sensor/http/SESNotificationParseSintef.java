package sintef.envision.sensor.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import sintef.envision.sensor.swe.bean.SESNotification;

public class SESNotificationParseSintef {
	
	public static void main(String[] args) throws JDOMException, IOException
	{
		new SESNotificationParseSintef().parseSESNoticiation("");
	}

	public SESNotification parseSESNoticiation(String s) throws JDOMException, IOException
	{
		SESNotification sn=null;
		try{
		 
		 SAXBuilder sb = new SAXBuilder(false);
	     Document doc = sb.build(new ByteArrayInputStream(s.getBytes()));
	     System.out.print("a");
//		 Simple1.class.getClassLoader()
//         .getResourceAsStream("test.xml"
//		 Document doc = sb.build(SESNotificationParse.class.getClassLoader().getResourceAsStream("d://a.txt"));
	     Element root = doc.getRootElement();
	     Element body=this.getElementByName(root, "Body");
	     Element notify=this.getElementByName(body, "Notify");
	     Element notificationMessage=this.getElementByName(notify, "NotificationMessage");
	     Element message=this.getElementByName(notificationMessage, "Message");
	     Element observation=this.getElementByName(message, "Observation");
	     Element samplingTime=this.getElementByName(observation, "samplingTime");
	     Element TimeInstant=this.getElementByName(samplingTime, "TimeInstant");
	     Element timePosition=this.getElementByName(TimeInstant, "timePosition");
	     String time = timePosition.getValue();
	     Element result=this.getElementByName(observation, "result");
	     String re = result.getValue();
	     Element SubscriptionReference=this.getElementByName(notificationMessage, "SubscriptionReference");
	     Element ReferenceParameters=this.getElementByName(SubscriptionReference, "ReferenceParameters");
	     Element ResourceId=this.getElementByName(ReferenceParameters, "ResourceId");
	     String resourceID=ResourceId.getValue();
	     
	     System.out.println("---------------gao-----------------------");
    	 System.out.println("time:"+time);
    	 System.out.println("result:"+re);
    	 System.out.println("resourceID:"+resourceID);
    	 sn=new SESNotification();
    	 sn.setResourceId(resourceID);
    	 sn.setResult(Double.parseDouble(re));
    	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	 Date d=sdf.parse(time.substring(0, time.length()-1));
    	 sn.setTime(d.getTime());
    	 System.out.println("----------------end----------------------");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return sn;
	}
	private Element getElementByName(Element e,String name)
	{
		 List list=e.getChildren();
		 if(list!=null)
		 for(int i=0;i<list.size();i++)
		 {
			 if(((Element)list.get(i)).getName().equals(name))
				 return (Element)list.get(i);
		 }
		 
		 return null;
	}
}
