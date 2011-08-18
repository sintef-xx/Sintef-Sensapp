package sintef.envision.sensor.portlets.display;

import javax.annotation.PostConstruct;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.namespace.QName;

//import org.directwebremoting.bridge.portlets.DWRDispatcher;
import org.directwebremoting.bridge.portlets.DWRDispatcher;
import org.directwebremoting.extend.ProtocolConstants;
import org.directwebremoting.spring.DwrController;
import org.directwebremoting.ui.dwr.Util;
import org.directwebremoting.util.FakeHttpServletRequest;
import org.directwebremoting.util.FakeHttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.EventMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import de.ifgi.envision.resources.ResourceController;
import de.ifgi.envision.resources.jcrclient.JcrConfiguration;
import de.ifgi.envision.resources.model.Resource;
import de.ifgi.envision.resources.model.enums.States;
import de.ifgi.envision.resources.transformers.ParseResource;

/**
 * Nothing fancy, this portlet consumes the event from the ResourceManager and
 * displays all the information from the delivered resource
 * 
 * @author pajoma
 * 
 */
@Controller
@RequestMapping("VIEW")
public class SimpleResourceDisplayPortlet {




	@RenderMapping
	public ModelAndView handleRenderRequest(RenderRequest request, RenderResponse response) throws Exception {
		ModelAndView mav = null; 
		String remoteUser = request.getRemoteUser(); 
		
		if(remoteUser == null) {
			return new ModelAndView("not_authenticated");  
		} 
		
		mav = new ModelAndView("explain_resource");  
		return mav; 
	}

	/**
	 * Dispatching to DWR to update our view
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@EventMapping(value = "{http://liferay.com/events}envision.resource.sendResource")
	public void processResource(EventRequest request, EventResponse response) throws Exception {
		String resource_hash = request.getEvent().getValue().toString();
		String dwrSession=(String)request.getAttribute("dwrSession");
		System.out.println("dwrSession-2:"+dwrSession);
		DWRDispatcher dispatcher = new DWRDispatcher("resourceDisplay2", "updateResourceDescription2" );
		dispatcher.setParameter(resource_hash,dwrSession); 
		dispatcher.setPathToDWRServlet("");
		
		dispatcher.invokeDWRMethod(request, response);
		
//		DWRDispatcher dispatcher2 = new DWRDispatcher("sensors", "test");
//		dispatcher2.setPathToDWRServlet("");
//		//dispatcher.setParameter("use DWRDispatcher");
//		dispatcher2.invokeDWRMethod(request, response);
		 
	}
	



}
