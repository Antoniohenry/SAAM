package SaamAlgo.Graph;

import SaamAlgo.Graph.Edge.Edge;
import SaamAlgo.Graph.Edge.FinalEdge;
import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Operations.Constants;

import java.util.List;

public class Route implements IRoute{

    private final List<Edge> route;
    //private final double length;
    //private final double finalLegLength;

    public Route(List<Edge> route){
        this.route = route;
        //this.length = setTotalLength();
        //this.finalLegLength = getFinalLegLength();
    }

    public Node getFirstNode(){
        return route.get(0).getEntryNode();
    }

    public Node getLastNode(){
        return route.get(route.size() -1).getExitNode();
    }

/*
    private double setTotalLength(){
        double length = 0;
        for(Edge edge : this.route){
            length += edge.getLength();
        }
        return  length;
    }

    private double getFinalLegLength(){
        FinalEdge finalEdge = (FinalEdge) route.get(route.size() - 1);
        return finalEdge.getLength();
    }*/

    public double getFlyingTime(double approachSpeed, double landingSpeed){
        double length = 0;
        int size = route.size();
        for(int i = 0; i < size - 1; i++ ){
            length += route.get(i).getLength();
        }
        double finalLegLength = ((FinalEdge) route.get(size -1)).getLength(landingSpeed);

        double timeInHours = (length - finalLegLength)/approachSpeed + finalLegLength / landingSpeed; // in hours;
        return timeInHours * 3600;
    }

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