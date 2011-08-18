Graph.Layout = {};
Graph.Layout.gao = function(graph, w, h, L, R) {
	//width, height
    this.graph = graph;
    this.iterations = 500;
    this.maxRepulsiveForceDistance = 6;
    this.k = 2;
    this.c = 0.01;
    this.maxVertexMovement = 0.5;
    
    this.width_m=w;
    this.hight_m=h;
    this.L_Nodes=L;
    this.R_Nodes=R;
    this.layout();
};
Graph.Layout.gao.prototype = {
	    layout: function() {
	var width = this.width_m;
   // var height = $(document).height() - 100;
	var height=this.hight_m;
	var div_L=0;
	var div_r=0;
	
	div_L=this.L_Nodes.length;
	div_R=this.R_Nodes.length;
	var count_g=0;
	for(var i=0;i<div_L;i++)
	{
		var group=this.L_Nodes[i];
		var group_Hight=height/div_L/2+height/div_L*count_g;
		var count=0;
		var group_length=group.length;
		var group_Hight_small=height/div_L/group_length;
		for(var j=0;j<group_length;j++)
		{
			var node_id = group[j];
			var node=this.graph.nodes[node_id];
			
			if(count==0)
			{
				node.layoutPosX = width/5;
	 			node.layoutPosY = group_Hight;
			}
			else
			{
				node.layoutPosX = width/5+width/5;
				node.layoutPosY = group_Hight_small/2+group_Hight_small*count+height/div_L*count_g;
			}
			count++;
		}
		count_g++;
	}
	count_g=0;
	for (var r=0;r<div_R;r++)
	{
		var group=this.R_Nodes[r];
		var group_Hight=height/div_R/2+height/div_R*count_g;
		var count=0;
		var group_length=group.length;
		var group_Hight_small=height/div_R/group_length;
		for(var j=0;j<group_length;j++)
		{
			var node_id = group[j];
			var node=this.graph.nodes[node_id];
			if(count==0)
			{
				node.layoutPosX = width*4/5;
	 			node.layoutPosY = group_Hight;
			}
			else
			{
				node.layoutPosX = width*3/5;
				node.layoutPosY = group_Hight_small/2+group_Hight_small*count+height/div_L*count_g;
			}
			count++;
		}
		count_g++;
	}

	 	this.layoutCalcBounds();
	},
	layoutCalcBounds: function() {
	        var minx = Infinity, maxx = -Infinity, miny = Infinity, maxy = -Infinity;

	        for (i in this.graph.nodes) {
	            var x = this.graph.nodes[i].layoutPosX;
	            var y = this.graph.nodes[i].layoutPosY;
	            
	            if(x > maxx) maxx = x;
	            if(x < minx) minx = x;
	            if(y > maxy) maxy = y;
	            if(y < miny) miny = y;
	        }

	        this.graph.layoutMinX = minx;
	        this.graph.layoutMaxX = maxx;

	        this.graph.layoutMinY = miny;
	        this.graph.layoutMaxY = maxy;
	    }
}
	    