var redraw;

var width;
var height;
var render;
var render_io;
var l=[['SensappSOS-GAO',':OC'],['Oslo_feature111','feature111:FC']];
var r=[['GaoTemperatureClassifierAlgorithm','FeatureCollectionInput:FC','ObservationCollectionInput:OC']];
function initMediation()
{
	require({context: _contextPathSensor,baseUrl: _contextPathSensor},["dwr/commonjs/amd/engine","dwr/commonjs/amd/interface/sensors"], 
			 function(dwr,sensors) {
		 dwr.engine.setActiveReverseAjax(true);
			dwr.engine.setNotifyServerOnPageUnload(true);
			//alert(_contextPathSensor);
			//alert(portletsessionid);
			sensors.initSession(portletsessionid);
  	});
	
	
	width = 1000;
    height = 400;
    render = function(r, n) {
        var set = r.set().push(
            /* custom objects go here */
            r.rect(n.point[0]-30, n.point[1]-13, 60, 44).attr({"fill": "#feb", r : "12px", "stroke-width" : "1px" })).push(
            r.text(n.point[0], n.point[1] + 10, (n.label || n.id)));
        return set;
    };
    render_io = function(r, n) {
        var set = r.set().push(
            /* custom objects go here */
            r.rect(n.point[0]-30, n.point[1]-13, 60, 44).attr({"fill": "#fef", r : "12px", "stroke-width" : "1px" })).push(
            r.text(n.point[0], n.point[1] + 10, (n.label || n.id)));
        return set;
    };
    var g = new Graph();
    this.current_graph=g;
    initCenterPanelMediation();
    initButtonPanelMediation();
    //drawMediationStart();
}

function drawMediationStart()
{
	drawMediationAll(l,r);
	}

function drawMediationAll(lp,rp)
{
	drawMediation(lp,'L');
	drawMediation(rp,'R');
	var layouter = new Graph.Layout.gao(this.current_graph,width, height,lp,rp);
	var renderer = new Graph.Renderer.Raphael('mediation-draw', this.current_graph, width, height);
	current_render=renderer ;
	current_layouter=layouter ;
}
function drawMediation(p,flag)
{
	//var nodes=p;
	var length_p=p.length;
	
	for(var i=0;i<length_p;i++)
	{
		var group=p[i];
		var first_node;
		var length_group=group.length;
		var count=0;
		for(var j=0;j<length_group;j++)
		{
			var node_id = group[j];
			if(count==0)
			{
				first_node=node_id;
				this.current_graph.addNode(node_id, {render:render});
				count++;
			}
			else if(count>0)
			{
				this.current_graph.addNode(node_id, {render:render_io});
				if(flag=='L')
				{
					this.current_graph.addEdge(first_node, node_id, {weight:"1px", directed: true, stroke : "#aaa"});
				}
				else if(flag=='R')
				{
					this.current_graph.addEdge(node_id,first_node , {weight:"1px", directed: true, stroke : "#aaa"});
				}
			}
		}
	}
}

function initCenterPanelMediation()
{
	 var panel = Ext.getCmp('mediation.panel.center'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="mediation-draw" style="height: 395px;"></div>'];
			 
			var panelCenter = new Ext.Panel({
				id: 'mediation.panel.center',
				frame : true,
				height: 460,  
				style : 'margin: 0 auto;',
				renderTo : 'center-mediation',
				html: html,
				preventBodyReset: true
			});
			panel=panelCenter;
			this.width=panelCenter.body.dom.clientWidth;
			//alert(this.width);
		}
	
}

function initButtonPanelMediation()
{
	var panel = Ext.getCmp('mediation.panel.button'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="f"></div>'];
			 
			var panelButton = new Ext.Panel({
				id: 'mediation.panel.button',
				frame : true,
				height: 20,  
				style : 'margin: 0 auto;',
				renderTo : 'button-mediation',
				html: html,
				bbar: [],
				preventBodyReset: true
			});
			panel=panelButton;
		}
	 var tb = panel.getBottomToolbar(); 
		var mediationAction = new Ext.Action({itemId: 'mediationAction',text: 'Build',iconCls: 'copy-icon',
			handler: function() {
				//isSet=1;
				//initCenterPanelSES();
				//initChart();
				//initObservationDataSES();
				
			}
		}); 
		
		tb.add('->', mediationAction);
}
