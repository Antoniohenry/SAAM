package SaamAlgo.Graph;

import SaamAlgo.Graph.Edge.Edge;
import SaamAlgo.Graph.Edge.FinalEdge;
import SaamAlgo.Operations.Constants;

import java.util.List;

public class Route implements IRoute{

    private final List<Edge> route;
    private final double length;
    private final double finalLegLength;

    public Route(List<Edge> route){
        this.route = route;
        this.length = setTotalLenght();
        this.finalLegLength = getFinalLegLength();
    }

    private double setTotalLenght(){
        double length = 0;
        for(Edge edge : this.route){
            length += edge.getLength();
        }
        return  length;
    }

    private double getFinalLegLength(){
        FinalEdge finalEdge = (FinalEdge) route.get(route.size() - 1);
        return finalEdge.getLength();
    }

    public double getFlyingTime(int speed){
        return (length - finalLegLength)/speed + finalLegLength/ Constants.speedInArc;
    }

    public List<Edge> getRoute() {
        return route;
    }

    public double getLength() {
        return length;
    }
}