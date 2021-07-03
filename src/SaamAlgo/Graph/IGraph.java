package SaamAlgo.Graph;

import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Model.Aircraft;
import org.jetbrains.annotations.NotNull;

public interface IGraph {

    /**
     Get THE Route between a entry Node and an exit Node
     @param entry The entry point (Node) in TMA
     @param runway The exit point (Node) ie a runway
     @return a Route such as specified in IRoute
     */
    Route getRoute(Node entry, Node runway);

    /**
     Add an aircraft to the graph, to the Nodes and Edges specified in aircraft.getRoute()
     @param aircraft : the aircraft to add
     */
    void addAircraft(@NotNull Aircraft aircraft);

    /**
     Remove an aircraft to the graph, to the Nodes and Edges specified in aircraft.getRoute()
     @param aircraft : the aircraft to remove
     */
    void removeAircraft(Aircraft aircraft);
}
