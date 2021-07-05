package SaamAlgo.Graph.Edge;

import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.IFlight;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Operations.Constants;

import java.util.Optional;

public class EdgeFlight implements IFlight {

    private final Double entryTime;
    private final Double exitTime;
    private final Aircraft aircraft;

    public EdgeFlight(Double entryTime, Double exitTime, Aircraft aircraft) {
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.aircraft = aircraft;
    }

    public double isInConflict(IFlight other) {

        boolean overtaking = false;
        double deltaTime;
        double separation;
        double distance;

        if (entryTime < other.getEntryTime(Optional.empty())) {
            //other arrive in second on the edge
            separation = Constants.TABLE_SEPARATION[aircraft.getVortexCat()][other.getAircraft().getVortexCat()];
            if (aircraft.getSpeed() > other.getAircraft().getSpeed()) {
                //other is the slowest
                deltaTime = other.getEntryTime(Optional.empty()) - entryTime;

            } else {
                //other is the fastest
                if (other.getExitTime(Optional.empty()) < exitTime) {
                    overtaking = true;
                    deltaTime = exitTime - other.getExitTime(Optional.empty());
                } else {
                    deltaTime = other.getExitTime(Optional.empty()) - exitTime;
                }

            }
            distance = deltaTime * Constants.SEC_TO_HOURS  * aircraft.getSpeed();
        } else {
            //other arrive in first
            separation = Constants.TABLE_SEPARATION[other.getAircraft().getVortexCat()][aircraft.getVortexCat()];
            if (other.getAircraft().getSpeed() > aircraft.getSpeed()) {
                //other is the fastest
                deltaTime = entryTime - other.getEntryTime(Optional.empty());
            } else {
                //other is the slowest
                if (other.getExitTime(Optional.empty()) > exitTime) {
                    overtaking = true;
                    deltaTime = other.getExitTime(Optional.empty()) - exitTime;
                } else {
                    deltaTime = exitTime - other.getExitTime(Optional.empty());
                }

            }
            distance = deltaTime * Constants.SEC_TO_HOURS * other.getAircraft().getSpeed();
        }

        double criticize = 0;
        if(distance < separation) {
            criticize = distance / separation;
        }

        if(criticize < 0 || criticize > 1){
            throw new Error("Criticize not between 0 and 1");
        }

        if (overtaking){
            criticize += Constants.overtakingReward;
        }

        return criticize;
    }

    @Override
    public void addConflict(IFlight other, IConflict conflict) {
        aircraft.addEdgeConflict(conflict);
        other.getAircraft().addEdgeConflict(conflict);
    }


    @Override
    public String toString() {
        return "Flight{" +
                "aircraftId=" + aircraft.getId() +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                '}';
    }

    public double getEntryTime(Optional<Double> radius) {
        return entryTime;
    }

    public double getExitTime(Optional<Double> radius) {
        return exitTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

}
