package SaamAlgo.Model.Graph.Edge;

import SaamAlgo.Model.Graph.IConflict;
import SaamAlgo.Model.Graph.IFlight;
import SaamAlgo.Model.Graph.KeyError;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Graph.Node.Node;
import SaamAlgo.Model.Constants;

import java.util.TreeMap;

public class Edge {

    private final Node entryNode;
    private final Node exitNode;
    private final String name;
    private final Double length;
    private final TreeMap<Double ,IFlight> flyingAircraftIn;
    private final TreeMap<Double, IFlight> flyingAircraftOut;


    public Edge(Node node1, Node node2, String name) {
        this.entryNode = node1;
        this.exitNode = node2;

        node1.getEdges().add(node2);
        node2.getEdges().add(node1);

        this.length = Math.sqrt( Math.pow(node1.x - node2.x, 2) + Math.pow(node1.y - node2.y, 2) );
        this.name = name;
        this.flyingAircraftIn = new TreeMap<>();
        this.flyingAircraftOut = new TreeMap<>();

    }

    public String getName() {
        return name;
    }

    public Node getEntryNode() {
        return entryNode;
    }

    public Node getExitNode() {
        return exitNode;
    }

    public double getLength() {
        return length;
    }

    public TreeMap<Double, IFlight> getFlyingAircraftIn() {
        return flyingAircraftIn;
    }

    public TreeMap<Double, IFlight> getFlyingAircraftOut() {
        return flyingAircraftOut;
    }

    public void addFlyingAircraft(Aircraft aircraft, double entryTime){ //entryTime
        double exitTime = entryTime + (length / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        IFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);

        IFlight resultIn = flyingAircraftIn.put(entryTime, flight);
        IFlight resultOut = flyingAircraftOut.put(exitTime, flight);
        if(resultIn == null && resultOut == null) {
            computeConflictsAndSetReward(entryTime, exitTime);
        }else {
            System.out.println("resultOut = " + resultOut);
            System.out.println("resultIn = " + resultIn);
            throw new KeyError("The key to add already exists in one of the treemap");
        }
    }

    public void removeAircraft(double entryTime){
        IFlight flightIn = flyingAircraftIn.remove(entryTime);

        if(flightIn == null){
            throw new KeyError("Try to remove a key that doesn't exist :" + entryTime);
        }
        else {
            double exitTime = flightIn.getExitTime();
            IFlight flightOut = flyingAircraftOut.remove(exitTime);
            if(flightOut == null) {
                throw new KeyError("Try to remove a key that doesn't exist :" + exitTime);
            }
        }

    }

    public void computeConflictsAndSetReward(double currentKeyIn, double currentKeyOut){
        computeConflictsAndSetRewardForward(currentKeyIn, currentKeyOut);
        computeConflictsAndSetRewardBackward(currentKeyIn, currentKeyOut);
    }

    public void computeConflictsAndSetRewardForward(double currentKeyIn, double currentKeyOut){
        Double nextKeyIn = flyingAircraftIn.higherKey(currentKeyIn);
        Double nextKeyOut = flyingAircraftOut.higherKey(currentKeyOut);

        IFlight currentFlight = flyingAircraftIn.get(currentKeyIn);

        boolean next = true;
        while (next && (nextKeyIn !=null) && (nextKeyOut != null)){

            next = continueInBothTree(nextKeyIn, nextKeyOut, currentFlight);

            nextKeyIn = flyingAircraftIn.higherKey(nextKeyIn);
            nextKeyOut = flyingAircraftOut.higherKey(nextKeyOut);
        }
    }

    public void computeConflictsAndSetRewardBackward(double currentKeyIn, double currentKeyOut) {
        Double previousKeyIn = flyingAircraftIn.lowerKey(currentKeyIn);
        Double previousKeyOut = flyingAircraftOut.lowerKey(currentKeyOut);

        IFlight currentFlight = flyingAircraftIn.get(currentKeyIn);

        boolean previous = true;
        while (previous && (previousKeyIn != null) && (previousKeyOut != null)) {

            previous = continueInBothTree(previousKeyIn, previousKeyOut, currentFlight);

            previousKeyIn = flyingAircraftIn.lowerKey(previousKeyIn);
            previousKeyOut = flyingAircraftOut.lowerKey(previousKeyOut);
        }

    }

    private boolean continueInBothTree(Double continueKeyIn, Double continueKeyOut, IFlight currentFlight) {

        boolean continue_ = false;

        IFlight continueFlightIn = flyingAircraftIn.get(continueKeyIn);
        IFlight continueFlightOut = flyingAircraftOut.get(continueKeyOut);

        double criticize = currentFlight.isInConflict(continueFlightIn);

        if (continueFlightIn.equals(continueFlightOut)) {
            //no overtaking
            if (criticize != 0) {
                IConflict conflict = new EdgeConflict(currentFlight, continueFlightIn, name, criticize);
                conflict.setConflict();
                continue_ = true;
            }
        } else {
            //if overtaking we have to compute conflict on the previousFlightIn and the previousFlightOut
            if (criticize != 0) {
                IConflict conflict = new EdgeConflict(currentFlight, continueFlightIn, name, criticize);
                conflict.setConflict();
                continue_ = true;
            }
            criticize = currentFlight.isInConflict(continueFlightOut);
            if (criticize != 0) {
                IConflict conflict = new EdgeConflict(currentFlight, continueFlightOut, name, criticize);
                conflict.setConflict();
                continue_ = true;
            }
        }
        return continue_;
    }


    @Override
    public String toString() {
        return "Edge{" +
                "name='" + name + '\'' +
                //", flyingAircraft=" + flyingAircraft +
                '}';
    }

}
