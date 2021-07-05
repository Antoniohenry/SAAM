package SaamAlgo.Graph.Node;

import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.IFlight;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Operations.Constants;

import java.util.Optional;

public class NodeFlight implements IFlight {
    private final double time;
    private final Aircraft aircraft;

    public NodeFlight(double time, Aircraft aircraft) {
        this.time = time;
        if(time < 0){
            throw new Error("Negative entryTime, need an offset on the beginning of simulation ");
        }

        this.aircraft = aircraft;
    }


    public double isInConflict(IFlight other) {

        boolean overtaking = false;
        double deltaTime;

        double distance;

        double radius;
        double separation;

        if (getEntryTime(Optional.of(3.)) < other.getEntryTime(Optional.of(3.))) {
            //other arrive in second on the node
            separation = Constants.TABLE_SEPARATION[aircraft.getVortexCat()][other.getAircraft().getVortexCat()];
            radius = separation / Math.sqrt(2);

            deltaTime = getExitTime(Optional.of(radius)) - other.getEntryTime(Optional.of(radius));

            if (deltaTime < 0) {
                return 0;
            }

            if (getExitTime(Optional.of(radius)) > other.getExitTime(Optional.of(radius))) {
                overtaking = true;
            }

        } else {
            //other arrives in first in the node
            separation = Constants.TABLE_SEPARATION[other.getAircraft().getVortexCat()][aircraft.getVortexCat()];
            radius = separation / Math.sqrt(2);

            deltaTime = other.getExitTime(Optional.of(radius)) - getEntryTime(Optional.of(radius));

            if (deltaTime < 0) {
                return 0;
            }

            if (other.getExitTime(Optional.of(radius)) > getExitTime(Optional.of(radius))) {
                overtaking = true;
            }
        }

        distance = deltaTime * Constants.SEC_TO_HOURS * Constants.nominalApproachSpeed;

        double criticize = 0;
        if (distance < separation) {
            criticize = distance / separation;
        }

        if (criticize < 0 || criticize > 1) {
            throw new Error("Criticize not between 0 and 1 : " + criticize);
        }

        if (overtaking) {
            criticize += Constants.overtakingReward;
        }

        return criticize;
    }


    @Override
    public void addConflict(IFlight other, IConflict conflict) {
        aircraft.addNodeConflict(conflict);
        other.getAircraft().addEdgeConflict(conflict);
    }

    public double getEntryTime(Optional<Double> radius) {
        if(radius.isPresent()) {
            return time - (radius.get() / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        }
        else {
            throw new Error("trying to get a node entryTime without a radius node");
        }
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public double getExitTime(Optional<Double> radius) {
        if(radius.isPresent()){
            return time + (radius.get() / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        }
        else {
            throw new Error("trying to get a node entryTime without a radius node");
        }
    }

    @Override
    public String toString() {
        return "Flight{" +
                "time" + time +
                ", aircraft=" + aircraft.getId() +
                '}';
    }
}
