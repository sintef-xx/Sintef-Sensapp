package org.directwebremoting.bridge.portlets;

import java.io.Serializable;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.portlet.EventRequest;
import javax.portlet.PortalContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.portlet.filter.PortletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.extend.ProtocolConstants;

/**
 * Wraps the Portlet Request to make all necessary DWR parameters available to
 * the DWR servlet. The only parameters which have to be defined are the name of
 * the method and the script. Any parameters expected by the method (e.g. the
 * DWR session ID) are also included.
 * 
 * @author pajoma
 * 
 */
public class DWRRequestWrapper extends PortletRequestWrapper {

	private Map<String, String[]> dwrParameters;
	private String prefix = "c0-";
	private final String scriptname;
	private final String methodname;
	private String dwrServletPath;
	
	@Override
	public PortletRequest getRequest() {
		if(super.getRequest() instanceof EventRequest) {
			return super.getRequest(); 
		} else {
			return super.getRequest(); 
		}
		
	}

	public DWRRequestWrapper(PortletRequest request, String scriptname, String methodname) throws PortletException {
		super(request);
		this.scriptname = scriptname;
		this.methodname = methodname;

		this.prepareParameters();
	}

	private void prepareParameters() {
		getParameterMap().put(prefix + ProtocolConstants.INBOUND_KEY_SCRIPTNAME, new String[] { scriptname });
		getParameterMap().put(prefix + ProtocolConstants.INBOUND_KEY_METHODNAME, new String[] { methodname });

		String sessionId = getRequest().getPortletSession().getId();
		getParameterMap().put(ProtocolConstants.INBOUND_KEY_SCRIPT_SESSIONID, new String[] { sessionId });

		String page = getRequest().getContextPath();
		getParameterMap().put(ProtocolConstants.INBOUND_KEY_PAGE, new String[] { page });

		String windowName = getRequest().getWindowID();
		getParameterMap().put(ProtocolConstants.INBOUND_KEY_WINDOWNAME, new String[] { windowName });

		getParameterMap().put(ProtocolConstants.INBOUND_CALL_COUNT, new String[] { "1" });
		getParameterMap().put(ProtocolConstants.INBOUND_KEY_BATCHID, new String[] { "1" });
		getParameterMap().put(prefix + ProtocolConstants.INBOUND_KEY_ID, new String[] { "1" });
		System.out.println("sessionId:"+sessionId);
		System.out.println("page:"+page);
		System.out.println("windowName:"+windowName);
		System.out.println("scriptname:"+scriptname);
		System.out.println("methodname:"+methodname);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.portlet.filter.PortletRequestWrapper#getParameterMap()
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		if (dwrParameters == null) {
			dwrParameters = new HashMap<String, String[]>();

		}
		return dwrParameters;
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.portlet.filter.PortletRequestWrapper#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		String[] v = getParameterMap().get(name);
		if (v == null)
			return null;
		else
			return v[0];
	};

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.portlet.filter.PortletRequestWrapper#getParameterValues(java.lang
	 * .String)
	 */
	@Override
	public String[] getParameterValues(String name) {
		return getParameterMap().get(name);
	};

	public String getRequestPath() {
		StringBuilder sb = new StringBuilder();
		sb.append("/dwr/call/plaincall/");
		sb.append(scriptname).append(".");
		sb.append(methodname).append(".dwr");
		return sb.toString();
	}
	
	public String getPortletPath() {
		if(this.dwrServletPath != null) return dwrServletPath; 
		else 
		return getRequest().getContextPath();
	}

	public void addMethodParameters(String sessionID, String[] parameter) {
		if(sessionID != null) {
			getParameterMap().put(ProtocolConstants.INBOUND_KEY_DWR_SESSIONID, new String[] {sessionID}); 
		}
		
		if(parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				getParameterMap().put(prefix + "param" + i, new String[] { "string:" + parameter[i] });
			}
		}
	

	}

	public void setDWRServletPath(String dwrServletPath) {
		this.dwrServletPath = dwrServletPath;
	}

}
