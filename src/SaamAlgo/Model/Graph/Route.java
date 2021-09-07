package SaamAlgo.Model.Graph;

import SaamAlgo.Model.Graph.Edge.Edge;
import SaamAlgo.Model.Graph.Edge.FinalEdge;
import SaamAlgo.Model.Graph.Node.Node;

import java.util.List;

public class Route{

    private final List<Edge> route;
    private double length;
    //private final double finalLegLength;

    public Route(List<Edge> route){
        this.route = route;

        int size = route.size();
        for(int i = 0; i < size - 1; i++ ){
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
    public double getFlyingTime(double approachSpeed, double landingSpeed){

        double finalLegLength = ((FinalEdge) route.get((route.size()) -1)).getLength(landingSpeed);

        double timeInHours = (length - finalLegLength)/approachSpeed + finalLegLength / landingSpeed; // in hours;
        return timeInHours * 3600;
    }

    /**
     Get list of Edges of the route (useful to iterate on this route)
     @return List of Edges composing the route (preferably a linkedList to iterate)
     */
    public List<Edge> getEdges() {
        return route;
    }

    @Override
    public String toString() {
        return "Route{" +
                "route=" + route +
                '}';
    }
}