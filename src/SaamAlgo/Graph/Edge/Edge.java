package SaamAlgo.Graph.Edge;

import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.IFlight;
import SaamAlgo.Graph.KeyError;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Operations.Constants;

import java.util.Optional;
import java.util.TreeMap;

public class Edge {

    private final Node entryNode;
    private final Node exitNode;
    private final String name;
    private final Double length;
    private final TreeMap<Double ,IFlight> flyingAircraftsIn;
    private final TreeMap<Double, IFlight> flyingAircraftsOut;


    public Edge(Node node1, Node node2, String name) {
        this.entryNode = node1;
        this.exitNode = node2;

        node1.getEdges().add(node2);
        node2.getEdges().add(node1);

        this.length = Math.sqrt( Math.pow(node1.x - node2.x, 2) + Math.pow(node1.y - node2.y, 2) );
        this.name = name;
        this.flyingAircraftsIn = new TreeMap<>();
        this.flyingAircraftsOut = new TreeMap<>();

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

    public TreeMap<Double, IFlight> getFlyingAircraftsIn() {
        return flyingAircraftsIn;
    }

    public TreeMap<Double, IFlight> getFlyingAircraftsOut() {
        return flyingAircraftsOut;
    }

    public void addFlyingAircraft(Aircraft aircraft, double entryTime){ //entryTime
        double exitTime = entryTime + (length / aircraft.getSpeed()) * Constants.HOURS_TO_SEC;
        IFlight flight = new EdgeFlight(entryTime, exitTime, aircraft);

        IFlight resultIn = flyingAircraftsIn.put(entryTime, flight);
        IFlight resultOut = flyingAircraftsOut.put(exitTime, flight);
        if(resultIn == null && resultOut == null) {
            computeConflictsAndSetReward(entryTime, exitTime);
        }else {
            System.out.println("resultOut = " + resultOut);
            System.out.println("resultIn = " + resultIn);
            throw new KeyError("The key to add already exists in one of the treemap");
        }
    }

    public void removeAircraft(double entryTime){
        IFlight flightIn = flyingAircraftsIn.remove(entryTime);

        if(flightIn == null){
            throw new KeyError("Try to remove a key that doesn't exist :" + entryTime);
        }
        else {
            double exitTime = flightIn.getExitTime(Optional.empty());
            IFlight flightOut = flyingAircraftsOut.remove(exitTime);
            if(flightOut == null) {
                throw new KeyError("Try to remove a key that doesn't exist :" + exitTime);
            }
        }

    }

    public void computeConflictsAndSetReward(double currentKeyIn, double currentKeyOut){
        computeConflictsAndSetRewardForward(currentKeyIn, currentKeyOut);
        computeConflictsAndSetRewardBackward(currentKeyIn, currentKeyOut);
    }

    public void computeConflictsAndSetRewardBackward(double currentKeyIn, double currentKeyOut) {
        Double previousKeyIn = flyingAircraftsIn.lowerKey(currentKeyIn);
        Double previousKeyOut = flyingAircraftsOut.lowerKey(currentKeyOut);

        IFlight currentFlight = flyingAircraftsIn.get(currentKeyIn);

        boolean previous = true;
        while (previous && (previousKeyIn != null) && (previousKeyOut != null)) {

            previous = continueInBothTree(previousKeyIn, previousKeyOut, currentFlight);

            previousKeyIn = flyingAircraftsIn.lowerKey(previousKeyIn);
            previousKeyOut = flyingAircraftsOut.lowerKey(previousKeyOut);
        }

        //if there is an overtaking and one of the key is null we have to compute conflicts on the other one
        descentInTreeMap(previousKeyIn, currentFlight, flyingAircraftsIn);
        descentInTreeMap(previousKeyOut, currentFlight, flyingAircraftsOut);

    }

    private boolean continueInBothTree(Double continueKeyIn, Double continueKeyOut, IFlight currentFlight) {

        boolean continue_ = false;

        IFlight continueFlightIn = flyingAircraftsIn.get(continueKeyIn);
        IFlight continueFlightOut = flyingAircraftsOut.get(continueKeyOut);

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


    public void computeConflictsAndSetRewardForward(double currentKeyIn, double currentKeyOut){
        Double nextKeyIn = flyingAircraftsIn.higherKey(currentKeyIn);
        Double nextKeyOut = flyingAircraftsOut.higherKey(currentKeyOut);

        IFlight currentFlight = flyingAircraftsIn.get(currentKeyIn);

        boolean next = true;
        while (next && (nextKeyIn !=null) && (nextKeyOut != null)){

            next = continueInBothTree(nextKeyIn, nextKeyOut, currentFlight);

            nextKeyIn = flyingAircraftsIn.higherKey(nextKeyIn);
            nextKeyOut = flyingAircraftsOut.higherKey(nextKeyOut);
        }

        //if there is an overtaking and one of the key is null we have to compute conflicts on the other one
        climbInTreeMap(nextKeyIn, currentFlight, flyingAircraftsIn);
        climbInTreeMap(nextKeyOut, currentFlight, flyingAircraftsOut);

    }

    private void climbInTreeMap(Double nextKey, IFlight currentFlight, TreeMap<Double, IFlight> flyingAircrafts) {
        while (nextKey != null){
            IFlight nextFlight = flyingAircrafts.get(nextKey);
            double criticize = currentFlight.isInConflict(nextFlight);
            if(criticize != 0){
                IConflict conflict = new EdgeConflict(currentFlight, nextFlight, name, criticize);
                conflict.setConflict();
                nextKey = flyingAircrafts.higherKey(nextKey);
            }
            else {
                break;
            }
        }
    }

    private void descentInTreeMap(Double previousKey, IFlight currentFlight, TreeMap<Double, IFlight> flyingAircrafts) {
        while (previousKey != null){
            IFlight previousFlight = flyingAircrafts.get(previousKey);
            double criticize = currentFlight.isInConflict(previousFlight);
            if(criticize != 0){
                IConflict conflict = new EdgeConflict(currentFlight, previousFlight, name, criticize);
                conflict.setConflict();
                previousKey = flyingAircrafts.lowerKey(previousKey);
            }
            else {
                break;
            }
        }
    }


    @Override
    public String toString() {
        return "Edge{" +
                "name='" + name + '\'' +
                //", flyingAircraft=" + flyingAircraft +
                '}';
    }
}
