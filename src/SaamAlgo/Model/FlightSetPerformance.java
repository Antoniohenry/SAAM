package SaamAlgo.Model;

import SaamAlgo.Operations.IAgent;
import SaamAlgo.Operations.IStatePerformance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FlightSetPerformance implements IStatePerformance {

    Comparator<Aircraft> comparator = Comparator.comparing(Aircraft::getReward);

    private FlightSet flightSet;

    private final int nodeConflicts;
    private final int linkConflicts;
    private final double delayOnRunway;
    private final double reward;


    public FlightSetPerformance(FlightSet flightSet) {
        this.flightSet = flightSet;

        this.nodeConflicts = getNodeConflictNumber(flightSet.getAircrafts());
        this.linkConflicts = getEdgeConflictNumber(flightSet.getAircrafts());
        this.delayOnRunway = getTotalDelay(flightSet.getAircrafts());
        this.reward = getFlightSetReward(flightSet.getAircrafts());
    }


    public int getEdgeConflictNumber(List<Aircraft> aircrafts){
        int nb = 0;
        for(IAgent aircraft : aircrafts){
            nb += aircraft.getEdgeConflictNumber();
        }
        return nb / 2; //chaque conflit est compte sur deux avions
    }

    public double getTotalDelay(List<Aircraft> aircrafts){
        double delay = 0;
        for(Aircraft aircraft : aircrafts){
            delay += Math.abs(aircraft.getRta() - aircraft.getTimeOnRunway());
        }
        return delay;
    }

    public int getNodeConflictNumber(List<Aircraft> aircrafts){
        int nb = 0;
        for(IAgent aircraft : aircrafts){
            nb += aircraft.getNodeConflictNumber();
        }
        return nb / 2; //chaque conflit est compte sur deux avions
    }

    public double getFlightSetReward(List<Aircraft> aircrafts){
        double reward = 0.;
        for(Aircraft aircraft : aircrafts){
            reward += aircraft.getReward();
        }
        return reward;
    }

    public double getWorstReward(List<Aircraft> aircrafts){
        aircrafts.sort(comparator.reversed());
        return aircrafts.get(0).getReward();
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

    public String getSWPerformance(double start, double end){
        List<IAgent> agents = flightSet.getAgentsToHandled(0, start, end);

        List<Aircraft> aircrafts = new ArrayList<>();

        for (IAgent agent : agents){
            if(agent instanceof Aircraft){
                aircrafts.add( (Aircraft) agent);
            }
        }

        return String.valueOf(
                " Number of aircrafts : " +
                        aircrafts.size() +
                " Node Conflict : " +
                        getNodeConflictNumber(aircrafts)) +
                " Edge Conflict : " +
                getEdgeConflictNumber(aircrafts) +
                " Delay : " +
                getTotalDelay(aircrafts) +
                " reward : " +
                getFlightSetReward(aircrafts);


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
