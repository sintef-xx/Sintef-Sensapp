package sintef.envision.sensor.portlets.manager;

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
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.bridge.portlets.DWRDispatcher;
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
import sintef.envision.sensor.swe.SOSTool;

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
public class ResourceManagerPortlet  { 
	Logger log = Logger.getLogger(ResourceManagerPortlet.class.getName()); 
	
	
	private @Autowired ResourceController rc;
	private List<String> callbacks = null;
	int first=1;
	
	

	// http://localhost:8080/web/guest/home?p_p_id=ResourceManager_WAR_resourceportlet03&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_resource_id=callbacks&p_p_cacheability=cacheLevelPage&p_p_col_id=column-1&p_p_col_count=2


	
	@RenderMapping(params = "action=resource-sending") 
	public void handleResourceSending(@RequestParam String action, RenderRequest request, RenderResponse response, Model model, Writer output) throws IOException {
		/* this does unfortunately not help, the response is always the complete portal page, anything we write in there would have to be scraped 
		 * from the HTML (which we don't really want to do)
		 */
		}
	private ResourceController getResourceController(String remoteUser) throws RepositoryException, IOException {
		if(rc == null) {
			// note, this will be replaced for the review with the workspace of user john doe (last parameter)
			//JcrConfiguration config = new JcrConfiguration("http://giv-wfs.uni-muenster.de/jcr/", "guest", "guest", remoteUser); 
			JcrConfiguration config = new JcrConfiguration("http://giv-wfs.uni-muenster.de/jcr/", "envision", "envision", remoteUser); 
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
		if(first==1)
		{
			rc=null;
			first=0;
		}
		rc=getResourceController(remoteUser); 
		mav = new ModelAndView("list_resources"); 	
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
	@ActionMapping(params = "action=sensors-sending")
	public void actionSendEvent(SessionStatus status, ActionRequest request, ActionResponse response) throws Exception  {
		
		String action=(String) request.getParameter( "sensorAction");
		//String dwrSession=(String)request.getParameter("dwrSession");
		
		log.info("Action: "+action); 
		System.out.print("Action-print: "+action);
		
		
		
		QName qname=null;
		if(action!=null)
		{
			//request.setAttribute("dwrSession", dwrSession);
			String sosurl=(String) request.getParameter( "url_sos");
			String sosversion=(String) request.getParameter( "version_sos");
			request.setAttribute("sosurl", sosurl);
			request.setAttribute("sosversion", sosversion);
			if(action.equals("showAllMapAction"))
			{
				
				String s=new SOSTool().sendPostToSWEAdapter(sosurl,sosversion);
				
				qname= new QName("http://liferay.com/events","envision.sensor.showAllMap");
				
				response.setEvent(qname, s); //here need to figure out what kind of paramter need to be sent to mapviewer
				System.out.println("send event envision.sensor.showAllMap");
			}
			else {
				
				String procedure=(String) request.getParameter( "procedure");
				String offering=(String) request.getParameter( "offering");
				String observedProperty=(String) request.getParameter( "observedProperty");
				request.setAttribute("procedure", procedure);
				request.setAttribute("offering", offering);
				request.setAttribute("observedProperty", observedProperty);
				
				if(action.equals("showOneMapAction"))
				{
					String location = (String) request.getParameter( "location");
					
					qname= new QName("http://liferay.com/events","envision.sensor.showOneMap");
					response.setEvent(qname, offering+"*"+procedure+"*"+observedProperty+"*"+location+"");
					System.out.print("send event envision.sensor.showOneMap");
				}
				else if(action.equals("sosAction"))
				{
					qname= new QName("http://liferay.com/events","envision.sensor.sosAction");
					response.setEvent(qname, offering+"*"+procedure+"*"+observedProperty+"*"+sosurl+"*"+sosversion+"");
					System.out.print("send event envision.sensor.sosAction");
				}
				else if(action.equals("sesAction"))
				{
					qname= new QName("http://liferay.com/events","envision.sensor.sesAction");
					response.setEvent(qname, offering+"*"+procedure+"*"+observedProperty);
					System.out.print("send event envision.sensor.sesAction");
					
				}
			}
			
		}
		status.setComplete();
	  	
//		log.info("Action: Sending resource with id ["+rid+"] to other portlets. "); 
//		
//		// events are fired AFTER this method, we can't directly wait for an response
//	  	response.setEvent(qname, rid);
//	  	request.setAttribute("dwrSession", dwrSession);
//	  	System.out.println("send resource-resource portlet:"+rid);
//	  	response.setEvent("session", request.getPortletSession().getId());
//	  	response.setRenderParameter("action", "resource-sending"); 	// to trigger the correct render method
//    	status.setComplete();
	}
	

	


	@EventMapping(value = "{http://liferay.com/events}envision.resource.sendResource")
	public void eventSendResource(EventRequest request, EventResponse response) throws Exception {
		// here, need to parse the resource to get the sos url
		try{
					
//			DWRDispatcher dispatcher = new DWRDispatcher("sensors", "test");
//			dispatcher.setPathToDWRServlet("");
//			dispatcher.setParameter((String)request.getPortletSession().getId());
//			dispatcher.invokeDWRMethod(request, response);

			
		String sid=(String)request.getPortletSession().getId();
		Mysession.getSessionById(sid).addScript(new ScriptBuffer("env.sensors.showWaitMessage();"));	
		
		String resource_hash = request.getEvent().getValue().toString();
		//String resource_hash = "SOS10_BrgmSosForAdes";
		System.out.println("resource_hash:"+resource_hash);
		ResourceController rc = getResourceController(this.getUserEmail(request)); 
		Resource resource = rc.findResource(resource_hash);
		
		String SOSUrl="http://localhost:8080/52nSOSv3_WAR/sos";
		
		if(resource!=null)
		{
			String base = Construct.constructBaseURL(rc,resource );
			System.out.println("base:"+base);

			Types resourcetype = Types.identify(resource.getType());
			if(!((""+resourcetype).startsWith("SOS")))
			{
					//alert in the client part
			}
			//Reference reference = ResourceLookup.getReference(resource, ReferenceType.URL_OGC);	
			List<Reference> lr=resource.getReferences();
			URL l=null;
			for(int i=0;i<lr.size();i++)
			{
				try
				{
					if(lr.get(i).getType().equals("url:ogc"))
					{
						String[] lss=base.split(":");
						base=lss[0]+":"+lss[1];
						l = Construct.referenceURL(base, lr.get(i)); 
						//rf_use=lr.get(i);
						System.out.println("sosUrl:"+l);
					}			
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}				
			}	
			
			//=Construct.referenceURL(base, reference);
			
			SOSUrl=""+l;
		}
		
		
		String version="1.0.0";
		System.out.print("get a sendResource event");

		
		System.out.println("sid:"+sid);
		ScriptBuffer scriptbuffer = new ScriptBuffer("env.sensors.initSensors('"+SOSUrl+"','"+version+"');");
		//Mysession.getSessionById(sid).addScript(scriptbuffer);	
		ScriptSession se=Mysession.getSessionById(sid);
		if(se!=null)
		{
			System.out.println("ss:"+se.getId());
			System.out.println("invoke:"+scriptbuffer.toString());
			se.addScript(scriptbuffer);
		}
//		ScriptBuffer scriptbuffer2 = new ScriptBuffer("sayhello();");
//		Mysession.getSessionById(sid).addScript(scriptbuffer2);
		
		System.out.println("finish");
		
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
//	@EventMapping(value = "{http://liferay.com/events}envision.resource.refresh")
//	public void eventRefreshList(EventRequest request, EventResponse response) throws Exception {
//		DWRDispatcher dispatcher = new DWRDispatcher("resources", "refresh"); 
//		
//		dispatcher.invokeDWRMethod(request, response);
//	}

	
}