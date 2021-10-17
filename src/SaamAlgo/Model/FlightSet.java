package SaamAlgo.Model;

import SaamAlgo.Model.Graph.Graph;
import SaamAlgo.Model.Graph.IFlight;
import SaamAlgo.Model.Graph.Node.Node;
import SaamAlgo.Interface.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class FlightSet implements IState {

    private final ArrayList<Aircraft> aircraft;
    private final Graph graph;

    Comparator<IAgent> comparator = Comparator.comparing(IAgent::setAndGetReward);

    public FlightSet() {
        super();
        this.graph = new Graph();
        this.aircraft = new ArrayList<>();

        getFlightSetFromFile("src/DATA1/simulation.flights");

    }


    private void getFlightSetFromFile(String filename){
        Random random = new Random(123);
        String line;

        //System.out.println("Loading flights...");
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
                    aircraft.add(new Aircraft(graph, callsign, (int) speedIn, entryTime, realLandingTime, category, entryNode, runway));
                }

            }

            flightsFileInput.close();
            flightsFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<? extends IAgent> getAgents() {
        return aircraft;
    }

    public List<Number> getTotalPerformance(List<? extends IAgent> aircrafts){
        double reward = 0;
        int edgeConflict = 0;
        int nodeConflict = 0;
        double averageDelay = 0;
        double worstReward = 0;
        for(IAgent aircraft : aircrafts){
            edgeConflict += aircraft.getEdgeConflictNumber();
            nodeConflict += aircraft.getNodeConflictNumber();
            reward += aircraft.setAndGetReward();
            averageDelay += aircraft.getDelayInMin();
            if(aircraft.getReward() < worstReward){
                worstReward = aircraft.getReward();
            }

        }
        return List.of(reward, worstReward, averageDelay /  aircrafts.size(), nodeConflict / 2, edgeConflict / 2);
    }

    public String getPerformanceString(List<? extends IAgent> aircraft){
        DecimalFormat df = new DecimalFormat("#.####");
        List<Number> perf = getTotalPerformance(aircraft);

        return "TotalReward = " + df.format(perf.get(0)) +
                "  WorstReward = " + df.format(perf.get(1)) +
                "  AverageDelay = " + df.format(perf.get(2)) + " min " +
                "  NodeConflict = " + perf.get(3) +
                "  EdgeConflict = " + perf.get(4);

    }


    public List<IAgent> getAgentsToHandled(double threshold, List<? extends IAgent> aircraft){ // in %

        if(aircraft.size() == 0){
            return new LinkedList<>();
        }
        aircraft.sort(comparator);

        LinkedList<IAgent> criticalFlightSet = new LinkedList<>();

        if(threshold < 1){
            double worstSWReward = aircraft.get(0).getReward();
            for (IAgent agent : aircraft) {
                if ((agent.getReward() <= threshold * worstSWReward && agent.getReward() < 0) || agent.getEdgeConflictNumber() != 0 || agent.getNodeConflictNumber() != 0 ) {
                    criticalFlightSet.add(agent);
                }
            }
        }

        if(threshold >=1 && threshold < 2){

            double min = aircraft.get(0).getReward();
            for (IAgent agent : aircraft) {
                if (agent.getNodeConflictNumber() != 0 || agent.getEdgeConflictNumber() != 0) {
                    criticalFlightSet.add(agent);
                    min = agent.getReward();
                }
            }
            for (IAgent agent : aircraft) {
                if(agent.getReward() < (threshold - 1) * min) {
                    criticalFlightSet.add(agent);
                }
            }

        }

        if(threshold == 2) {
            Random rd = new Random();
            double worstReward = aircraft.get(0).getReward();
            for (IAgent agent : aircraft) {
                if (rd.nextDouble() < (agent.getReward() / worstReward)) {
                    criticalFlightSet.add(agent);
                }
            }
        }

        return criticalFlightSet;
    }

    public List<? extends IAgent> getAircraftInSW(double start, double end){
        return aircraft.stream().filter(aircraft -> aircraft.getStatus(start, end) == SlidingWindowParameters.status.ACTIVE).collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return getPerformanceString(aircraft);
    }

    public void decisionToDoc(String pathName){
        FileWriter file;
        try {
            file = new FileWriter(pathName);

            for(Aircraft aircraft : aircraft){
                file.append(aircraft.getDecision().toString()).append(String.valueOf(aircraft.setAndGetReward())).append(" \n");
            }

            file.append(getPerformanceString(aircraft));

            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void toDoc(String pathname){
        FileWriter file;

        try {
            file = new FileWriter(pathname);

            for (Node node : graph.getNodes().values()) {
                file.append("Node : ").append(node.getName()).append(String.valueOf('\n'));
                file.flush();
                TreeMap<Double, IFlight> treeMap = node.getFlyingAircraft();
                for (double time : treeMap.keySet()) {
                    Aircraft aircraft = treeMap.get(time).getAircraft();
                    file.append("Aircraft : ").append(String.valueOf(time)).append(" ").append(aircraft.getPrint());
                    file.flush();
                }
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

