package SaamAlgo.Model;

import SaamAlgo.Graph.Graph;
import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Operations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FlightSet implements IState, IOperations {

    private final ArrayList<Aircraft> aircrafts;
    private final Graph graph;
    private double worstReward;
    private FlightSetPerformance performance;

    Comparator<Aircraft> comparator = Comparator.comparing(Aircraft::getReward);

    public FlightSet() {
        this.graph = new Graph();
        this.aircrafts = new ArrayList<>();

        getFlightSetFromFile("DATA/20170711_26L_ARRIVEES.flights");
        getFlightSetFromFile("DATA/20170711_27R_ARRIVEES.flights");

        worstReward = getWorstReward();

    }

    private void getFlightSetFromFile(String filename){
        Random random = new Random(123);
        String line;

        System.out.println("Loading flights...");
        try {
            FileReader flightsFile = new FileReader(filename);
            BufferedReader flightsFileInput = new BufferedReader(flightsFile);

            while ((line = flightsFileInput.readLine()) != null){
                StringTokenizer tokenizer = new StringTokenizer(line);
                String ICAO = tokenizer.nextToken();
                String callsign = tokenizer.nextToken();// AF0315 etc...
                String aircraftType = tokenizer.nextToken(); // B737 A330 etc...
                int vortexCat = Integer.parseInt(tokenizer.nextToken());// 0-Small 1-Medium 2-Large
                tokenizer.nextToken();// pour visualisation xtraj ??
                String gate = tokenizer.nextToken();
                int rwy = Integer.parseInt(tokenizer.nextToken());
                double entryTime = Double.parseDouble(tokenizer.nextToken()); //in seconds
                // adding an hour to each aircraft in order to be sure that no one have a negative entry time
                // adding random seconds to avoid having the same key in treemaps
                entryTime = entryTime + 3600 + 0.1  * random.nextDouble();
                int entryNodeNumber = Integer.parseInt(tokenizer.nextToken());
                double speedIn = Double.parseDouble(tokenizer.nextToken());
                double realLandingTime = Double.parseDouble(tokenizer.nextToken());

                Node entryNode = graph.getEntryNode(entryNodeNumber);

                Aircraft.vortexCat category;
                switch (vortexCat) {
                    case 0:
                        category = Aircraft.vortexCat.LIGHT;
                        break;
                    case 1:
                        category = Aircraft.vortexCat.MEDIUM;
                        break;
                    case 2:
                        category = Aircraft.vortexCat.HEAVY;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value for wake vortex category " + vortexCat);
                }

                Node runway;
                switch (rwy){
                    case 1 : runway = graph.getRunway("RWY_27R"); break;
                    case 2 : runway = graph.getRunway("RWY_26L"); break;
                    default:
                        throw new IllegalStateException("Unexpected value for runway: " + rwy);
                }

                aircrafts.add(new Aircraft(graph, callsign, (int) speedIn, entryTime, category, entryNode, runway));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

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
        aircrafts.sort(comparator.reversed());
        LinkedList<IAgent> criticalFlightSet = new LinkedList<>();
        for (Aircraft aircraft : aircrafts){
            if(aircraft.getReward() >= threshold * worstReward){
                criticalFlightSet.add(aircraft);
            }
        }
        return criticalFlightSet;
    }

    @Override
    public IStatePerformance stateEvaluation() {
        return new FlightSetPerformance(this);
    }

    @Override
    public String toString() {
        return "FlightSet{" +
                "aircrafts=" + aircrafts +
                ", worstReward=" + worstReward +
                '}';
    }
}
