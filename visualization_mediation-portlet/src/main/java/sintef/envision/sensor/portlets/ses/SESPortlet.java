package sintef.envision.sensor.portlets.ses;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;

import org.directwebremoting.ScriptBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import sintef.envision.sensor.http.LocalHttpServer;
import sintef.envision.sensor.portlets.map.SensorMapPortlet;
import sintef.envision.sensor.swe.Mysession;
import de.ifgi.envision.resources.ResourceController;

@Controller 
@RequestMapping("VIEW")
public class SESPortlet {
	Logger log = Logger.getLogger(SESPortlet.class.getName()); 
	
	
	//private @Autowired ResourceController rc;
	private List<String> callbacks = null;
	private LocalHttpServer server=null;
	@RenderMapping
	public ModelAndView handleRenderRequest(RenderRequest request, RenderResponse response) throws Exception 
	{ 	 
		ModelAndView mav = null; 
		
		String remoteUser = this.getUserEmail(request); 
		
		
		if(remoteUser == null) {
			return new ModelAndView("not_authenticated");  
		} 
		//rc = getResourceController(remoteUser); 
		//rc=null;
	//	rc=getResourceController(remoteUser); 
		mav = new ModelAndView("sensor_ses"); 	
		System.out.println("sensor_ses");
		try{
		server=new LocalHttpServer(9089);
		server.initialize();
		}
		catch(Exception e)
		{
			
		}
		System.out.println("begin to listen at port 9089");
		return mav;
	}
	private String getUserEmail(PortletRequest request) {
		Map userInfo = (Map) request.getAttribute(PortletRequest.USER_INFO); 
		String remoteUser = (userInfo != null) ? (String)userInfo.get("user.home-info.online.email") : null;
		
		return remoteUser; 
	}
	@ActionMapping(params = "action=ses-sending")
	public void actionSendEvent(SessionStatus status, ActionRequest request, ActionResponse response) throws Exception  {

		
		status.setComplete();

	}
	@EventMapping(value = "{http://liferay.com/events}envision.sensor.sesAction")
	public void eventShowOneMap(EventRequest request, EventResponse response) throws Exception {
		String sensor=request.getEvent().getValue().toString();
		String sid=(String)request.getPortletSession().getId();
		ScriptBuffer scriptbuffer = new ScriptBuffer("initDataSES('"+sensor+"');");
		Mysession.getSessionById(sid).addScript(scriptbuffer);
		System.out.println("sesAction:"+sensor);
	}
}
