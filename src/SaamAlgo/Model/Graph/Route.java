package SaamAlgo.Model.Graph;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Graph.Edge.Arc;
import SaamAlgo.Model.Graph.Edge.Edge;
import SaamAlgo.Model.Graph.Edge.FinalEdge;
import SaamAlgo.Model.Graph.Node.Node;

import java.util.List;

public class Route{

    private final List<? extends Edge> route;
    private double length;
    private final Arc arc;
    private final FinalEdge finalEdge;
    //private final double finalLegLength;

    public Route(List<? extends Edge> route){
        this.route = route;

        arc = route.get(route.size() -2).toArc();
        finalEdge = (FinalEdge) route.get((route.size() - 1));

        int size = route.size();
        for(int i = 0; i < size - 3; i++ ){
            this.length += route.get(i).getLength();
        }

    }

    public Node getFirstNode(){
        return route.get(0).getEntryNode();
    }

    public Node getLastNode(){
        return route.get(route.size() -1).getExitNode();
    }

    /**
     Get the flying time of the route
     @return flying time in hours
     */
    public double getFlyingTime(Aircraft aircraft){

        return  (length/ aircraft.getSpeed()) + arc.getFlyingTime(aircraft) + finalEdge.getFlyingTime(aircraft); // in hours;
    }

    public double getLength(Aircraft aircraft){
        return length + arc.getLength(aircraft) + finalEdge.getLength();
    }

    /**
     Get list of Edges of the route (useful to iterate on this route)
     @return List of Edges composing the route (preferably a linkedList to iterate)
     */
    public List<? extends Edge> getEdges() {
        return route;
    }

    @Override
    public String toString() {
        return "Route{" +
                "route=" + route +
                '}';
    }
}