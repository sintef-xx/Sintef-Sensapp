Ext.onReady(function(){
 
	// from http://superdit.com/2010/07/27/get-latest-tweet-using-extjs-dataview-and-php/
	var jsonText = '{"list": [    {"res": "cdc_112", "state": "20", "tags": "envision, simple-use-case"}    {"res": "cdc_111", "state": "20", "tags": "envision, simple-use-case"}    {"res": "sos", "state": "20", "tags": "envision, simple-use-case"}   ]}';
	var data = Ext.decode(jsonText);
	
    var strTweets = new Ext.data.Store({
        proxy: new Ext.data.MemoryProxy(jsonText),
        reader: new Ext.data.JsonReader({
    		root: "list"}, [
    		        	    {name: 'res', type: 'string'},
    		        	    {name: 'state',	type: 'string'},
    		        	    {name: 'tags',	type: 'string'}
        ])
       
    });


    strTweets.load(); 
    
    var tplTweets = new Ext.XTemplate(
        '<tpl for=".">',
            '<div class="tweetbox">{res}</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
    );
    
    var dataTweets = new Ext.DataView({
        autoScroll: true, 
        store: strTweets, 
        tpl: tplTweets,
        autoHeight: false, 
        height: 265,
        multiSelect: true, 
        itemSelector: 'div.thumb-wrap',
        emptyText: 'No tweets to display',
        style: 'border:1px solid #99BBE8;background:#fff;'
    });
    
    var panelTop = new Ext.FormPanel({
        title: 'Search Resource', 
        width: 400,
        renderTo: 'top',
        buttonAlign: 'right',
        labelWidth: 150, 
        fileUpload: true,
        frame: true, 
        style: 'margin: 0 auto;',
        id: 'panelTop',
        items: [{
            xtype: 'textfield',
            emptyText: '',
            fieldLabel: 'Search',
            buttonText: 'Select a File',
            width: 230,
            id: 'twitter_username'
        }],
        buttons: [{
            id: 'btnGetTweets',
            text: 'Latest Tweets',
            handler: getTweets
        }],
        keys: [{
            key: [Ext.EventObject.ENTER], 
            handler: getTweets
        }]
    });


    var panelBottom = new Ext.Panel({
        title: 'Latest Tweets', 
        frame: true, 
        width: 400,
        height: 300, 
        id: 'panelBottom', 
        style: 'margin: 0 auto;',
        renderTo: 'bottom', 
        items: [dataTweets]
    });
    

    function getTweets() {
        var u = Ext.getCmp('twitter_username').getValue();
        proxyTweets.setUrl('get-tweets.php?username=' + u, false);
        strTweets.load();
        Ext.getCmp('panelBottom').setTitle('Latest Tweets From ' + u);
    }
});
