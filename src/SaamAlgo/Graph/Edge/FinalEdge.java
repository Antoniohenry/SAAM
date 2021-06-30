package SaamAlgo.Graph.Edge;

import SaamAlgo.Operations.Constants;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Graph.Node.Node;

public class FinalEdge extends Edge{

    public static final double SPEED = 180;

    public FinalEdge(Node node1, Node node2, String name) {
        super(node1, node2, name);
    }

    @Override
    public void addFlyingAircraft(Aircraft aircraft, double entryTime){
        double exitTime = entryTime + (getLength() / SPEED) + aircraft.getTimeInArc();
        EdgeFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);
        getFlyingAircraftsIn().put(entryTime, flight);
        getFlyingAircraftsOut().put(exitTime, flight);
        //this.getExitNode().addAircraft(aircraft, exitTime);
    }

    @Override
    public double getLength(){
        return super.getLength() + SPEED * Constants.standardTimeInArc;
    }

}
