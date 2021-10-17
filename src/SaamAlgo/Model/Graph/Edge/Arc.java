package SaamAlgo.Model.Graph.Edge;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Constants;
import SaamAlgo.Model.Graph.Node.Node;

public class Arc extends Edge {

    public Arc(Node node1, Node node2, String name) {
        super(node1, node2, name);
    }

    @Override
    public void addFlyingAircraft(Aircraft aircraft, double entryTime){

        double exitTime = entryTime + (getLength() / aircraft.getSpeed()) * Constants.HOURS_TO_SEC + aircraft.getTimeInArc();
        EdgeFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);
        getFlyingAircraftIn().put(entryTime, flight);
        getFlyingAircraftOut().put(exitTime, flight);
    }

    public double getLength(Aircraft aircraft){
        return super.getLength() + aircraft.getSpeed() * (aircraft.getTimeInArc() * Constants.SEC_TO_HOURS);
    }


    /**
     * @return in hours
     */
    @Override
    public double getFlyingTime(Aircraft aircraft){
        return (super.getLength() / aircraft.getSpeed()) + aircraft.getTimeInArc() * Constants.SEC_TO_HOURS;
    }

}
