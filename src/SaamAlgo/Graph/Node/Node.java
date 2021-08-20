package SaamAlgo.Graph.Node;

import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.IFlight;
import SaamAlgo.Graph.KeyError;
import SaamAlgo.Model.Aircraft;

import java.awt.Point;
import java.util.*;

public class Node extends Point {

    private final String name;
    private final List<Node> edges;
    private final TreeMap<java.lang.Double, IFlight> flyingAircrafts;

    public Node(double x, double y, String name) {
        super((int) x, (int) y);
        this.name = name;
        this.edges = new ArrayList<>();
        this.flyingAircrafts = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public List<Node> getEdges() {
        return edges;
    }

    public void addAircraft(Aircraft aircraft, double time){
        IFlight result;

        result = flyingAircrafts.put(time, new NodeFlight(time, aircraft));
        if(result == null) {
            computeConflictAndSetReward(time);
        }else{
            throw new KeyError("The key already exists in the treemap : " + result);
        }

    }

    public void removeAircraft(double time){
        IFlight result = flyingAircrafts.remove(time);
        if(result == null){
            throw new KeyError("Try to remove a key that doesn't exist :" + time);
        }
    }

    public TreeMap<java.lang.Double, IFlight> getFlyingAircrafts() {
        return flyingAircrafts;
    }

    public void computeConflictAndSetReward(double currentKey){
        IFlight currentAircraft = flyingAircrafts.get(currentKey);

        //On remonte la treelist car il peut y avoir plus de 2 avions en conflit
        java.lang.Double nextKey = flyingAircrafts.higherKey(currentKey);
        while (nextKey != null){
            IFlight nextAircraft = flyingAircrafts.get(nextKey);
            double criticize = currentAircraft.isInConflict(nextAircraft);
            if(criticize != 0){
                IConflict conflict = new NodeConflict(currentAircraft, nextAircraft, name, criticize);
                conflict.setConflict();
                nextKey = flyingAircrafts.higherKey(nextKey);
            }
            else {
                nextKey = null;
            }
        }

        //on redescent la treelist
        java.lang.Double previousKey = flyingAircrafts.lowerKey(currentKey);
        while (previousKey != null){
            IFlight previousAircraft = flyingAircrafts.get(previousKey);
            double criticize = previousAircraft.isInConflict(currentAircraft);
            if(criticize != 0){
                IConflict conflict = new NodeConflict(previousAircraft, currentAircraft, name, criticize);
                conflict.setConflict();
                previousKey = flyingAircrafts.lowerKey(previousKey);
            }
            else {
                previousKey = null;
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!super.equals(o)) return false;
        Node node = (Node) o;
        return getName().equals(node.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }

}
