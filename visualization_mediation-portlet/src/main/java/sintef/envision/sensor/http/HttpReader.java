package sintef.envision.sensor.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.directwebremoting.ScriptBuffer;
import org.jdom.JDOMException;

import sintef.envision.sensor.swe.Mysession;
import sintef.envision.sensor.swe.bean.SESNotification;
import sintef.envision.sensor.http.SESNotificationParseSintef;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



/**
 * Simple HTTP reader that displays all incoming requests
 * and answers with 'HTTP 200 (OK)'.
 * 
 * @author Thomas Everding
 *
 */
public class HttpReader implements HttpHandler {
	
//	private DisplayGUI display;
	
	private int connectionNumber = 1;
	
	
	/**
	 * 
	 * Constructor
	 *
	 */
	public HttpReader() {
		//build display
		
	}

	
	public void handle(HttpExchange httpExchange) throws IOException {
		//get request
		InputStream in = httpExchange.getRequestBody();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		//display request

		connectionNumber++;
		
		String s;
		String sesnofity="";
		while ((s = reader.readLine()) != null) {			
			sesnofity+=s;			
		}
		try {					
			SESNotification sn=new SESNotificationParseSintef().parseSESNoticiation(sesnofity);
			if(sn!=null)
			{
				System.out.println("notifycation_sn:"+sn.toString());
				String resourceId=sn.getResourceId();
				if(resourceId!=null)
				{
					ScriptBuffer scriptbuffer = new ScriptBuffer("initPointSES('"+sn.toString()+"');");
					Mysession.getSesstionByResourceId(resourceId).addScript(scriptbuffer);//here add point in the chart
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//build response and close connection
		String response = ResponseBuilder.buildResponseToAnonymous();
		httpExchange.sendResponseHeaders(200, response.length());
		
		OutputStream out = httpExchange.getResponseBody();
		out.write(response.getBytes());
		out.flush();
		
		//close connection
		out.close();
		reader.close();
		in.close();
	}

}
