/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.onReady(function(){
	var jsonText = '{"list": [    {"res": "cdc_112", "state": "20", "tags": "envision, simple-use-case"}    {"res": "cdc_111", "state": "20", "tags": "envision, simple-use-case"}    {"res": "sos", "state": "20", "tags": "envision, simple-use-case"}   ]}'; 
	
	var RecordDef = Ext.data.Record.create([
      {name: 'state'},     // "mapping" property not needed if it's the same as "name"
	  {name: 'tags'},                // This field will use "occupation" as the mapping.
	]);

	var jsonReader = new Ext.data.JsonReader({
	       root: "list",                // The property which contains an Array of record objects
	       id: "res"                     // The property within the record object that provides an ID for the record (optional)
	   }, RecordDef);

	var data = jsonReader.read(resources.listServices());
	
	 var store = new Ext.data.ArrayStore({
	        fields: [
	           {name: 'identifier'},
	           {name: 'state'},
	           {name: 'tags'}
	        ]
	    });
	 store.loadDate(data);
	

//    // Custom rendering Template for the View
//    var resultTpl = new Ext.XTemplate(
//        '<tpl for=".">',
//        '<div class="resource-item">',
//            '<h3>{identifier}<br />by {author}</span>',
//            '<a href="http://extjs.com/forum/showthread.php?t={topicId}&p={postId}" target="_blank">{title}</a></h3>',
//            '<p>{excerpt}</p>',
//        '</div></tpl>'
//    );
//
//    var panel = new Ext.Panel({
//        applyTo: 'rmp-list',
//        title:'Resource Manager',
//        height:300,
//        autoScroll:true,
//
//        items: new Ext.DataView({
//            tpl: resultTpl,
//            store: store,
//            itemSelector: 'div.resource-item'
//        }),
//
////        tbar: [
////            'Search: ', ' ',
////            new Ext.ux.form.SearchField({
////                store: ds,
////                width:320
////            })
////        ],
//
//        bbar: new Ext.PagingToolbar({
//            store: ds,
//            pageSize: 20,
//            displayInfo: true,
//            displayMsg: 'Resources {0} - {1} of {2}',
//            emptyMsg: "No resources to display"
//        })
//    });
//
//    ds.load({params:{start:0, limit:20, forumId: 4}});
});
