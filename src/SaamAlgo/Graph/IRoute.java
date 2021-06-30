package SaamAlgo.Graph;

import SaamAlgo.Graph.Edge.Edge;

import java.util.List;

public interface IRoute {

    /**
     Get list of Edges of the route (useful to iterate on this route)
     @return List of Edges composing the route (preferably a linkedList to iterate)
     */
    public List<Edge> getRoute();

    /**
     Get the flying time of the route
     @param speed The aircraft speed without the speed in the final leg (after merge point)
     @return flying time in hours
     */
    public double getFlyingTime(int speed); //in kts
}
