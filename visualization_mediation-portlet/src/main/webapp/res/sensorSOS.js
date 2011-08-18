var chartSOS;
var sosurl,sosversion;
var offering, procedure, observedProperty;
var isSet=0;
var dataset=[];
function initSOS()
{
	isSet=0;
	initCenterPanelSOS();
	initButtonPanelSOS();
	initchartSOS();
}

function initCenterPanelSOS()
{
	 var panel = Ext.getCmp('sensorSOS.panel.center'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="sos" style="height: 395px;"></div>'];
			 
			var panelCenter = new Ext.Panel({
				id: 'sensorSOS.panel.center',
				frame : true,
				height: 460,  
				style : 'margin: 0 auto;',
				renderTo : 'centerSOS',
				html: html,
				preventBodyReset: true
			});
			panel=panelCenter;
		}
	
}

function initButtonPanelSOS()
{
	var panel = Ext.getCmp('sensorSOS.panel.button'); 
	 if(typeof panel != "undefined") {
			panel.destroy(); 
		}
	 panel=null;
	 if(!panel) {
			var html = ['<div id="b"></div>'];
			 
			var panelButton = new Ext.Panel({
				id: 'sensorSOS.panel.button',
				frame : true,
				height: 20,  
				style : 'margin: 0 auto;',
				renderTo : 'buttonSOS',
				html: html,
				bbar: [],
				preventBodyReset: true
			});
			panel=panelButton;
		}
	 var tb = panel.getBottomToolbar(); 
		var refreshSOSAction = new Ext.Action({itemId: 'refreshSOSAction',text: 'Refresh',iconCls: 'copy-icon',
			handler: function() {
				isSet=1;
				initCenterPanelSOS();
				initchartSOS();
				initObservationDataSOS();
				
			}
		}); 
		
		tb.add('->', refreshSOSAction);
}

function initchartSOS()
{
	//jQuery(document).ready(function() {
		chartSOS = new Highcharts.Chart({
			chart: {
				renderTo: 'sos',
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
    plotOptions:{//��ͼ��������
        spline:{
            allowPointSelect :true,//�Ƿ�����ѡ�е�
            animation:true,//�Ƿ�����ʾ�����ʱ��ʹ�ö���
            cursor:'pointer',//�����ڵ��ϵ�ʱ����ʾ����״
            dataLabels:{
                enabled :true,//�Ƿ��ڵ���Ա���ʾ����
                rotation:0
            },
            enableMouseTracking:true,//������ȥ�Ƿ���ʾ�Ǹ�С����
            events:{//�����������¼�
                click: function() {
					//series = chartSOS.series[0];
					//var x=Date.UTC(1971,  6, 5);
					//var y=1;											// Add it
					//series.addPoint([x, y]);					
                }
            },
            marker:{
                enabled:true,//�Ƿ���ʾ��
                radius:3,//��İ뾶
                //fillColor:"#888"
                //lineColor:"#000"
                //symbol: 'url(http://highchartSOSs.com/demo/gfx/sun.png)',//��������õ���ͼƬ����ʾ
                states:{
                    hover:{
                        enabled:true//������ȥ���Ƿ�Ŵ�
                    },
                    select:{
                        enabled:false//�������ѡ�е�ʱ���״̬
                    }
                }
            },
            states:{
                hover:{
                    enabled:true,//������ȥ�ߵ�״̬����
                    lineWidth:3
                }
            },
            stickyTracking:true,//����
            visible:true,
            lineWidth:2//������ϸ
            //pointStart:100,
        }
    },
			series: [{
				name: 'SOS Observation',
				// Define the data points. All series have a dummy year
				// of 1970/71 in order to be compared on the same x axis. Note
				// that in JavaScript, months start at 0 for January, 1 for February etc.
				data: []
			}		
			]
		});
		
		
	//});
}


function initDataSOS(sensor)
{
	Ext.get('SOSMessage').mask("loading SOS Observation data."); 
	var properties=sensor.split('*');
	if(properties.length>=5)
	{
		offering=properties[0];
		procedure=properties[1];
		observedProperty=properties[2];
		sosurl=properties[3];
		sosversion=properties[4];
		isSet=1;		
	}
	
	initObservationDataSOS();
	
	//initCenterPanelSOS();
	//initchartSOS();
}

function initObservationDataSOS()
{
	if(isSet==1)
	{
		require({baseUrl: _contextPathSensor},["dwr/commonjs/amd/interface/sensorSOSDWR"], 
				 function(sensorSOSDWR) {
			
			sensorSOSDWR.listObservation(sosurl,sosversion,offering,procedure,observedProperty,ObservationHandler);	
	  	});
		
		
	}	
}

function ObservationHandler(observations)
{
	if(observations!=null)
	{
		var observationList=observations.split(';');
		dataset=[];
		if(observationList!=null)
		{
			for(var i=0; i< observationList.length; i++)
			{
				var observation=observationList[i];
				initPointSOS(observation);
			}
			
		}
	}
	Ext.get('SOSMessage').unmask();
}

function initPointSOS(observation)
{
	properties=observation.split('*');
	if(properties!=null)
	if(properties.length>=5)
	{
		series = chartSOS.series[0];
		var x=parseInt(properties[4]);
		var y=parseFloat(properties[3]);
		series.addPoint([x, y]);
		//chartSOS.redraw();
		//var d=[properties[4], properties[3]];
		//dataset.push(d);
		//alert(properties[4]+"*"+properties[3]);
	}	
}


//

 //  [1300232553000,29.9],
//   [1300284727000,23.0],
//   [1300284787000,-13.5],
 //  [1300284847000,23.0],
 //  [1300284907000,23.0]
 //1300232553000*29.9   
 //1300284727000*23.0
 //1300284787000*-13.5
 //1300284847000*23.0
 //1300284907000*23.0
 //1300284967000*23.0
 //1300285027000*23.0
 //1300285207000*24.5
 //1300285267000*-14.5
 //1300285327000*-15.5
 //1300285387000*17.5
 //1300285447000*15.5
 //1300285507000*-17.5
 //1300285567000*13.5
 //1300285687000*-19.0
 //1300364608000*29.9     
   
