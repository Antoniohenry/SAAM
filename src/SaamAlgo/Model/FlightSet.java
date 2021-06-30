package SaamAlgo.Model;

import SaamAlgo.Graph.Graph;
import SaamAlgo.Operations.IAgent;
import SaamAlgo.Operations.IOperations;
import SaamAlgo.Operations.IState;
import SaamAlgo.Operations.IStatePerformance;

import java.util.*;

public class FlightSet implements IState, IOperations {

    private final ArrayList<Aircraft> aircrafts;
    private final Graph graph;
    private double worstReward;
    private FlightSetPerformance performance;

    Comparator<Aircraft> comparator = Comparator.comparing(Aircraft::getReward);

    public FlightSet() {

        Boolean test = true;

        this.graph = new Graph(test);
        this.aircrafts = new ArrayList<>();

        if(test){
            setUptest();
        }

        worstReward = getWorstReward();

    }

    public void setUptest(){
        Aircraft a1 = new Aircraft(graph, "1", 101, 1.01, Aircraft.vortexCat.LIGHT, graph.lEntry(), graph.lRunway());
        Aircraft a2 = new Aircraft(graph, "2", 100, 1.0, Aircraft.vortexCat.MEDIUM, graph.rEntry(), graph.rRunway());
        Aircraft a3 = new Aircraft(graph, "3", 100, 1.02, Aircraft.vortexCat.LIGHT, graph.lEntry(), graph.rRunway());

        aircrafts.add(a1);
        aircrafts.add(a2);
        aircrafts.add(a3);

    }

    private double getWorstReward(){
        aircrafts.sort(comparator.reversed());
        return aircrafts.get(0).getReward();
    }

    public ArrayList<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public int getEdgeConflictNumber(){
        int nb = 0;
        for(Aircraft aircraft : aircrafts){
            nb += aircraft.getEdgeConflictNumber();
        }
        return nb / 2; //chaque conflit est compte sur deux avions
    }

    public double getTotalDelay(){
        double delay = 0;
        for(Aircraft aircraft : aircrafts){
            delay += Math.abs(aircraft.getRta() - aircraft.getTimeOnRunway());
        }
        return delay;
    }

    public int getNodeConflictNumber(){
        int nb = 0;
        for(Aircraft aircraft : aircrafts){
            nb += aircraft.getNodeConflictNumber();
        }
        return nb / 2; //chaque conflit est compte sur deux avions
    }

    public double getFlightSetReward(){
        double reward = 0.;
        for(Aircraft aircraft : aircrafts){
            reward += aircraft.getReward();
        }
        return reward;
    }

    public List<IAgent> getWorstAgents(double threshold){ // in %
        worstReward = getWorstReward();
        System.out.println("worstReward = " + worstReward);
        aircrafts.sort(comparator.reversed());
        LinkedList<IAgent> criticalFlightSet = new LinkedList<>();
        for (Aircraft aircraft : aircrafts){
            if(aircraft.getReward() < threshold * worstReward){
                criticalFlightSet.add(aircraft);
            }
        }
        return criticalFlightSet;
    }

    @Override
    public IStatePerformance stateEvaluation() {
        return new FlightSetPerformance(this);
    }

}
