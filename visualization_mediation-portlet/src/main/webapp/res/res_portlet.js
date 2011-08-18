Ext.namespace('env.sensors');


var _resDataViewSensors; 
var _resHandlersSensors; 
var _resContextPathSensors; 
var sos_url;
var sos_version;
//Ext.onReady(function() {
//	env.sensors.init(); 
//});

env.sensors.init = function(context) {
	_resContextPathSensors = context; 
	
	require({context: _contextPathSensor,baseUrl: _contextPathSensor},["dwr/commonjs/amd/engine","dwr/commonjs/amd/interface/sensors"], 
			 function(dwr,sensors) {
		 dwr.engine.setActiveReverseAjax(true);
			dwr.engine.setNotifyServerOnPageUnload(true);
			sensors.initSession(portletsessionid);
   	});
	
	
	// if reverse ajax is required
//	if (!dwr.engine._isActiveReverseAjax) {
//		dwr.engine.setActiveReverseAjax(true);
//		sensors.initSession(dwr.engine._scriptSessionId);
//	}
	
	
	
	// mask the whole layer
	//Ext.get('rmpContent').mask("Loading sensors ..."); 
	
	
	// without this line we get always an annoying popup when navigating away from page
	//Ext.EventManager.on(window, 'beforeunload', function(){
	//	sensors.deleteSession();
	//	dwr.engine._unloader;
	//});
	window.onbeforeunload = function(e){
		require({context: _contextPathSensor,baseUrl: _contextPathSensor},["dwr/commonjs/amd/engine","dwr/commonjs/amd/interface/sensors"], 
				 function(dwr,sensors) {
			sensors.deleteSession(portletsessionid);
			dwr.engine._unloader;
	   	});
		
		};
	
	env.sensors.initFirstWin();
	
	// get resources encoded in json from DWR-exposed interface (we use a callback method for this)
	//resources.listResources(env.sensors.sequence);
	
	
	// if data view is not yet set, we do it from here 
	
	
	// remove loading screen after loading
	//Ext.get('rmpContent').unmask(); 
};

env.sensors.initFirstWin=function()
{
	var j="{\"list\": []}";
	env.sensors.sequence(j);
};

env.sensors.showWaitMessage=function()
{	
	Ext.get('rmpContentSensors').mask("please wait for the operation."); 
	alert("loading sensors!");
	};

env.sensors.initSensors=function(sosurl, sosversion){
	Ext.get('rmpContentSensors').mask("Loading sensors ..."); 
	
	require({context: _contextPathSensor,baseUrl: _contextPathSensor},["dwr/commonjs/amd/interface/sensors"], 
			 function(sensors) {
		sensors.listResources(sosurl, sosversion,env.sensors.sequence);
  	});
	sos_url=sosurl;
	sos_version=sosversion;
	
};

env.sensors.sequence = function(json) {

	var dataView = env.sensors.loadDataIntoView(json); 

	var resourcePanel = env.sensors.renderResourceList(dataView);
	
	env.sensors.updateStatus("Loaded "+dataView.getStore().getTotalCount()+" records"); 
	Ext.get('rmpContentSensors').unmask(); 	
};


env.sensors.registerHandler = function(handler) {
	if(!_resHandlersSensors) {
		_resHandlersSensors = new Array(); 
	}
	_resHandlersSensors.push(handler); 
};



env.sensors.loadDataIntoView = function(json){

	var data = Ext.util.JSON.decode(json);
	
	// create new memory proxy (the store needs it, this usually directly a HTTP
	// proxy)
	var memoryProxy = new Ext.data.MemoryProxy(data);	
 	var jsonReader = new Ext.data.JsonReader({
		idProperty: "procedure",
		root: "list", 
		fields: [
		   {name: 'procedure',type: 'string'},
		   {name: 'offering',type: 'string'},
		   {name: 'observedProperty',type: 'string'},
		   {name: 'state',type: 'string'},
		   {name: 'search', type: 'string'},
		   {name: 'tags',type: 'string'},
		   {name: 'location', type: 'string'}
	   ]
	}); 


	
	// the store basically links the proxy with the reader
	var store = new Ext.data.Store({
		proxy: memoryProxy
		,reader: jsonReader
		,baseParams: {limit: 100}
	});

	store.on("exception", function(proxy, response, action, options, notset, arg) {
		env.sensors.updateError(arg.name+": "+arg.message);  
	});

	// initiate loading from proxy to reader
	store.load();


	// the template for the individual items in the list
	var tpl = new Ext.XTemplate('<tpl for=".">',
			'<div class="res-wrap" id="{procedure}">',
			'<div class="res-row" ><span class="res-name">{procedure}</span><br/>',
			'<span class="res-type"> Property: {observedProperty}</span>',
			'',
			'</div>',
		    '</div> </tpl>');

	// the data view, see also
	// http://dev.sencha.com/deploy/dev/examples/view/data-view.html
	var dv = new Ext.DataView({
		id : "sensors.dataview", 
		autoScroll : true,
		singleSelect: true, 
		store : store,
		tpl : tpl,
		autoHeight : false,
		height : 265,
		multiSelect : false,
		itemSelector : 'div.res-wrap',
		overClass : 'res-over',
		selectedClass : 'res-selected',
		emptyText : ' No resources to display',
		style : 'border:1px solid #99BBE8;background:#fff;'
	});
	
	return dv; 
	
};




env.sensors.renderResourceList = function(dataView) {
	var panel = Ext.getCmp('resource.panel.ListSensors'); 
	
	if(typeof panel != "undefined") {
		panel.destroy(); 
	}
	

	
	if ((typeof panel == "undefined") || (panel.isDestroyed)) {
		panel = new Ext.Panel({
			id : 'resource.panel.ListSensors',
			frame: false,
			style: 'margin: 0 auto;',
			renderTo: 'rmpListSensors',
			items: [dataView]
			
			,bbar: []
			,tbar: ['Search: ', ' ', new env.sensors.ResSearchField({
				store: dataView.getStore()
			})]	
		});
	
	
		/* register listeners */
		dataView.on({
			"selectionchange" : function(vw, selected) {
				if(selected) {
					var records = vw.getSelectedRecords(); 
					if(records) {
						panel.getBottomToolbar().fireEvent("selected", records[0]);
						//alert(records[0].data.observedProperty);
					}
					
				} else {
					panel.getBottomToolbar().fireEvent("unselected", null);
				}
				
			}
		});
				// triggered by the search tool
		dataView.getStore().on("datachanged", function() {
			if(dataView) {
				dataView.refresh(); 
			}
			
		}); 
		
		
		env.sensors.renderActionsList(panel);	
		
		env.sensors.renderStatus(); 
	} 
	
	return panel; 
};


env.sensors.renderStatus = function() {
	var panel = Ext.getCmp('resource.panel.statusSensors'); 
	
	if(!panel) {
		var html = ['<div id="statusSensors"></div>'];
		 
		var panelBottom = new Ext.Panel({
			id: 'resource.panel.statusSensors',
			frame : true,
			height: 40,  
			style : 'margin: 0 auto;',
			renderTo : 'rmpInfoSensors',
			html: html,
			preventBodyReset: true
		});
	}


};

/* define actions */

	
env.sensors.renderActionsList = function(resPanel) {

	
	var tb = resPanel.getBottomToolbar(); 
	var record = null; 
	
	var refreshSensorsAction = new Ext.Action({itemId: 'refreshSensorsAction',text: 'Refresh',iconCls: 'refresh-icon', 
		handler: function() {
			require({context: _contextPathSensor,baseUrl: _contextPathSensor},["dwr/commonjs/amd/interface/sensors"], 
					 function(sensors) {
				sensors.refresh();
		  	});
			
			
		}
	}); 
	
	var showAllMapAction = new Ext.Action({itemId: 'showAllMapAction',text: 'All on the map',iconCls: 'import-icon', 
		handler: function() {
			//env.sensors.handleImportAction();  
			env.sensors.updateStatus("Show "+"all sensors"+" on the map."); 
			env.sensors.handleAllSensor('showAllMapAction');
		}
	}); 
	
	var showOneMapAction = new Ext.Action({itemId: 'showOneMapAction',text: 'One on the Map',iconCls: 'send-icon',
		handler: function() {
//			require({baseUrl: _contextPathSensor},["dwr/commonjs/amd/interface/sensors"], 
//					 function(sensors) {
//				sensors.showOneMap(dwr.engine._scriptSessionId ,record.data.procedure);
//		  	});
			env.sensors.updateStatus("Show "+record.data.procedure+" on the map."); 
			
			env.sensors.handleOneSensor(record.data,'showOneMapAction');
		}
	}); 

	
	var sosAction = new Ext.Action({itemId: 'sosAction',text: 'SOS Records',iconCls: 'copy-icon',
		handler: function() {
			//sensors.sosAction(dwr.engine._scriptSessionId ,record.data.procedure);
			env.sensors.updateStatus("Show "+record.data.procedure+" records."); 
			env.sensors.handleOneSensor(record.data,'sosAction');
		}
	}); 

	var sesAction = new Ext.Action({itemId: 'sesAction',text: 'See SES Event',iconCls: 'copy-icon', 
		handler: function() {
			//sensors.sesAction(dwr.engine._scriptSessionId ,record.data.procedure);
			env.sensors.updateStatus("Show "+record.data.procedure+" events."); 
			env.sensors.handleOneSensor(record.data,'sesAction');
		}
	});

	
	tb.add(refreshSensorsAction);
	tb.add('->', sesAction, '->', sosAction, '->', showOneMapAction, '->', showAllMapAction );
	
	sesAction.disable(); 
	sosAction.disable(); 
	showOneMapAction.disable(); 
    tb.doLayout();
	
	
	/* enable flash element for copy action */
//	ZeroClipboard.setMoviePath(_resContextPathSensors+'/static/res/ZeroClipboard.swf'); 
//	var clip = new ZeroClipboard.Client();
//	var copyElement = tb.getComponent('copyAction').btnEl; 
//	clip.glue(copyElement.id);


	
	tb.on("selected", function(selectedRecord) {
		record = selectedRecord; 
		if(!record) {
			sesAction.disable(); 
			sosAction.disable(); 
			showOneMapAction.disable(); 
			return; 
		} 
		//alert('select:'+record.data.state);
		// set copy text
//		clip.setText(record.data.reshash);
		var state = record.data.state;
		
		// always on if something is selected
		sesAction.enable(); 
		sosAction.enable(); 
		showOneMapAction.enable(); 
				
		if(state == 0) {
			return; // do nothing
		}
		
		// just imported
		//if(state < 20) {
		//	translateAction.setHidden(false);  	
		//	return; 
		//} 
	
		env.sensors.updateStatus("Selected sensors: "+record.data.procedure+" from offering:"+record.data.offering); 
	}) ;
	
	
};




env.sensors.updateStatus = function(message) {
	var div = Ext.get('statusSensors');
	div.slideOut('r', {
	    easing: 'easeOut',
	    duration: .1
	});
	
	if(message) {
		var d = new Date(); 
		var h = d.getHours();
		var m = d.getMinutes();
		var s = d.getSeconds(); 
		
		div.update(h+":"+m+":"+s+" - "+message);
	}
	div.slideIn('l', {
	    easing: 'easeOut',
	    duration: .2
	});
};

env.sensors.updateError = function(message) { 
	env.sensors.updateStatus("<font color=\"red\">"+message+"</font>"); }
	

// called by the DWR methods if anything changes on the server
env.sensors.handleRefresh = function(){

//	if(selectedRecord != null) {
//		dataView.deselect(selectedRecord); 
//		selectedRecord = null; 
//	} 
	require({context: _contextPathSensor,baseUrl: _contextPathSensor},["dwr/commonjs/amd/interface/sensors"], 
			 function(sensors) {
		sensors.listResources(sos_url,sos_version,env.sensors.sequence);
 	});
		//reloading the json from the server
};

env.sensors.handleAllSensor=function(act){
	sensorsSendingURL = sensorsSendingURL.replace(/&amp;/g, "&");
	//alert(act);
	Ext.Ajax.request({
		url : sensorsSendingURL,
		params : { sensorAction:act, url_sos:sos_url, version_sos:sos_version }, 
		method : 'GET'
	});
	
	// also notify handlers
	for(key in _resHandlersSensors) {
		_resHandlersSensors[key](record); 
	}
};

env.sensors.handleOneSensor = function(record, act) {
	// we assume that the var resourceSendingURL and callbacksURL has been set
	// in the JSP file including this
	// we have to send it to the portlet action, not through DWR (we wouldn't
	// know which portlet in which session
	// is firing this action

	sensorsSendingURL = sensorsSendingURL.replace(/&amp;/g, "&");

	Ext.Ajax.request({
		url : sensorsSendingURL,
		params : { sensorAction:act,url_sos:sos_url, version_sos:sos_version, location: record.location ,procedure : record.procedure, observedProperty:record.observedProperty ,offering:record.offering }, 
		method : 'GET'
	});
	
	// also notify handlers
	for(key in _resHandlersSensors) {
		_resHandlersSensors[key](record); 
	}
};
	


env.sensors.ResSearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
    	env.sensors.ResSearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },
 
    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
    width: 220, 
    hasSearch : false,

    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            var o = {start: 0};
            this.store.reload({params:o});
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        this.store.filter('search', v, true, false); 
        this.hasSearch = true;
        this.triggers[0].show();
    }
});



