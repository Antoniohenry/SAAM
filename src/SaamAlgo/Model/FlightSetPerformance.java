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
    private final double averageDelayOnRunway;
    private final double reward;


    public FlightSetPerformance(FlightSet flightSet) {
        this.flightSet = flightSet;

        this.nodeConflicts = getNodeConflictNumber(flightSet.getAircrafts());
        this.linkConflicts = getEdgeConflictNumber(flightSet.getAircrafts());
        this.averageDelayOnRunway = getAverageDelayInMin(flightSet.getAircrafts());
        this.reward = getFlightSetReward(flightSet.getAircrafts());
    }


    public int getEdgeConflictNumber(List<Aircraft> aircrafts){
        int nb = 0;
        for(IAgent aircraft : aircrafts){
            nb += aircraft.getEdgeConflictNumber();
        }
        return nb / 2; //chaque conflit est compte sur deux avions
    }

    public double getAverageDelayInMin(List<Aircraft> aircrafts){
        if(aircrafts.size() == 0){
            return 0;
        }
        double delay = 0;
        for(Aircraft aircraft : aircrafts){
            delay += Math.abs(aircraft.getRta() - aircraft.getTimeOnRunway());
        }
        return delay / (60 * aircrafts.size());
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

    public double getAverageDelayOnRunway() {
        return averageDelayOnRunway;
    }

    public double getReward() {
        return reward;
    }

    public List<Integer> getSWPerformance(double start, double end){
        List<IAgent> agents = flightSet.getAgentsToHandled(0, start, end);

        List<Aircraft> aircrafts = new ArrayList<>();

        for (IAgent agent : agents){
            if(agent instanceof Aircraft){
                aircrafts.add( (Aircraft) agent);
            }
        }
        return List.of(getNodeConflictNumber(aircrafts), getEdgeConflictNumber(aircrafts));

    }

    public String getSWPerformanceString(double start, double end){
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
                " AverageDelay : " +
                getAverageDelayInMin(aircrafts) +
                " reward : " +
                getFlightSetReward(aircrafts);


    }

    @Override
    public String toString() {
        return "FlightSetPerformance{" +
                "nodeConflicts=" + nodeConflicts +
                ", linkConflicts=" + linkConflicts +
                ", delayOnRunway=" + averageDelayOnRunway +
                ", reward=" + reward +
                '}';
    }
}
