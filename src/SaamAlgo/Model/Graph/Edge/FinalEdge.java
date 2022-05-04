package SaamAlgo.Model.Graph.Edge;

import SaamAlgo.Model.Constants;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Graph.Node.Node;

public class FinalEdge extends Edge{

    public FinalEdge(Node node1, Node node2, String name) {
        super(node1, node2, name);
    }

    @Override
    public void addFlyingAircraft(Aircraft aircraft, double entryTime){

        double exitTime = entryTime + (getLength() / aircraft.landingSpeed);
        EdgeFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);
        getFlyingAircraftIn().put(entryTime, flight);
        getFlyingAircraftOut().put(exitTime, flight);
    }

    /**
     * @return in hours
     */
    @Override
    public double getFlyingTime(Aircraft aircraft){
        return super.getLength() / aircraft.landingSpeed;
    }

}
