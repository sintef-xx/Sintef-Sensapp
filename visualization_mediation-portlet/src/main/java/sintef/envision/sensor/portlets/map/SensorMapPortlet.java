package sintef.envision.sensor.portlets.map;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jcr.RepositoryException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.MimeResponse;
import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.namespace.QName;

import org.apache.tika.mime.MimeType;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
//import org.directwebremoting.bridge.portlets.DWRDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.w3c.dom.Element;

import sintef.envision.sensor.swe.Mysession;

import de.ifgi.envision.resources.ResourceController;
import de.ifgi.envision.resources.helper.Construct;
import de.ifgi.envision.resources.helper.ResourceLookup;
import de.ifgi.envision.resources.jcrclient.JcrConfiguration;
import de.ifgi.envision.resources.model.Reference;
import de.ifgi.envision.resources.model.Resource;
import de.ifgi.envision.resources.model.enums.ReferenceType;
import de.ifgi.envision.resources.model.enums.Types;
import de.ifgi.envision.resources.transformers.SerializeResource;

/**
 * @author gaoxiaoxin
 *
 */
@Controller 
@RequestMapping("VIEW")
public class SensorMapPortlet  { 
	Logger log = Logger.getLogger(SensorMapPortlet.class.getName()); 
	
	
	private @Autowired ResourceController rc;
	private List<String> callbacks = null;
	
	

	// http://localhost:8080/web/guest/home?p_p_id=ResourceManager_WAR_resourceportlet03&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_resource_id=callbacks&p_p_cacheability=cacheLevelPage&p_p_col_id=column-1&p_p_col_count=2


	

	private ResourceController getResourceController(String remoteUser) throws RepositoryException, IOException {
		if(rc == null) {
			// note, this will be replaced for the review with the workspace of user john doe (last parameter)
			//JcrConfiguration config = new JcrConfiguration("http://giv-wfs.uni-muenster.de/jcr/", "guest", "guest", remoteUser); 
			JcrConfiguration config = new JcrConfiguration("http://giv-wfs.uni-muenster.de/jcr/", "guest", "guest", "testing"); 
			rc = new ResourceController(config); 
			rc.refresh(); 
		}
		return rc; 
	}
	
	/**
	 * The normal render request, returning the default list view
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
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
		mav = new ModelAndView("sensor_map"); 	
		System.out.println("sensor_map");
		return mav;
	}
	



	/**
	 * Extracts the user email from the portlet request. This has to be configured in the portlet descriptor xml. 
	 * @param request
	 * @return
	 */
	private String getUserEmail(PortletRequest request) {
		Map userInfo = (Map) request.getAttribute(PortletRequest.USER_INFO); 
		String remoteUser = (userInfo != null) ? (String)userInfo.get("user.home-info.online.email") : null;
		
		return remoteUser; 
	}


	/**
	 * Action triggering the Event "envision.resource.sendResource". This function does not return anything interesting, but other 
	 * portlets may respond to it. If any action on the client side should be triggered, they have to register a javascript callback
	 * function as string in the shared session (using "callback{X}" as name). 
	 * 
	 * @param status
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ActionMapping(params = "action=map-sending")
	public void actionSendEvent(SessionStatus status, ActionRequest request, ActionResponse response) throws Exception  {
		String action=(String) request.getParameter( "sensorAction");
		String sensorData=(String) request.getParameter( "sensorData");
		String sosurl=(String) request.getParameter( "url_sos");
		String sosversion=(String) request.getParameter( "version_sos");
		System.out.println("sensorData:"+sensorData);
		QName qname=null;
		if(action.equals("SOS"))
		{
			qname= new QName("http://liferay.com/events","envision.sensor.sosAction");
			response.setEvent(qname, sensorData+"*"+sosurl+"*"+sosversion+"");
			System.out.print("send event envision.sensor.sosAction");
		}
		else if(action.equals("SES"))
		{
			qname= new QName("http://liferay.com/events","envision.sensor.sesAction");
			response.setEvent(qname, sensorData);
			System.out.print("send event envision.sensor.sesAction");
		}
		
		status.setComplete();

	}
	

	


	@EventMapping(value = "{http://liferay.com/events}envision.sensor.showAllMap")
	public void eventShowAllMap(EventRequest request, EventResponse response) throws Exception {
		String sosurl=(String)request.getAttribute("sosurl");
		String sosversion=(String)request.getAttribute("sosversion");
		
		String sensorList = request.getEvent().getValue().toString();
		String sid=(String)request.getPortletSession().getId();
		ScriptBuffer scriptbuffer = new ScriptBuffer("initAllSensorMap('"+sensorList+"');");
		Mysession.getSessionById(sid).addScript(scriptbuffer);
		ScriptBuffer scriptbuffer2 = new ScriptBuffer("initSOSInfo('"+sosurl+"','"+sosversion+"');");
		Mysession.getSessionById(sid).addScript(scriptbuffer2);
		
		System.out.println();
		System.out.println(sensorList);
	}
	
	@EventMapping(value = "{http://liferay.com/events}envision.sensor.showOneMap")
	public void eventShowOneMap(EventRequest request, EventResponse response) throws Exception {
		String sosurl=(String)request.getAttribute("sosurl");
		String sosversion=(String)request.getAttribute("sosversion");
		
		String sensor=request.getEvent().getValue().toString();
		String sid=(String)request.getPortletSession().getId();
		ScriptBuffer scriptbuffer = new ScriptBuffer("initSelectedSensorMap('"+sensor+"');");
		Mysession.getSessionById(sid).addScript(scriptbuffer);
		ScriptBuffer scriptbuffer2 = new ScriptBuffer("initSOSInfo('"+sosurl+"','"+sosversion+"');");
		Mysession.getSessionById(sid).addScript(scriptbuffer2);
		System.out.println(sensor);
	}
	
//	@EventMapping(value = "{http://liferay.com/events}envision.resource.refresh")
//	public void eventRefreshList(EventRequest request, EventResponse response) throws Exception {
//		DWRDispatcher dispatcher = new DWRDispatcher("resources", "refresh"); 
//		
//		dispatcher.invokeDWRMethod(request, response);
//	}

	
}