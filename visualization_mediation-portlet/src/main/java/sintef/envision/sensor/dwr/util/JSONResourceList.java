package sintef.envision.sensor.dwr.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sintef.envision.sensor.swe.bean.SensorResource;

import de.ifgi.envision.resources.model.Resource;
import de.ifgi.envision.resources.model.enums.States;

/**
 * Renders JSON representation of resource list
 * 
 * 
 * 
 * {"reshash": "WFS10_WorldAirport+Airport", "superhash": "WFS10_WorldAirport","name": "Airport (World Airport)", "state": "Link Only", "type": "OGC WFS FeatureType", "search": "Airport ogc:wfs:featuretype - WorldAirport ", "tags": "-"},
 * 
 * @author pajoma
 * 
 */
public class JSONResourceList implements Serializable {

	private static final long serialVersionUID = 8160614358445349543L;
	private final List<SensorResource> resources;
	private String generated = null; 

	public JSONResourceList(List<SensorResource> resources) {
		this.resources = resources;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (generated == null) {
			generated = this.generateJSON(); 
			
		}

		return generated;
	}

	private String generateJSON() {
		StringBuilder sb = new StringBuilder(); 
		sb.append("{\"list\": [").append('\n');
		
		for(int i=0;i<resources.size();i++)
		{
			SensorResource res=resources.get(i);
			this.addResource(sb, res);
			if(i+1<resources.size()) sb.append(",");
			sb.append('\n');
		}
		
//		Iterator<Resource> resItr = resources.iterator();   
//		while(resItr.hasNext()) {
//			Resource res = resItr.next(); 
//			
//			// add all sub resources (eg. feature types)
//			if(res.containsResources()) {
//				Iterator<Resource> subItr = res.getResources().get().iterator(); 
//				while(subItr.hasNext()) {
//					this.addSubResource(sb, subItr.next(), res); 
//					if(subItr.hasNext()) sb.append(",").append('\n');
//	
//				}			
//			} else {
//				// add the resource itself
//				this.addResource(sb, res); 
//			}			
//			if(resItr.hasNext()) sb.append(",");
//			sb.append('\n');
//		}
		sb.append("]}");
		return sb.toString(); 
	}

	
	private void addResource(StringBuilder sb, SensorResource res) {
		sb.append("  {");
		sb.append("\"procedure\": \"").append(res.getProcedure()).append("\", ");

		sb.append("\"offering\": \"").append(res.getOffering()).append("\", ");
		sb.append("\"observedProperty\": \"").append(res.getObservedProperty()).append("\", "); 
		
	
		sb.append("\"state\": \"").append(res.getState()).append("\", ");
		sb.append("\"search\": \"").append(constructSearchText(res)).append("\", ");
		
		sb.append("\"tags\": \"").append(s(res.getTags())).append("\", ");
		sb.append("\"location\": \"").append(res.getLocation()).append("\""); 
		 
		sb.append("}"); 
	}
	
//	private void addSubResource(StringBuilder sb,  Resource res, Resource father) {	
//		sb.append("  {");
//
//		
//		
//		sb.append("\"reshash\": \"").append(s(res.getHash())).append("\", ");
//		sb.append("\"superhash\": \"").append(s(father.getHash())).append("\",");
//		
//		// TODO: set title in subresources
//		sb.append("\"name\": \"").append(s(res.getIdentifier())).append(" (<small>").append(father.getTitle()).append("</small>)").append("\", ");
//		
//		// state is state of the father
//		sb.append("\"state\": \"").append(father.getState()).append("\", ");
//		
//		String type = res.getType(); 
//		if(type.equalsIgnoreCase("ogc:wfs:featuretype")) type = "OGC WFS FeatureType"; // small hack, TODO: change when resources have been updated
//		sb.append("\"type\": \"").append(type).append("\", "); 
//		
//		sb.append("\"search\": \"").append(constructSearchText(res)).append("\", ");
//		
//		sb.append("\"tags\": \"").append(s(res.getTags())).append("\"");
//		sb.append("}"); 
//	}
	
	
	private StringBuilder constructSearchText(SensorResource res) {
	
		StringBuilder sb = new StringBuilder(); 
		sb.append(s(res.getProcedure())).append(' ');
		sb.append(s(res.getOffering())).append(' ');
		sb.append(s(res.getObservedProperty())).append(' ');
	
		return sb; 
	}
	
	private String s(String object) {
		if ((object == null)  || (object.isEmpty())) return "-"; 
		else return object; 
	}
}
