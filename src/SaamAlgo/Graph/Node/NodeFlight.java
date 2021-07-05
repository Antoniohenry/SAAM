package SaamAlgo.Graph.Node;

import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.IFlight;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Operations.Constants;

import java.util.Optional;

public class NodeFlight implements IFlight {
    private final double time;
    //private final double entryTime;
    private final Aircraft aircraft;
    //private final double exitTime;

    public NodeFlight(double time, Aircraft aircraft) {
        this.time = time;
        if(time < 0){
            throw new Error("Negative entryTime, need an offset on the beginning of simulation ");
        }
        /*this.entryTime = time - (Constants.nodeRadius /aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        if(entryTime < 0){
            throw new Error("Negative entryTime, need an offset on the beginning of simulation ");
        }*/
        this.aircraft = aircraft;
        //this.exitTime = time + (Constants.nodeRadius / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
    }


    public double isInConflict(IFlight other) {

        boolean overtaking = false;
        double deltaTime;

        double distance;

        double radius;
        double separation;


        if (getEntryTime(Optional.of(3.)) < other.getEntryTime(Optional.of(3.))) {
            System.out.println("getEntryTime(0) = " + getEntryTime(Optional.of(0.)));
            System.out.println("other.getEntryTime(0) = " + other.getEntryTime(Optional.of(0.)));
            //other arrive in second on the edge
            separation = Constants.TABLE_SEPARATION[aircraft.getVortexCat()][other.getAircraft().getVortexCat()];
            radius = separation / Math.sqrt(2);

            if (aircraft.getSpeed() > other.getAircraft().getSpeed()) {
                //other is the slowest
                deltaTime = other.getEntryTime(Optional.of(radius)) - getEntryTime(Optional.of(radius));

            } else {
                //other is the fastest
                if (other.getExitTime(Optional.of(radius)) < getExitTime(Optional.of(radius))) {
                    overtaking = true;
                    deltaTime = getExitTime(Optional.of(radius)) - other.getExitTime(Optional.of(radius));
                } else {
                    deltaTime = other.getExitTime(Optional.of(radius)) - getExitTime(Optional.of(radius));
                }

            }
            distance = deltaTime * Constants.SEC_TO_HOURS * aircraft.getSpeed();
        } else {
            //other arrive in first
            separation = Constants.TABLE_SEPARATION[other.getAircraft().getVortexCat()][aircraft.getVortexCat()];
            radius = separation / Math.sqrt(2);

            if (other.getAircraft().getSpeed() > aircraft.getSpeed()) {
                //other is the fastest
                deltaTime = getEntryTime(Optional.of(radius)) - other.getEntryTime(Optional.of(radius));
            } else {
                //other is the slowest
                if (other.getExitTime(Optional.of(radius)) > getExitTime(Optional.of(radius))) {
                    overtaking = true;
                    deltaTime = other.getExitTime(Optional.of(radius)) - getExitTime(Optional.of(radius));
                } else {
                    deltaTime = getExitTime(Optional.of(radius)) - other.getExitTime(Optional.of(radius));
                }

            }
            distance = deltaTime * Constants.SEC_TO_HOURS * other.getAircraft().getSpeed();
        }

        double criticize = 0;
        if(distance < separation) {
            criticize = distance / separation;

            if (criticize > 1 || criticize < 0) {
                System.out.println("criticize = " + criticize + "Not between 0 and 1");
                System.out.println("other = " + other);
                System.out.println("this = " + this);
                System.out.println("separation = " + separation);
                System.out.println("radius = " + radius);
                System.out.println("distance = " + distance);
                System.out.println("deltaTime = " + deltaTime);
                System.out.println("aircraft = " + aircraft);
                System.out.println("other = " + other.getAircraft());
                System.out.println("overtaking = " + overtaking);
                //TODO reproduire cette erreur
            }
        }

        if(criticize < 0 || criticize > 1){
            throw new Error("Criticize not between 0 and 1 : " + criticize);
        }

        if (overtaking){
            criticize += Constants.overtakingReward;
        }

        return criticize;


        /*double deltaTime;
        double distance;

        if(entryTime < other.getEntryTime()){
            deltaTime = other.getEntryTime() - exitTime;
            distance = Math.abs(deltaTime * aircraft.getSpeed());
        }
        else {
            deltaTime = entryTime - other.getExitTime();
            distance = Math.abs(deltaTime * other.getAircraft().getSpeed());
        }

        if (deltaTime < 0){
            if(distance / (Constants.nodeRadius * 2) > 1){
                throw new Error("criticize not between 0 and 1");
            }
            return distance / (Constants.nodeRadius * 2);
        }
        else {
            return 0;
        } */

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
