package SaamAlgo.Graph;

import SaamAlgo.Graph.Edge.Edge;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Operations.IAgent;

import java.util.List;

public interface IRoute {

    /**
     Get list of Edges of the route (useful to iterate on this route)
     @return List of Edges composing the route (preferably a linkedList to iterate)
     */
    List<Edge> getEdges();

    /**
     Get the flying time of the route
     @param aircraft : the aircraft flying
     @return flying time in hours
     */
    double getFlyingTime(double approachSpeed, double landingSpeed);
}
