package sintef.envision.sensor.portlets.mediation;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import sintef.envision.sensor.swe.Mysession;



@Controller 
@RequestMapping("VIEW")
public class MediationPortlet {
	Logger log = Logger.getLogger(MediationPortlet.class.getName()); 
	
	private String getUserEmail(PortletRequest request) {
		Map userInfo = (Map) request.getAttribute(PortletRequest.USER_INFO); 
		String remoteUser = (userInfo != null) ? (String)userInfo.get("user.home-info.online.email") : null;
		
		return remoteUser; 
	}
	
	@RenderMapping
	public ModelAndView handleRenderRequest(RenderRequest request, RenderResponse response) throws Exception 
	{ 	 
		ModelAndView mav = null; 
		
		String remoteUser = this.getUserEmail(request); 
		
		
		if(remoteUser == null) {
			return new ModelAndView("not_authenticated");  
		} 
		//rc = getResourceController(remoteUser); 
	//	rc=null;
	//	rc=getResourceController(remoteUser); 
		mav = new ModelAndView("mediation"); 	
		System.out.println("mediation");
		return mav;
	}
	
	@ActionMapping(params = "action=mediation")
	public void actionSendEvent(SessionStatus status, ActionRequest request, ActionResponse response) throws Exception  {
		

	}
	
	@EventMapping(value = "{http://liferay.com/events}envision.resource.mediation")
	public void eventBuildMediation(EventRequest request, EventResponse response) throws Exception {
		String sid=(String)request.getPortletSession().getId();
		ScriptBuffer scriptbuffer = new ScriptBuffer("drawMediationStart();");
		Mysession.getSessionById(sid).addScript(scriptbuffer);
		System.out.println("mediationAction:");
	}

	@EventMapping(value = "{http://liferay.com/events}envision.resource.sendResource")
	public void eventShowAllMap(EventRequest request, EventResponse response) throws Exception {
		
	}
}
