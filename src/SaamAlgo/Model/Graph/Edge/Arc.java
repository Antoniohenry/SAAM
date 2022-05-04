package SaamAlgo.Model.Graph.Edge;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Constants;
import SaamAlgo.Model.Graph.IFlight;
import SaamAlgo.Model.Graph.KeyError;
import SaamAlgo.Model.Graph.Node.Node;

public class Arc extends Edge {

    public Arc(Node node1, Node node2, String name) {
        super(node1, node2, name);
    }

    @Override
    public void addFlyingAircraft(Aircraft aircraft, double entryTime){

        double exitTime = entryTime + (getLength() + aircraft.getArcLength() / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        EdgeFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);
        getFlyingAircraftIn().put(entryTime, flight);
        getFlyingAircraftOut().put(exitTime, flight);
    }

    public double getLength(Aircraft aircraft){
        return super.getLength() + aircraft.getArcLength();
    }


    /**
     * @return in hours
     */
    @Override
    public double getFlyingTime(Aircraft aircraft){
        return (super.getLength() + aircraft.getArcLength() / aircraft.getSpeed()) * Constants.SEC_TO_HOURS;
    }

}
