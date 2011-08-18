



	Ext.data.DataProxy.addListener('exception', function(proxy, type, action, options, res) {
	    if (type === 'remote') {
	        Ext.Msg.show({
	            title: 'REMOTE EXCEPTION',
	            msg: res.message,
	            icon: Ext.MessageBox.ERROR,
	            buttons: Ext.Msg.OK
	        });
	    }
	});	


function renderResourcesList() {
	var jsonText = '{	    "list": [	             {	                 "res": "cdc_112",	                 "type": "OGC Feature Collection",	                 "state": "Not yet annotated",	                 "tags": "corine, landcover, urban areas, continuoues"	             },	             {	                 "res": "cdc_111",	                 "type": "OGC Feature Collection",	                 "state": "Not yet annotated",	                 "tags": "corine, landcover, urban areas"	             },	             {	                 "res": "sos",	                 "type": "OGC Observation Collection",	                 "state": "Not yet annotated",	                 "tags": "sensor, ground water, depth"	             }	         ]	     }';
	var data = Ext.util.JSON.decode(jsonText);
	var memoryProxy = new Ext.data.MemoryProxy(data);
	
	var jsonReader = new Ext.data.JsonReader({
		id: "res",
		root: "list"}, [
	    {name: 'res', type: 'string'},
	    {name: 'type', type: 'string'},
	    {name: 'state',	type: 'string'},
	    {name: 'tags',	type: 'string'}
	    ]);
	
	var store = new Ext.data.Store({
		proxy: memoryProxy,
		reader: jsonReader
	});
	
	store.load();
}
	
Ext.onReady(function(){

//	var jsonText = '{"list": ['+
//		'{"res": "cdc_112", "type": "OGC Feature Collection", "state": "not yet annotated", "tags": "envision, simple-use-case"},'+    
//		'{"res": "cdc_111", "type": "OGC Feature Collection", "state": "published", "tags": "envision, simple-use-case"},'+    
//		'{"res": "sos", "type": "OGC Observation Collection", "state": "not yet translated", "tags": "envision, simple-use-case"}'+   
//		']}';
	
	var jsonText = '{	    "list": [	             {	                 "res": "cdc_112",	                 "type": "OGC Feature Collection",	                 "state": "Not yet annotated",	                 "tags": "corine, landcover, urban areas, continuoues"	             },	             {	                 "res": "cdc_111",	                 "type": "OGC Feature Collection",	                 "state": "Not yet annotated",	                 "tags": "corine, landcover, urban areas"	             },	             {	                 "res": "sos",	                 "type": "OGC Observation Collection",	                 "state": "Not yet annotated",	                 "tags": "sensor, ground water, depth"	             }	         ]	     }';
	
	var emptyText = '{"list": []}';
	var data = Ext.util.JSON.decode(jsonText);
	var memoryProxy = new Ext.data.MemoryProxy(data);
	
	var jsonReader = new Ext.data.JsonReader({
		id: "res",
		root: "list"}, [
	    {name: 'res', type: 'string'},
	    {name: 'type', type: 'string'},
	    {name: 'state',	type: 'string'},
	    {name: 'tags',	type: 'string'}
	    ]);

	var store = new Ext.data.Store({
		proxy: memoryProxy,
		reader: jsonReader
	});
	store.load(); 

	
	var tpl = new Ext.XTemplate(
		'<tpl for=".">',
			'<div class="res-wrap" id="{res}">',
			'<div class="res-row" > <span class="res-name">{res}</span>',
			'<span class="res-state">{state}</span></div>',
			'Type: <span class="res-type">{type}</span><br />',
			'Tags: <span class="x-editable">{tags}</span> ',
			'<div id="res-send-button"> send</div>',
			'</div></tpl>'
	);
	
/*
 <div id="res-wrap">

	 	<div id="res-row"> 
	 		<span class="res-name">cdc_112</span>
	 		<span class="res-state">not yet annotated</span>
	 	</div>
	 	Type: <span class="res-type">OGC Feature Collection</span><br>
	 	Tags: <span class="x-editable">envision, simple-use-case</span>
 </div>	 
 
 <div class="thumb-wrap" id="zack_sink.jpg">
 	<div class="thumb">
 		<img src="images/thumbs/zack_sink.jpg" title="zack_sink.jpg">
 	</div>
 	<span class="x-editable">zack_sink.jpg</span>
 </div>
 
 */
	var dataView = new Ext.DataView({
		autoScroll: true, 
		store: store, 
		tpl: tpl,
		autoHeight: false, 
		height: 265,
		multiSelect: false,
		singleSelect: true,
		itemSelector: 'div.res-wrap',
		overClass:'x-view-over',
		emptyText: 'No resources to display',
		loadingText: 'Please Wait...',
		style: 'border:1px solid #99BBE8;background:#fff;',
        plugins: [
                //  new Ext.DataView.DragSelector(),
                  new Ext.DataView.LabelEditor({dataIndex: 'tags'})
              ],

//  	        listeners: {
//              	selectionchange: {
//              		fn: function(dv,nodes){
//              			Ext.MessageBox.alert('Success'); 
//              			var l = nodes.length;
//              			var s = l != 1 ? 's' : '';
//              			panelBottom.setTitle('Simple DataView ('+l+' item'+s+' selected)');
//              		}
//              	}
//              },
        writer: new Ext.data.JsonWriter({
                  encode:     true,
                  listful:    false
                  })


	});


	  
	  var sendButton = new Ext.Toolbar.Button({
		    text: 'Send',
		    disabled: true,
		    iconCls: 'cancel',
		    applyTo: 'res-send-button', 
		    handler: function() {
		    	
//		      association_id = dataView.getSelectedRecords()[0].data.id;
//
//		      Ext.Ajax.request({
//		        url: config.baseUrl + '/' + association_id + '.ext_json',
//		        method: 'post',
//		        params: "_method=delete",
//		        success: function() {
//		          imageAssociationStore.reload();
//		        }
//		      });
		    }
		  });

	  
    // little bit of feedback
	dataView.on("click", function(vw, index, node, e){
		Ext.MessageBox.alert('click'); 	

		var jsonText = '{"list": [{"action": "Delete Resource", "target": "delete(32)"},{"action": "Annotate Service", "target": "annotate(32)"}]}';
		
		var jsonReader = new Ext.data.JsonReader({
			id: "action",
			root: "list"}, 
			
			[
		    {name: 'action', type: 'string'},
		    {name: 'target', type: 'string'}
		    ]);
		
		
        var tpl = new Ext.XTemplate(
                '<tpl for="list">',
                    '<span class="res-action-link">{action}</span>',
                '</tpl>'
            );

		  tpl.overwrite(p.body, data);


		
		
//		var resourceSendingURL ="http://localhost:8080/web/52north/start?p_auth=bRZikp9B&p_p_id=ResourceManager_WAR_ResourceManager001SNAPSHOT&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_ResourceManager_WAR_ResourceManager001SNAPSHOT_action=resource-sending";
//	    
//		Ext.Ajax.request({
//			url : resourceSendingURL,
//			params : { resourceId : node.id }, 
//			method: 'GET',
//			success: function ( result, request ) { 
//				Ext.MessageBox.alert('Success'); 
//			},
//			failure: function ( result, request) { 
//				Ext.MessageBox.alert('Failed', result.responseText); 
//			} 
//
//		});
	    // call dwr method directly
	});



    
	 var panelBottom = new Ext.Panel({
	        title: 'Resources', 
	        frame: true, 
	        width: 400,
	        height: 300, 

	        style: 'margin: 0 auto;',
	        renderTo: 'rmpList', 
	        items: [dataView]
	    });






});
