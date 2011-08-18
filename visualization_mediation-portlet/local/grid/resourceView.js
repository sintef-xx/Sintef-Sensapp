/**
 * 
 */



Ext.onReady(function() {
	renderView(); 

});

var resourcesDataView; 
var actionsDataView; 
var searchToolbar; 
var panel; 
var actionsStore; 

function renderView() {
	
	
	// render search panel 
	buildSearchToolbar(); 
	
	
	// get resources encoded in json from DWR-exposed interface (we have to define a callback method for this
	resources.listResources(buildResView);
	// resources.listActions(null, buildActionsView);
	
	// render default actions
	buildActionsView(defaultActions); 
	

	
	renderResPanel([resourcesDataView, actionsDataView]); 
}

var updatePanel = function () {
	if(panel) panel.render(); 
}

var renderResPanel = function (views) {
	panel = new Ext.Panel({
		//title: 'Resources', 
		frame : true,
		  height: 350, 
    width: 250,

		style : 'margin: 0 auto;',
		
		items : views, 
		tbar : searchToolbar
		
	});
	
	panel.render('rmpList');
	
}




var updateActionsView = function (json) {
	var data = Ext.util.JSON.decode(json);
	
	actionsStore.loadData(data); 
	actionsDataView.refresh(); 
}

var buildSearchToolbar = function() {
searchToolbar = new Ext.Toolbar({
   
    autoHeight: true, 
    autoWidth: true,
    items: [
	 	{
            xtype: 'textfield',
            name: 'field1',
            emptyText: 'enter search term'
        },
        {
            // xtype: 'button', // default for Toolbars, same as 'tbbutton'
            text: 'Search'
        }

    ]
});
}

function clickHandler(btn) {
  Ext.Msg.alert('clickHandler', 'button pressed');
}
function toggleHandler(item, pressed) {
  Ext.Msg.alert('toggleHandler', 'toggle pressed');
}

var buildActionsView = function (json) {
	var data = Ext.util.JSON.decode(json);
	

	// create new memory proxy (the store needs it, this usually directly a HTTP proxy)
	var proxy = new Ext.data.MemoryProxy(data);

	// create the reasoner, with the variable mappings
	var jsonReader = new Ext.data.JsonReader({id : "res",root : "list"}, [ {name : 'res',type : 'string'}, {name : 'action',type : 'string'}, {name : 'callback',type : 'string'} ]);
	
	// the store basically links the proxy with the reader
	actionsStore = new Ext.data.Store({
		proxy : proxy,
		reader : jsonReader
	});

	// initiate loading from proxy to reader
	actionsStore.load();

	// the template for the individual items in the list
	var tpl = new Ext.XTemplate('<tpl for=".">',
			'<span class="res-action"><a href="#"> {action} </a> </span><br>',
			'</div></tpl>');

	// the data view, see also http://dev.sencha.com/deploy/dev/examples/view/data-view.html
	actionsDataView = new Ext.DataView({
		autoScroll : true,
		store : actionsStore,
		tpl : tpl,
		autoHeight : false,
		height : 265,
		multiSelect : false,
		itemSelector : 'div.res-wrap',
		overClass : 'x-view-over',
		emptyText : 'No resources to display',
		style : 'border:1px solid #99BBE8;background:#fff;',
		plugins : [

		new Ext.DataView.LabelEditor({
			dataIndex : 'tags'
		}) ],
		writer : new Ext.data.JsonWriter({
			encode : true,
			listful : false
		})

	});
	
}

var buildResView = function (json) {
	var data = Ext.util.JSON.decode(json);

	// create new memory proxy (the store needs it, this usually directly a HTTP proxy)
	var memoryProxy = new Ext.data.MemoryProxy(data);

	// create the reasoner, with the variable mappings
	var jsonReader = new Ext.data.JsonReader({
		id : "res",
		root : "list"
	}, [ {
		name : 'res',
		type : 'string'
	}, {
		name : 'type',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'tags',
		type : 'string'
	} ]);

	// the store basically links the proxy with the reader
	var store = new Ext.data.Store({
		proxy : memoryProxy,
		reader : jsonReader
	});

	// initiate loading from proxy to reader
	store.load();

	// the template for the individual items in the list
	var tpl = new Ext.XTemplate('<tpl for=".">',
			'<div class="res-wrap" id="{res}">',
			'<div class="res-row" > <span class="res-name">{res}</span>',
			'<span class="res-state">{state}</span></div>',
			'Type: <span class="res-type">{type}</span><br />',
			'Tags: <span class="x-editable">{tags}</span> ', '</div></tpl>');

	// the data view, see also http://dev.sencha.com/deploy/dev/examples/view/data-view.html
	resourcesDataView = new Ext.DataView({
		autoScroll : true,
		store : store,
		tpl : tpl,
		autoHeight : false,
		height : 265,
		multiSelect : false,
		itemSelector : 'div.res-wrap',
		overClass : 'x-view-over',
		emptyText : 'No resources to display',
		style : 'border:1px solid #99BBE8;background:#fff;',
		plugins : [

		new Ext.DataView.LabelEditor({
			dataIndex : 'tags'
		}) ],
		writer : new Ext.data.JsonWriter({
			encode : true,
			listful : false
		})

	});

	// little bit of feedback
	resourcesDataView.on("click", function(dv, index, node, e) {
		 Ext.each(dv.store.data.items,
            function(item) {
                item.data.selected = 0;
            }
        );
		resources.listActions(null, updateActionsView);
 		 
	});


}