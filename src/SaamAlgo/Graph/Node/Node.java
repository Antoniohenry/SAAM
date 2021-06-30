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
    private final TreeMap<java.lang.Double, NodeFlight> flyingAircrafts;

    public Node(int x, int y, String name) {
        super(x, y);
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
            System.out.println("result = " + result);
            throw new KeyError("The key already exists in the treemap");
        }
    }

    public void removeAircraft(double time){
        IFlight result = flyingAircrafts.remove(time);
        if(result == null){
            throw new KeyError("Try to remove a key that doesn't exist :" + time);
        }
    }

    public TreeMap<java.lang.Double, NodeFlight> getFlyingAircrafts() {
        return flyingAircrafts;
    }

    public void computeConflictAndSetReward(double currentKey){
        //On remonte la treelist car il peut y avoir plus de 2 avions en conflit
        java.lang.Double nextKey = flyingAircrafts.higherKey(currentKey);
        while (nextKey != null){
            double criticize = flyingAircrafts.get(currentKey).isInConflict(flyingAircrafts.get(nextKey));
            if(criticize != 0){
                IConflict conflict = new NodeConflict(flyingAircrafts.get(currentKey), flyingAircrafts.get(nextKey), name, criticize);
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
            double criticize = flyingAircrafts.get(previousKey).isInConflict(flyingAircrafts.get(currentKey));
            if(criticize != 0){
                IConflict conflict = new NodeConflict(flyingAircrafts.get(previousKey), flyingAircrafts.get(currentKey), name, criticize);
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
