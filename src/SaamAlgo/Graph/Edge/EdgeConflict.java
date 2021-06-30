package SaamAlgo.Graph.Edge;

import SaamAlgo.Graph.Conflict;
import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.IFlight;


public class EdgeConflict extends Conflict implements IConflict {


    public EdgeConflict(IFlight flight1, IFlight flight2, String name, double criticize) {
        super(flight1, flight2, name, criticize);
    }

    @Override
    public void setConflict() {
        flight1.getAircraft().addEdgeConflict(this);
        flight2.getAircraft().addEdgeConflict(this);
    }

}
