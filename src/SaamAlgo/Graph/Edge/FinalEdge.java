package SaamAlgo.Graph.Edge;

import SaamAlgo.Operations.Constants;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Graph.Node.Node;

public class FinalEdge extends Edge{

    public FinalEdge(Node node1, Node node2, String name) {
        super(node1, node2, name);
    }

    @Override
    public void addFlyingAircraft(Aircraft aircraft, double entryTime){

        double exitTime = entryTime + (getLength() / aircraft.getFinalSpeed()) * Constants.HOURS_TO_SEC + aircraft.getTimeInArc();
        EdgeFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);
        getFlyingAircraftsIn().put(entryTime, flight);
        getFlyingAircraftsOut().put(exitTime, flight);
    }

    public double getLength(Aircraft aircraft){
        return super.getLength() + aircraft.getFinalSpeed() * Constants.standardTimeInArc * Constants.SEC_TO_HOURS;
    }



}
