package SaamAlgo.Model.Graph.Node;

import SaamAlgo.Model.Graph.Conflict;
import SaamAlgo.Model.Graph.IFlight;

public class NodeConflict extends Conflict {


    public NodeConflict(IFlight flight1, IFlight flight2, String name, double criticize) {
        super(flight1, flight2, name, criticize);
    }


    @Override
    public void setConflict() {
        flight1.getAircraft().addNodeConflict(this);
        flight2.getAircraft().addNodeConflict(this);
    }
}
