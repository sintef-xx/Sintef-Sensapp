package sintef.envision.sensor.dwr;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
//import org.directwebremoting.bridge.portlets.DWRDispatcher;
import org.springframework.stereotype.Service;

/**
 * Logging status updates of the resource API to the resource portlets status bar
 * @author pajoma
 *
 */
@Service
@RemoteProxy(name="resource-status")
public class DWRStatusUpdater {
	private static Logger log = Logger.getLogger(DWRStatusUpdater.class); 
	
	
	@RemoteMethod
	public void updateStatus(String message)  {
		ScriptSessions.addScript("updateStatus("+message+");"); 
	}
	
	
	public static void setMessage(String message, PortletRequest request, PortletResponse response) {
		
//		try {
//			DWRDispatcher dispatch = new DWRDispatcher("resource-status", "updateStatus");
//			dispatch.setParameter(message); 
//			dispatch.invokeDWRMethod(request, response);
//		} catch (IOException e) {
//			log.error("Failed to update status", e); 
//			throw new RuntimeException(e);
//		} catch (PortletException e) {
//			log.error("Failed to update status", e); 
//			throw new RuntimeException(e);
//		}
	}
	
}
