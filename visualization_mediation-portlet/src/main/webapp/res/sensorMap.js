var map, layer;
var vectorLayer;
var singleLayer;
var selectedSensorInfo;
var selectedSensorName;
var sosurl, sosversion;

function initSOSInfo(url, version)
{
	sosurl=url;
	sosversion=version;
}

function init(){
            
			initCenterPanel();
			initStatusPanel();
	
            OpenLayers.ProxyHost="/proxy/?url=";
           
            map = new OpenLayers.Map('map'); 
      //      layer = new OpenLayers.Layer.WMS( "OpenLayers WMS",
       //         "http://vmap0.tiles.osgeo.org/wms/vmap0", {layers: 'basic'} );
            var layer = new OpenLayers.Layer.Google("Google Streets", {layers: 'basic'});

            map.addLayer(layer);
            map.setCenter(new OpenLayers.LonLat(0, 0), 2);
           // map.zoomToMaxExtent();
            
            singleLayer= new OpenLayers.Layer.Vector("selected sensor");
			vectorLayer = new OpenLayers.Layer.Vector("all sensors");
			
			
			//var point = new OpenLayers.Geometry.Point(115,32);
			//var fea = new OpenLayers.Feature.Vector(point);
			//fea.fid="point1";
			//var d1="data1";
			//fea.data=d1;
      		//vectorLayer.addFeatures(fea);
			
			map.addLayer(singleLayer);
			map.addLayer(vectorLayer);
            ctrl = new OpenLayers.Control.SelectFeature(vectorLayer,
                    {scope: this, onSelect: function(evt){
						//alert(evt.data); 
						//var xn=evt.geometry.x+10;
						//var yn=evt.geometry.y+20;
						//var p=new OpenLayers.Geometry.Point(xn,yn);
						//var f=new OpenLayers.Feature.Vector(p);
						//vectorLayer.addFeatures(f);
						
						selectedSensorInfo=evt.data;
						var properties=selectedSensorInfo.split('*');
						if(properties!=null)
						if(properties.length>=3)							
							selectedSensorName=properties[1];
						updateMapStatus(selectedSensorName);
						OpenLayers.Event.stop(evt); 
				}});      
            
            map.addControl(this.ctrl);
            ctrl.activate();  
            
            ctrl2 = new OpenLayers.Control.SelectFeature(singleLayer,
                    {scope: this, onSelect: function(evt){
						//alert(evt.data); 
						//var xn=evt.geometry.x+10;
						//var yn=evt.geometry.y+20;
						//var p=new OpenLayers.Geometry.Point(xn,yn);
						//var f=new OpenLayers.Feature.Vector(p);
						//vectorLayer.addFeatures(f);
						
						selectedSensorInfo=evt.data;
						var properties=selectedSensorInfo.split('*');
						if(properties!=null)
						if(properties.length>=3)							
							selectedSensorName=properties[1];
						updateMapStatus(selectedSensorName);
						OpenLayers.Event.stop(evt); 
				}});      
            
            map.addControl(this.ctrl2);
            ctrl2.activate();
    
            map.addControl(new OpenLayers.Control.LayerSwitcher());
            
           // map.setCenter(new OpenLayers.LonLat(0, 0), 1);
            
        };
 function initCenterPanel()
 {
	 var panel = Ext.getCmp('sensor.panel.center'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="map" style="height: 460px;"></div>'];
			 
			var panelCenter = new Ext.Panel({
				id: 'sensor.panel.center',
				frame : true,
				height: 460,  
				style : 'margin: 0 auto;',
				renderTo : 'center',
				html: html,
				bbar: [],
				preventBodyReset: true
			});
			panel=panelCenter;
		}
	 var tb = panel.getBottomToolbar(); 
		var sosMapAction = new Ext.Action({itemId: 'sosMapAction',text: 'SOS',iconCls: 'copy-icon',
			handler: function() {
				//alert('o');
				if(selectedSensorName==null)
					return;
				updateMapStatus('Show SOS Record for '+selectedSensorName+' .');
				dealWithSensor('SOS');
			}
		}); 
		var sesMapAction = new Ext.Action({itemId: 'sesMapAction',text: 'SES',iconCls: 'copy-icon', 
			handler: function() {
				//alert('e');
				if(selectedSensorName==null)
					return;
				updateMapStatus('Show SES Event '+selectedSensorName+' .');
				dealWithSensor('SES');
			}
		});
		tb.add('->', sesMapAction, '->', sosMapAction);
 }
 function initStatusPanel()
 {
	 var panel = Ext.getCmp('sensor.panel.status'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
		if(!panel) {
			var html = ['<div id="sensorStatus"></div>'];
			 
			var panelBottom = new Ext.Panel({
				id: 'sensor.panel.status',
				frame : true,
				height: 40,  
				style : 'margin: 0 auto;',
				renderTo : 'docs',
				html: html,
				
				preventBodyReset: true
			});
			
		}
		
 }

function dealWithSensor(act)
{
	mapSendingURL = mapSendingURL.replace(/&amp;/g, "&");
	//alert(mapSendingURL);
	Ext.Ajax.request({
		url : mapSendingURL,
		params : { sensorAction:act, url_sos:sosurl, version_sos:sosversion , sensorData:selectedSensorInfo}, 
		method : 'GET'
	});
}
 
function updateMapStatus(message)
{
	var div = Ext.get('sensorStatus');
	div.slideOut('r', {
	    easing: 'easeOut',
	    duration: .1
	});
	div.update(message);
	div.originalDisplay=message;
	div.slideIn('l', {
	    easing: 'easeOut',
	    duration: .2
	});
}
        
function initAllSensorMap(sensorList)
{
	var sensors=sensorList.split(';');
	vectorLayer.removeAllFeatures();
	if(sensors==null)
		return;
	
	for(var i=0; i< sensors.length; i++)
	{
		var sensor= sensors[i];
		var properties=sensor.split('*');
		if(properties.length>=5)
		{
			var sensorInfo=properties[0]+'*'+properties[1]+'*'+properties[2];
			var x=properties[3];
			var y=properties[4];
			
			var proj = new OpenLayers.Projection("EPSG:4326");
			var p = new OpenLayers.Geometry.Point(x,y);
			p.transform(proj, map.getProjectionObject());
			var f=new OpenLayers.Feature.Vector(p);
			f.data=sensorInfo;
			vectorLayer.addFeatures(f);			
		}
		
	}
}
function initSelectedSensorMap(sensor)
{
	singleLayer.removeAllFeatures();
	if(sensor==null)
		return;
	var properties=sensor.split('*');
	if(properties.length>=5)
	{
		var sensorInfo=properties[0]+'*'+properties[1]+'*'+properties[2];
		var x=properties[3];
		var y=properties[4];
		var proj = new OpenLayers.Projection("EPSG:4326");
		var p = new OpenLayers.Geometry.Point(x,y);
		p.transform(proj, map.getProjectionObject());
		var f=new OpenLayers.Feature.Vector(p);
		f.data=sensorInfo;
		singleLayer.addFeatures(f);	
		map.setCenter(p, 3);
	}
}