package SaamAlgo.Model.Graph.Edge;

import SaamAlgo.Model.Graph.Conflict;
import SaamAlgo.Model.Graph.IFlight;


public class EdgeConflict extends Conflict {


    public EdgeConflict(IFlight flight1, IFlight flight2, String name, double criticize) {
        super(flight1, flight2, name, criticize);
    }

    @Override
    public void setConflict() {
        flight1.getAircraft().addEdgeConflict(this);
        flight2.getAircraft().addEdgeConflict(this);
    }

}
