var chart;
var SESurl,SESversion;
var offering, procedure, observedProperty;
var isSet=0;
var dataset=[];
var SESResourceId;
function initSES()
{
	isSet=0;
	
	//Ext.EventManager.on(window, 'beforeunload', function(){
	//	alert('close ses:'+SESResourceId);
	//	sensorSESDWR.doUnsubscribe(SESResourceId);		
	//});
//	window.onbeforeunload = function(e){
//		sensorSESDWR.doUnsubscribe(SESResourceId);		
//		dwr.engine._unloader;
//		};
	
	
	initCenterPanelSES();
	initButtonPanelSES();
	initChart();
	
}

function initCenterPanelSES()
{
	 var panel = Ext.getCmp('sensorSES.panel.center'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="SES" style="height: 395px;"></div>'];
			 
			var panelCenter = new Ext.Panel({
				id: 'sensorSES.panel.center',
				frame : true,
				height: 460,  
				style : 'margin: 0 auto;',
				renderTo : 'centerSES',
				html: html,
				preventBodyReset: true
			});
			panel=panelCenter;
		}
	
}

function initButtonPanelSES()
{
	var panel = Ext.getCmp('sensorSES.panel.button'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="d"></div>'];
			 
			var panelButton = new Ext.Panel({
				id: 'sensorSES.panel.button',
				frame : true,
				height: 20,  
				style : 'margin: 0 auto;',
				renderTo : 'buttonSES',
				html: html,
				bbar: [],
				preventBodyReset: true
			});
			panel=panelButton;
		}
	 var tb = panel.getBottomToolbar(); 
		var cleanSESAction = new Ext.Action({itemId: 'cleanSESAction',text: 'clean',iconCls: 'copy-icon',
			handler: function() {
				isSet=1;
				initCenterPanelSES();
				initChart();
				//initObservationDataSES();
				
			}
		}); 
		
		tb.add('->', cleanSESAction);
}

function initChart()
{
	//jQuery(document).ready(function() {
		chart = new Highcharts.Chart({
			chart: {
				renderTo: 'SES',
				type: 'spline',
				zoomType:'x'
			},
			title: {
				text: ''
			},
			subtitle: {
				text: ''	
			},
			xAxis: {
				type: 'datetime',
				maxZoom: 30000, 
				//tickInterval:3600000*2,
				dateTimeLabelFormats: { // don't display the dummy year
					second: '%H:%M:%S',
					minute: '%H:%M',
					hour: '%H:%M',
					day: '%e. %b',
					week: '%e. %b',
					month: '%b %y',
					year: '%Y'
				}
			},
			yAxis: {
				title: {
					text: 'Sensing value'
				}
				
			},
			tooltip: {
				formatter: function() {
		                return '<b>'+ this.series.name +'</b><br/>'+
						Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y +' m';
				}
			},
    plotOptions:{//绘图线条控制
        spline:{
            allowPointSelect :true,//是否允许选中点
            animation:true,//是否在显示报表的时候使用动画
            cursor:'pointer',//鼠标放在点上的时候显示的形状
            dataLabels:{
                enabled :true,//是否在点的旁边显示数据
                rotation:0
            },
            enableMouseTracking:true,//鼠标放上去是否显示那个小方框
            events:{//监听点的鼠标事件
                click: function() {
					//series = chart.series[0];
					//var x=Date.UTC(1971,  6, 5);
					//var y=1;											// Add it
					//series.addPoint([x, y]);					
                }
            },
            marker:{
                enabled:true,//是否显示点
                radius:3,//点的半径
                //fillColor:"#888"
                //lineColor:"#000"
                //symbol: 'url(http://highcharts.com/demo/gfx/sun.png)',//这个可以让点用图片来显示
                states:{
                    hover:{
                        enabled:true//鼠标放上去点是否放大
                    },
                    select:{
                        enabled:false//控制鼠标选中点时候的状态
                    }
                }
            },
            states:{
                hover:{
                    enabled:true,//鼠标放上去线的状态控制
                    lineWidth:3
                }
            },
            stickyTracking:true,//跟踪
            visible:true,
            lineWidth:2//线条粗细
            //pointStart:100,
        }
    },
			series: [{
				name: 'SES Event',
				// Define the data points. All series have a dummy year
				// of 1970/71 in order to be compared on the same x axis. Note
				// that in JavaScript, months start at 0 for January, 1 for February etc.
				data: []
			}		
			]
		});
		
		
	//});
}

function initDataSES(sensor)
{
	var properties=sensor.split('*');
	if(properties.length>=3)
	{
		offering=properties[0];
		procedure=properties[1];
		observedProperty=properties[2];			
	}
	
	initSubscribeSES();
	//initCenterPanelSES();
	//initChart();
}

function initSubscribeSES()
{	
	require({baseUrl: _contextPathSensor},["dwr/commonjs/amd/interface/sensorSESDWR"], 
			 function(sensorSESDWR) {
		
		sensorSESDWR.doSubscribe(procedure,SESResourceId,portletsessionid,reSetResourceId);
  	});
	
	isSet=1;
	initCenterPanelSES();
	initChart();				
}

function reSetResourceId(newid)
{
	SESResourceId=newid;
}


function initPointSES(sesNotification)
{
	properties=sesNotification.split('*');
	if(properties!=null)
	if(properties.length>=3)
	{
		series = chart.series[0];
		var x=parseInt(properties[1]);
		var y=parseInt(properties[0]);
		series.addPoint([x, y]);
	}	
}

