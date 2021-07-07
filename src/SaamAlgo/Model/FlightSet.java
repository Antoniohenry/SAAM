package SaamAlgo.Model;

import SaamAlgo.Graph.Graph;
import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Operations.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

        this.performance = new FlightSetPerformance(this);

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
                entryTime = entryTime + Constants.offsetTimeStartingTheSimulation + 0.1  * random.nextDouble();
                int entryNodeNumber = Integer.parseInt(tokenizer.nextToken());
                double speedIn = Double.parseDouble(tokenizer.nextToken());
                double realLandingTime = Double.parseDouble(tokenizer.nextToken()) + Constants.offsetTimeStartingTheSimulation;

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

                if(entryNode.getName().equals("OKIPA") || entryNode.getName().equals("LORNI")){
                    aircrafts.add(new Aircraft(graph, callsign, (int) speedIn, entryTime, realLandingTime, category, entryNode, runway));
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }



    public List<IAgent> getAgentsToHandled(double threshold, double SWStartingTime, double SWEndingTime){ // in %

        List<Aircraft> aircraftInSW = getAircraftInSW(SWStartingTime, SWEndingTime);
        if(aircraftInSW.size() == 0){
            return new LinkedList<>();
        }
        worstReward = performance.getWorstReward(aircraftInSW);
        aircraftInSW.sort(comparator.reversed());
        LinkedList<IAgent> criticalFlightSet = new LinkedList<>();
        for (Aircraft aircraft : aircraftInSW){
            if(aircraft.getReward() >= threshold * worstReward){
                criticalFlightSet.add(aircraft);
            }
        }
        return criticalFlightSet;
    }

    private List<Aircraft> getAircraftInSW(double start, double end){
        return aircrafts.stream().filter(aircraft -> aircraft.getStatus(start, end) == SlidingWindowParameters.status.ACTIVE).collect(Collectors.toList());
    }

    @Override
    public IStatePerformance stateEvaluation() {
        performance = new FlightSetPerformance(this);
        return performance;
    }


    @Override
    public String toString() {
        return "FlightSet{" +
                "aircrafts=" + aircrafts +
                ", worstReward=" + worstReward +
                '}';
    }
}
