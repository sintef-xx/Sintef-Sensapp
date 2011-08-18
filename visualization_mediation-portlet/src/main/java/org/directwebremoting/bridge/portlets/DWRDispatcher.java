package org.directwebremoting.bridge.portlets;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;





/**
 * Use this if you want to call any DWR methods from within a PortletContext (or
 * actually any other context not bound to DWR). Don't use it if you want to
 * call DWR to retrieve any data (responses will be ignored). This is only
 * useful if you want to dynamically update the content via DWR (i.e. using
 * reverse ajax).
 * 
 * Note that you have to activate the following option in your web.xml to make
 * this work:
 * <pre>
 * {@code 
 *  <init-param> 
 *   <param-name>allowGetForSafariButMakeForgeryEasier</param-name> 
 *   <param-value>true</param-value> 
 *  </init-param>	
 * }
 * </pre>
 * 
 * @author pajoma
 * 
 */
public class DWRDispatcher {

	private Map<String, Serializable> parameters = null;
	private final String scriptName; 
	private final String methodName;
	private String[] parameter;
	private  String sessionID, dwrServletPath; 

	/**
	 * @param scriptName
	 *            The DWR-exposed script the request should be forwarded to. Has
	 *            to match of the name-attribute of the @RemoteProxy annotation.
	 * @param methodName
	 *            The DWR-exposed method the request should be forwarded to. Has
	 *            to match the name of the method annotated with @RemoteMethod.
	 * @param parameter
	 *            
	 *          
	 */
	public DWRDispatcher(String scriptName, String methodName) {
		this.scriptName = scriptName;
		this.methodName = methodName;
	}
	
	public void setScriptSessionID(String scriptsession) {
		this.sessionID = scriptsession;
	}
	
	
	/**
	 * Setting the path to the DWR servlet. Note: this is only required if you expect to have multiple portlets whose messages should be aggregated into one channel
	 * @param dwrServletPath
	 */
	public void setPathToDWRServlet(String dwrServletPath) {
		this.dwrServletPath = dwrServletPath;
	}
	
	/**
	 * Setting some parameters which should be forwarded 
	 * @param parameter
	 * 				An open list of parameters in a string array [value1, value2]. 
	 *            	The order has to match the parameters expected by the DWR exposed method. 
	 */
	public void setParameter(String ... parameter) {
		this.parameter = parameter; 
	}
	
	/**
	 * Invokes the DWR method set in the constructor. Note that any errors in
	 * the execution are not sent back (you have to catch and log them directly
	 * in the DWR servlet)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws PortletException
	 */
	public void invokeDWRMethod(PortletRequest request, PortletResponse response) throws IOException, PortletException {
		DWRRequestWrapper dpr = new DWRRequestWrapper(request, this.scriptName, this.methodName);
		dpr.setDWRServletPath(this.dwrServletPath); 
		dpr.addMethodParameters(this.sessionID, parameter);

		PortletContext portletContext = request.getPortletSession().getPortletContext();	

		
		// TODO: if the dispatch should forward to another context, we have call another URL (we can't dispatch)
		
		String portalInfo = request.getPortalContext().getPortalInfo(); 
		System.out.println("portalInfo:"+portalInfo);
		if(portalInfo.contains("Liferay")) {
			// Liferay has a broken implementation of the Wrapper (it basically ignores it). But 
			// adding the parameters to the string for requesting the dispatcher still does the trick
			System.out.println("dwrServletPath:"+dwrServletPath);
			
			StringBuilder sb = new StringBuilder(dwrServletPath); 
			System.out.println("getRequestPath:"+dpr.getRequestPath());
			sb=sb.append(dpr.getRequestPath()).append("?");
		
			Enumeration<String> parameterNames = dpr.getParameterNames();
			while(parameterNames.hasMoreElements()) {
				String next = parameterNames.nextElement(); 
				sb.append(next); 
				sb.append("=").append(dpr.getParameter(next)); 
				if(parameterNames.hasMoreElements()) sb.append("&"); 
			}
			
			
			PortletRequestDispatcher prd = portletContext.getRequestDispatcher(sb.toString()); 
			System.out.println("prdPath:"+sb.toString());
			prd.forward(dpr, response); 
			
		} else {
			System.out.println("other");
			PortletRequestDispatcher prd = portletContext.getRequestDispatcher(dpr.getRequestPath());
			prd.include(dpr, response);
		}
		
		
	}

	// not used anymore, but I leave it as referenc
//	@SuppressWarnings("unused")
//	private String callExternal(LiferayDWRRequestWrapper dpr) {
//		if(portletpath == null) {
//			throw new RuntimeException("You have to define the portlet path if you want to dispatch requests in Liferay"); 
//		}
//		
//		try {
//			
//		 
//		DefaultHttpClient httpclient = new DefaultHttpClient();
//		HttpPost post = new HttpPost(); 
//		/* The URL we have to construct: 
//		 * http://localhost:8080/DwrEvents-0.0.2-SNAPSHOT/dwr/call/plaincall/message.updateMessage.dwr
//		 * 
//		 */
//		
//		// construct uri
//		String host = dpr.getRequest().getServerName();
//		int port = dpr.getRequest().getServerPort(); 
//		String scheme = dpr.getRequest().getScheme(); 
//		
//		
//		String path = this.portletpath+dpr.getRequestPath(); 
//		
//
//		URI uri = new URI(scheme, null, host, port, path, null, null );
//		post.setURI(uri); 
//		
//		StringBuilder sb = new StringBuilder(); 
//		for(Entry<String, String[]> e : dpr.getParameterMap().entrySet()) {
//			sb.append(e.getKey()).append("=").append(e.getValue()[0]).append('\n'); 
//		}
//		post.setEntity(new StringEntity(sb.toString()));
//		
//		HttpResponse execute = httpclient.execute(post); 
//		
//		StringWriter sw = new StringWriter(); 
//		IOUtils.copy(execute.getEntity().getContent(), sw); 
//		return sw.toString(); 
//
//		
//
//		} catch (URISyntaxException e) {
//			throw new RuntimeException(e); 
//		} catch (ClientProtocolException e) {
//			throw new RuntimeException(e); 
//		} catch (IOException e) {
//			throw new RuntimeException(e); 
//		}
//		
//	}

}
