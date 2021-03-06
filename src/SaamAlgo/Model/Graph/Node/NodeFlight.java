package SaamAlgo.Model.Graph.Node;

import SaamAlgo.Model.Graph.IConflict;
import SaamAlgo.Model.Graph.IFlight;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Constants;


public class NodeFlight implements IFlight, Cloneable {
    private final double entryTime;
    private final Aircraft aircraft;
    private final double exitTime;

    public NodeFlight(double time, Aircraft aircraft) {
        super();
        this.entryTime = time - (Constants.nodeRadius /aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        if(entryTime < 0){
            throw new Error("Negative entryTime, need an offset on the beginning of simulation ");
        }
        this.aircraft = aircraft;
        this.exitTime = time + (Constants.nodeRadius / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
    }

    @Override
    public double isInConflict(IFlight other) {

        boolean overtaking = false;
        double deltaTime;
        double separation = Constants.nodeRadius * 2;
        double distance;

        if (entryTime < other.getEntryTime()) {
            //other arrive in second on the node
            if (aircraft.getSpeed() > other.getAircraft().getSpeed()) {
                //other is the slowest
                deltaTime = other.getEntryTime() - entryTime;

            } else {
                //other is the fastest
                if (other.getExitTime() < exitTime) {
                    overtaking = true;
                    deltaTime = exitTime - other.getExitTime();
                } else {
                    deltaTime = other.getExitTime() - exitTime;
                }

            }
            distance = deltaTime * Constants.SEC_TO_HOURS * aircraft.getSpeed();
        } else {
            //other arrive in first
            if (other.getAircraft().getSpeed() > aircraft.getSpeed()) {
                //other is the fastest
                deltaTime = entryTime - other.getEntryTime();
            } else {
                //other is the slowest
                if (other.getExitTime() > exitTime) {
                    overtaking = true;
                    deltaTime = other.getExitTime() - exitTime;
                } else {
                    deltaTime = exitTime - other.getExitTime();
                }

            }
            distance = deltaTime * Constants.SEC_TO_HOURS * other.getAircraft().getSpeed();
        }

        double criticize = 0;
        if(distance < separation) {
            criticize = (separation - distance) / separation;

            if (criticize > 1 || criticize < 0) {
                System.out.println("criticize = " + criticize + "Not between 0 and 1");
                System.out.println("other = " + other);
                System.out.println("this = " + this);

                throw new Error("Criticize not between 0 and 1");
            }
        }

        if (overtaking){
            criticize = 1;
        }

        return criticize;
    }


    @Override
    public void addConflict(IFlight other, IConflict conflict) {
        aircraft.addNodeConflict(conflict);
        other.getAircraft().addEdgeConflict(conflict);
    }

    public double getEntryTime() {
        return entryTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public double getExitTime() {
        return exitTime;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "time" + (entryTime + exitTime) / 2 +
                ", aircraft=" + aircraft.getId() +
                '}';
    }

}
