package SaamAlgo.Model.Graph.Node;

import SaamAlgo.Model.Graph.IConflict;
import SaamAlgo.Model.Graph.IFlight;
import SaamAlgo.Model.Graph.KeyError;
import SaamAlgo.Model.Aircraft;

import java.awt.Point;
import java.util.*;

public class Node extends Point {

    private final String name;
    private final List<Node> edges;
    private final TreeMap<java.lang.Double, IFlight> flyingAircraft;

    public Node(double x, double y, String name) {
        super((int) x, (int) y);
        this.name = name;
        this.edges = new ArrayList<>();
        this.flyingAircraft = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public List<Node> getEdges() {
        return edges;
    }

    public void addAircraft(Aircraft aircraft, double time){
        IFlight result;

        result = flyingAircraft.put(time, new NodeFlight(time, aircraft));
        if(result == null) {
            computeConflictAndSetReward(time);
        }else{
            throw new KeyError("The key already exists in the treemap : " + result);
        }

    }

    public void removeAircraft(double time){
        IFlight result = flyingAircraft.remove(time);
        if(result == null){
            throw new KeyError("Try to remove a key that doesn't exist :" + time);
        }
    }

    public TreeMap<java.lang.Double, IFlight> getFlyingAircraft() {
        return flyingAircraft;
    }

    public void computeConflictAndSetReward(double currentKey){
        IFlight currentAircraft = flyingAircraft.get(currentKey);

        //On remonte la treemap car il peut y avoir plus de 2 avions en conflit
        java.lang.Double nextKey = flyingAircraft.higherKey(currentKey);
        while (nextKey != null){
            IFlight nextAircraft = flyingAircraft.get(nextKey);
            double criticize = currentAircraft.isInConflict(nextAircraft);
            if(criticize != 0){
                IConflict conflict = new NodeConflict(currentAircraft, nextAircraft, name, criticize);
                conflict.setConflict();
                nextKey = flyingAircraft.higherKey(nextKey);
            }
            else {
                nextKey = null;
            }
        }

        //on redescent la treemap
        java.lang.Double previousKey = flyingAircraft.lowerKey(currentKey);
        while (previousKey != null){
            IFlight previousAircraft = flyingAircraft.get(previousKey);
            double criticize = previousAircraft.isInConflict(currentAircraft);
            if(criticize != 0){
                IConflict conflict = new NodeConflict(previousAircraft, currentAircraft, name, criticize);
                conflict.setConflict();
                previousKey = flyingAircraft.lowerKey(previousKey);
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
