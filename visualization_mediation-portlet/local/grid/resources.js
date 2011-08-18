/**
 *  Mockup for resources
 */


if (typeof this['resources'] == 'undefined') this.resources = {};

resources._path = '/resource-portlet-0.2-SNAPSHOT/dwr';

var jsonText = '{	    "list": [	             {	                 "res": "cdc_112",	                 "type": "OGC Feature Collection",	                 "state": "Not yet annotated",	                 "tags": "corine, landcover, urban areas, continuoues"	             },	             {	                 "res": "cdc_111",	                 "type": "OGC Feature Collection",	                 "state": "Not yet annotated",	                 "tags": "corine, landcover, urban areas"	             },	             {	                 "res": "sos",	                 "type": "OGC Observation Collection",	                 "state": "Not yet annotated",	                 "tags": "sensor, ground water, depth"	             }	         ]	     }';
var actionText =  '{"list": [{"action": "Delete Resource", "callback": "delete", "res": "cdc_112"},{"action": "Add Resource", "target": "add", "res": ""}]}';
var defaultActions =  '{"list": [{"action": "Add Resource", "target": "add", "res": ""}]}';

resources.listResources = function(callback) {
	callback(jsonText); 
};


resources.listActions = function(p0, callback) {
	callback(actionText); 
};
