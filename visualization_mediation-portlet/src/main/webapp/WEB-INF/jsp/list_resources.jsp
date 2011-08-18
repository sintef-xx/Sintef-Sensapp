<%@ page session="true" contentType="text/html;charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix='c'%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<!--  adds portlet information to request object, see http://blogs.sun.com/deepakg/entry/jsr286_defineobjects_tag -->
<portlet:defineObjects/> 

<portlet:actionURL var="sensorsSendingURL" >
	<portlet:param name="action" value="sensors-sending" />
</portlet:actionURL>
<portlet:resourceURL var="callbackURL" id="callbacks"/>



<link href="<%=renderRequest.getContextPath()%>/res/res_portlet.css"  rel="stylesheet" type="text/css" />
<!--  
<link rel="stylesheet" type="text/css" href="http://extjs.cachefly.net/ext-3.3.1/resources/css/ext-all.css" />
<script type="text/javascript" src="http://extjs.cachefly.net/ext-3.3.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="http://extjs.cachefly.net/ext-3.3.1/ext-all.js"></script>
-->
<%-- <script type='text/javascript' src="<%=renderRequest.getContextPath()%>/dwr/engine.js"/></script> --%>
<%-- <script type='text/javascript' src="<%=renderRequest.getContextPath()%>/dwr/util.js"/></script> --%>
<%-- <script type='text/javascript' src="<%=renderRequest.getContextPath()%>/dwr/interface/resources.js"></script> --%>
<%-- <script type="text/javascript" src="<%=renderRequest.getContextPath()%>/static/res/ZeroClipboard.js" ></script> --%>
<%-- <script type="text/javascript" src="<%=renderRequest.getContextPath()%>/static/res/res_portlet.js" ></script> --%>



<script type="text/javascript" src="<%=renderRequest.getContextPath()%>/res/LAB.min.js"></script>
<script type="text/javascript"> 
	
	
	var _contextPathSensor = "<%=renderRequest.getContextPath()%>"; 
	var portletsessionid="<%=portletSession.getId()%>";
	
	$LAB
	//.script("http://extjs.cachefly.net/ext-3.3.1/adapter/ext/ext-base.js").wait()
	//	.script("http://extjs.cachefly.net/ext-3.3.1/ext-all.js").wait() 
		.script(_contextPathSensor+"/res/require.js").wait() 
		//.script(_contextPath+"/dwr/interface/sensors.js").wait() 
 		.script(_contextPathSensor+"/res/res_portlet.js").wait() 	
		.script(_contextPathSensor+"/dwr/util.js").wait()
 		.script(_contextPathSensor+"/res/ZeroClipboard.js").wait(function() {
 			env.sensors.init(_contextPathSensor); 
 		})
 		;  
	
	$LAB.setGlobalDefaults({AllowDuplicates:false, UseLocalXHR:true, UseCachePreload: true});
	
	var sensorsSendingURL = "${sensorsSendingURL}";
	

	function addToWindowOnload(oFunction) 
	{ 
	  
	var existingOnload = window.onload;     
	if (existingOnload)     
	{       
	window.onload = function(){ oFunction(); existingOnload(); };     
	}    
	else   
	{        
	window.onload = oFunction;    
	oFunction();
	}} 
	addToWindowOnload(function()
			{ 
	//	dwr.engine.setActiveReverseAjax(true);
	//	dwr.engine.setNotifyServerOnPageUnload(true);
		//alert(dwr.engine._scriptSessionId);
	//	resources.initSession(dwr.engine._scriptSessionId);
			}); 
	
</script>


	
    <div id="rmpContentSensors" >
    
        <div id="rmpListSensors" style="height:325px;">

        </div>
        <div id="rmpSecondarySensors">
			<div id="rmpInfoSensors">
            	
			</div>
        </div>
    </div>

<div id="import-dialog-Sensors" class="x-hidden">

    <div class="x-window-header">Import Sensors</div>

</div>
