package SaamAlgo.Model;

import SaamAlgo.Operations.IStatePerformance;

public class FlightSetPerformance implements IStatePerformance {

    private final int nodeConflicts;
    private final int linkConflicts;
    private final double delayOnRunway;
    private final double reward;

    public FlightSetPerformance(FlightSet flightSet) {
        this.nodeConflicts = flightSet.getNodeConflictNumber();
        this.linkConflicts = flightSet.getEdgeConflictNumber();
        this.delayOnRunway = flightSet.getTotalDelay();
        this.reward = flightSet.getFlightSetReward();
    }

    public int getNodeConflicts() {
        return nodeConflicts;
    }

    public int getLinkConflicts() {
        return linkConflicts;
    }

    public double getDelayOnRunway() {
        return delayOnRunway;
    }

    public double getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return "FlightSetPerformance{" +
                "nodeConflicts=" + nodeConflicts +
                ", linkConflicts=" + linkConflicts +
                ", delayOnRunway=" + delayOnRunway +
                ", reward=" + reward +
                '}';
    }
}
