package sintef.envision.sensor.dwr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.portlet.PortletContext;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.portlet.context.PortletContextAware;

import sintef.envision.sensor.dwr.util.JSONResourceList;
import sintef.envision.sensor.swe.Mysession;
import sintef.envision.sensor.swe.SESTool;
import sintef.envision.sensor.swe.SOSTool;
import sintef.envision.sensor.swe.bean.SensorResource;

import de.ifgi.envision.resources.ResourceController;
import de.ifgi.envision.resources.actions.CrossLinkResource;
import de.ifgi.envision.resources.actions.ImportResource;
import de.ifgi.envision.resources.actions.ProxyCapabilities;
import de.ifgi.envision.resources.actions.TranslateResource;
import de.ifgi.envision.resources.jcrclient.JcrConfiguration;
import de.ifgi.envision.resources.model.Resource;
import de.ifgi.envision.resources.model.enums.Actions;
import de.ifgi.envision.resources.model.enums.States;
import de.ifgi.envision.resources.model.enums.Types;

@Service
@RemoteProxy(name="sensors")
public class DWRResourceCollectionBean  {

	public static String PROXY_LINK = "http://semantic-proxy.appspot.com";
	
	private @Autowired ResourceController rc; 

	Logger log = Logger.getLogger(DWRResourceCollectionBean.class);

	@RemoteMethod
	public void test(String sid)
	{
		System.out.println("remote message:");
		ScriptSessions.addScript("env.sensors.showWaitMessage()");
	//	String sid=(String)request.getPortletSession().getId();
		Mysession.getSessionById(sid).addScript(new ScriptBuffer("env.sensors.showWaitMessage();"));	
	}
	

	/**
	 * Returns a collection of all resources as JSON. This is a remote method, try
	 * http://localhost:8080/ResourceManager-0.0.1-SNAPSHOT/dwr/index.html to call this 
	 * method via DWR. 
	 * 
	 * @return 
	 * @throws Exception 
	 */
	@RemoteMethod
	public String listResources(String url, String version) throws Exception {
		try {
			String s=new SOSTool().sendPostToSWEAdapter(url,version);
			List<SensorResource> l=new SOSTool().parseSensors(s);
						
			JSONResourceList jrl = new JSONResourceList(l); 
			System.out.println("jrl:"+jrl.toString());
			return jrl.toString(); 	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); 
		}
	
	}
	
	
	@RemoteMethod
	public void refresh() throws Exception {
		String sessionId=WebContextFactory.get().getScriptSession().getId();
		Browser.withSession(sessionId, new Runnable() {
			
			public void run() {
				ScriptSessions.addScript("env.sensors.updateStatus('Refreshing sensors')");
				
				log.info("Refreshing sensors"); 

				ScriptSessions.addScript("env.sensors.handleRefresh()");
				
				ScriptSessions.addScript("env.sensors.updateStatus('Sensors refreshed')"); 
				
			}
    		
    	}); 
		
	}
	@RemoteMethod
	public void showAllMap() throws Exception{
		String sessionId=WebContextFactory.get().getScriptSession().getId();
		Browser.withSession(sessionId, new Runnable() {
			
			public void run() {
				ScriptSessions.addScript("env.sensors.updateStatus('Show all sensors on the map')");
				
				log.info("Show all sensors on the map"); 
				// the actual work will be done on the client part by using portlet action
				
			}
    		
    	}); 
	}
	@RemoteMethod
	public void showOneMap(String sessionId, String sensor) throws Exception{
		final String s=sensor;
		Browser.withSession(sessionId, new Runnable() {
			
			public void run() {
				ScriptSessions.addScript("env.sensors.updateStatus('Show "+s+" on the map')");
				
				log.info("Show "+s+" on the map"); 
				// the actual work will be done on the client part by using portlet action		
			}
    		
    	}); 
	}
	@RemoteMethod
	public void sesAction(String sessionId, String sensor) throws Exception{
		final String s=sensor;
		Browser.withSession(sessionId, new Runnable() {
			
			public void run() {
				ScriptSessions.addScript("env.sensors.updateStatus('Show "+s+" event')");
				
				log.info("Show "+s+" event"); 
				// the actual work will be done on the client part by using portlet action		
			}
    		
    	}); 
	}
	@RemoteMethod
	public void sosAction(String sessionId, String sensor) throws Exception{
		final String s=sensor;
		Browser.withSession(sessionId, new Runnable() {
			
			public void run() {
				ScriptSessions.addScript("env.sensors.updateStatus('Show "+s+" records')");
				
				log.info("Show "+s+" records"); 
				// the actual work will be done on the client part by using portlet action		
			}
    		
    	}); 
	}
	
	@RemoteMethod
	public void initSession(String sessionId)
	{
		WebContextFactory.get().getSession().setAttribute("ScripSession", 
				WebContextFactory.get().getScriptSession().getId());
		
		Mysession.addMap(sessionId, WebContextFactory.get().getScriptSession());
//		ScriptBuffer scriptbuffer = new ScriptBuffer("env.sensors.showWaitMessage('"+""+"');");
//		WebContextFactory.get().getScriptSession().addScript(scriptbuffer);
		
		System.out.println("saved scripsession in session:"+WebContextFactory.get().getSession().getId());
		System.out.println("saved scripsession :"+WebContextFactory.get().getScriptSession().getId());
	}
	@RemoteMethod
	public void deleteSession(String sessionId)
	{
		Mysession.deleteSessionById(sessionId);
		String resourceId=Mysession.deleteSESSubscribeBySID(sessionId);
		if(resourceId!=null)
		{
			System.out.println("doing ubsubscribe for "+resourceId);
			if(resourceId!=null&&!(resourceId.equals("")))
			{
				SESTool ses=new SESTool();
				ses.doUnsubscribe(resourceId);
			}
		}
	}
	
	@RemoteMethod
	public void initSensors(String url, String version)
	{
		final String sosUrl=url;
		final String sosVersion=version;
		String sessionId=(String)WebContextFactory.get().getSession().getAttribute("ScripSession");
		System.out.println("get scripsession from session:"+sessionId);
		if(sessionId!=null)
		Browser.withSession(sessionId, new Runnable() {
			
			public void run() {
				ScriptSessions.addScript("env.sensors.updateStatus('init sensor list from "+sosUrl+"')");
				
				log.info("init sensor list from "+sosUrl); 
				ScriptSessions.addScript("env.sensors.initSensors('"+sosUrl+"','"+sosVersion+"')");
			}
    		
    	}); 
		else
		{
			ScriptSessions.addScript("env.sensors.updateStatus('init sensor list from "+sosUrl+"')");
			
			log.info("init sensor list from "+sosUrl); 
			ScriptSessions.addScript("env.sensors.initSensors('"+sosUrl+"','"+sosVersion+"')");
		}
	}

}
