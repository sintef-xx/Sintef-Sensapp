<%@ page session="true" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>



 <%-- DWR AJAX setup --%>
<portlet:defineObjects/> 

<link rel="stylesheet" href="<%=renderRequest.getContextPath()%>/theme/default/style.css" type="text/css" />


<script src="<%=renderRequest.getContextPath()%>/OpenLayers.js"></script>

<script type='text/javascript' src="dwrServlet/dwr/engine.js"/></script>
<script type='text/javascript' src="<%=renderRequest.getContextPath()%>/dwr/engine.js"/></script>
<script type='text/javascript' src="<%=renderRequest.getContextPath()%>/dwr/util.js"/></script>
<script type='text/javascript' src="<%=renderRequest.getContextPath()%>/dwr/interface/resourceDisplay.js"></script>
<script type="text/javascript">
Ext.onReady(function() {
	
	if (!dwr.engine._isActiveReverseAjax) {
		dwr.engine.setActiveReverseAjax(true);
		dwr.engine.setNotifyServerOnPageUnload(true);
	};
	

	Ext.EventManager.on(window, 'beforeunload', dwr.engine._unloader);
	alert(dwr.engine._scriptSessionId);
	init();
}); 
	function win(s)
	{
		alert('win'+s);
		};
    var map, layer;
    alert('?');
            function init(){
                alert('begin');
                OpenLayers.ProxyHost="/proxy/?url=";
                map = new OpenLayers.Map('map'); 
                layer = new OpenLayers.Layer.WMS( "OpenLayers WMS",
                    "http://vmap0.tiles.osgeo.org/wms/vmap0", {layers: 'basic'} );
                   
                map.addLayer(layer);
                map.setCenter(new OpenLayers.LonLat(0, 0), 0);



    			var vectorLayer = new OpenLayers.Layer.Vector("vector");
    			var point = new OpenLayers.Geometry.Point(115,32);
    			var fea = new OpenLayers.Feature.Vector(point);
    			fea.fid="point1";
    			var d1="data1";
    			fea.data=d1;
          		vectorLayer.addFeatures(fea);
    			map.addLayer(vectorLayer);
                ctrl = new OpenLayers.Control.SelectFeature(vectorLayer,
                        {scope: this, onSelect: function(evt){
    						//alert(evt.fid); 
    						var xn=evt.geometry.x+10;
    						var yn=evt.geometry.y+20;
    						var p=new OpenLayers.Geometry.Point(xn,yn);
    						var f=new OpenLayers.Feature.Vector(p);
    						vectorLayer.addFeatures(f);
    						OpenLayers.Event.stop(evt); 
    				}});
                map.addControl(this.ctrl);
                ctrl.activate();          
                map.addControl(new OpenLayers.Control.LayerSwitcher());
                map.zoomToMaxExtent();
            };
</script>

<p>By selecting a resource in your user collection and pressing the send button, you can display more specific details about it here:  </p>

<p>

<div id="resourceDetails" style="min-height: 200px;">  

</div>


</p>


<div style="margin-top: 50px">
<p>
	<b>What is a resource?</b> <br>
	A resource can by either a Web service, a composition draft, or an ontology. Some resources, such as a Web service, have to be prepared
	before being useful for the ENVISION portal. 
</p>
<br>
<p>	 
	<b>How do I prepare a resource?</b> <br>
	After importing a Web service (or having it discovered using the catalogue interface) you need to translate it into WSDL files and service models. 
	The latter is required for the semantic annotation, the former is a prerequisite for the composition. You can trigger the translation (and all subsequent actions)
	by clicking the action button in the tool bar.  
</p>
</div>
    <div id="tags"></div>
    <div id="shortdesc"></div>
    <div id="map" class="smallmap"></div>
    <div id="docs">
       abc
    </div>