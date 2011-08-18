package sintef.envision.sensor.dwr;


import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.ui.dwr.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ifgi.envision.resources.ResourceController;
import de.ifgi.envision.resources.helper.Construct;
import de.ifgi.envision.resources.jcrclient.JcrConfiguration;
import de.ifgi.envision.resources.model.Reference;
import de.ifgi.envision.resources.model.Resource;
import de.ifgi.envision.resources.model.enums.ReferenceType;

@Service
@RemoteProxy(name="resourceDisplay2")
public class DWRWebsiteUpdater {
	Logger log = Logger.getLogger(DWRWebsiteUpdater.class); 
	
	@Autowired ResourceController rc; 
	
	@RemoteMethod
	public void updateResourceDescription2(String resource_hash, String dwrSession) throws Exception {
		try {
//			JcrConfiguration config = new JcrConfiguration("http://giv-wfs.uni-muenster.de/jcr/", "envision", "envision", "test@liferay.com"); 
//			rc.setConfiguration(config);
//			rc.refresh(); 
//			
//			if(resource_hash.isEmpty()) throw new RuntimeException("No resource hash provided"); 
//			
//			String subResourceIdentifier = null; 
//			
//			// check whether is a subresource
//			if(resource_hash.contains(":")) {
//				// hash is then: WFS10_NorthernHemisphere:United_states_border
//				String[] split = resource_hash.split(":");
//				resource_hash = split[0]; 
//				subResourceIdentifier = split[1]; 
//			}
//			
//		
//			System.out.println("updateResourceDescription");
//			Resource res = rc.findResource(resource_hash); 
//			
//			if(res == null) throw new RuntimeException("Failed to find resource with given hash"); 
//			
//			// get base url
//			String base = Construct.constructBaseURL(rc, res);
			final StringBuilder sb = new StringBuilder(resource_hash); 
//			
//			// if the selected resource itself is a subresource
////			if(subResourceIdentifier != null) {
////				res = ResourceLookup.getIdentifiedResource(subResourceIdentifier, res); 
////			}
//			
//			sb.append("<h2>Resource Details</h2><br>"); 
//
//			this.generateDetailedDescription(res, sb, base); 
//			
//			sb.append("<br><h2>Included (sub-) resources</h2><br>"); 
//			for(Resource sres: res.listResources()) {
//				this.generateDetailedDescription(sres, sb, base);
//			}
			System.out.println("dwrSession-dwr-now:"+dwrSession);
//			Browser.withSession(dwrSession, new Runnable() {
//				
//				public void run() {
//					
//					
//				}}
//				);
			
//			Collection sessions =ServerContextFactory.get().getAllScriptSessions();
//			ScriptSession scriptSession=null;
//			for(Iterator i=sessions.iterator(); i.hasNext();)
//			{
//				scriptSession=(ScriptSession)i.next();
//			
//				System.out.println("sessionid:"+scriptSession.getId());
//				if(scriptSession.getId().equals(dwrSession))
//				{
//					ScriptBuffer scriptbuffer2 = new ScriptBuffer("win('2');");
//					scriptSession.addScript(scriptbuffer2);
//				}
//			}	
			
	    	Browser.withAllSessions(new Runnable() {

				public void run() {

					System.out.println("ScriptSessions-now");
					
					//ScriptSessions.addScript(new ScriptBuffer("win('1');"));
					//Util.setValue("resourceDetails", sb.toString()); 
					
				}
	    		
	    	}); 
			
		
			
		} catch (Exception e) {
			log.error("Failed to update website", e); 
			throw new RuntimeException(e);
		}
	
		
	}
	
	

	private void generateDetailedDescription(Resource res, StringBuilder sb, String base) throws IOException {
		sb.append("<p><b>Title:  </b>").append(s(res.getTitle())).append("</p>"); 
		sb.append("<p><b>Hash:  </b>").append(s(res.getHash())).append("</p>"); 
		sb.append("<p><b>Identifier:  </b>").append(s(res.getIdentifier())).append("</p>"); 
		sb.append("<p><b>Resource Type:  </b>").append(s(res.getType())).append("</p>"); 
		sb.append("<p><h2>References:  </h2>").append("</p>"); 
		
		for(Reference ref : res.listReferences()) {
			this.generateReference(ref,sb,base); 
		}

		

		
		
		
	}



	private void generateReference(Reference ref, StringBuilder sb, String base) throws IOException {
		sb.append("<p><table border=\"0\">"); 
		sb.append("<tr><td>Reference Type: </td><td>").append("<td>").append(ReferenceType.identify(ref.getType()).asString()).append("</td></tr>"); 
		sb.append("<tr><td>Reference Location: </td><td>").append("<td>").append(Construct.referenceURL(base, ref)).append("</td></tr>"); 
		
		sb.append("</table></p>"); 
		
	}

	
	private String s(String object) {
		if ((object == null)  || (object.isEmpty())) return "not set"; 
		else return object; 
	}
	
	
}
