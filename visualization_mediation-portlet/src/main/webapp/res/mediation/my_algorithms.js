var redraw;




window.onload = function() {
    var width = $(document).width();
    var height = $(document).height() - 100;

    var render = function(r, n) {
            var set = r.set().push(
                /* custom objects go here */
                r.rect(n.point[0]-30, n.point[1]-13, 60, 44).attr({"fill": "#feb", r : "12px", "stroke-width" : "1px" })).push(
                r.text(n.point[0], n.point[1] + 10, (n.label || n.id)));
            return set;
        };
var render2 = function(r, n) {
            var set = r.set().push(
                /* custom objects go here */
                r.rect(n.point[0]-30, n.point[1]-13, 60, 44).attr({"fill": "#fef", r : "12px", "stroke-width" : "1px" })).push(
                r.text(n.point[0], n.point[1] + 10, (n.label || n.id)));
            return set;
        };
    
    var g = new Graph();
    this.current_graph=g;
    /* creating nodes and passing the new renderer function to overwrite the default one */
/*
    g.addNode("New York", {render:render}); 
    g.addNode("Berlin", {render:render});
    g.addNode("Tel Aviv", {render:render});
    g.addNode("Tokyo", {render:render});
    g.addNode("Roma", {render:render});
    g.addNode("Madrid", {render:render});

 g.addNode("g", {render:render});
 g.addNode("x", {render:render});
 g.addNode("y", {render:render});
 g.addNode("z", {render:render});

g.addNode("a", {render:render});
g.addNode("b", {render:render});
g.addNode("c", {render:render});

*/
    /* connections */
    /*
    g.addEdge("Tokyo", "Tel Aviv", {weight:"1px", directed: true, stroke : "#aaa"}); 
    g.addEdge("Tokyo", "New York", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Tokyo", "Berlin", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Tel Aviv", "Berlin", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Tel Aviv", "New York", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Tel Aviv", "Roma", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Roma", "New York", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Berlin", "New York", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Madrid", "New York", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Madrid", "Roma", {weight:"1px", directed: true, stroke : "#aaa"});
    g.addEdge("Madrid", "Tokyo", {weight:"1px", directed: true, stroke : "#aaa"});

g.addEdge("g", "x", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("g", "y", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("x", "z", {weight:"1px", directed: true, stroke : "#aaa"});

g.addEdge("a", "b", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("a", "c", {weight:"1px", directed: true, stroke : "#aaa"});

*/

    g.addNode("FeatureCollection1-i", {render:render});
    g.addNode("FeatureCollection2-i", {render:render});
    g.addNode("ObservationCollection-i", {render:render});
    g.addNode("WPS", {render:render2}); 
 g.addNode("SOS", {render:render2}); 
    g.addNode("ObservationCollection", {render:render});
 g.addNode("WFS1", {render:render2}); 
    g.addNode("FeatureCollection1", {render:render});
 g.addNode("WFS2", {render:render2}); 
    g.addNode("FeatureCollection2", {render:render});

g.addEdge("SOS", "ObservationCollection", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("WFS1", "FeatureCollection1", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("WFS2", "FeatureCollection2", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("FeatureCollection1-i", "WPS", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("FeatureCollection2-i", "WPS", {weight:"1px", directed: true, stroke : "#aaa"});
g.addEdge("ObservationCollection-i", "WPS", {weight:"1px", directed: true, stroke : "#aaa"});

    /* layout the graph using the Spring layout implementation */
   // var layouter = new Graph.Layout.Spring(g);
var l=[['SOS','ObservationCollection'],['WFS1','FeatureCollection1'],['WFS2','FeatureCollection2']];
var r=[['WPS','FeatureCollection1-i','FeatureCollection2-i','ObservationCollection-i']];
    var layouter = new Graph.Layout.gao(g,width, height,l,r);

    /* draw the graph using the RaphaelJS draw implementation */
    var renderer = new Graph.Renderer.Raphael('canvas', g, width, height);
current_render=renderer ;
current_layouter=layouter ;
    redraw = function() {
        layouter.layout();
        renderer.draw();
    };
};