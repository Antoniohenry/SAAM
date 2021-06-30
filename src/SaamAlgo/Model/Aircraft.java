package SaamAlgo.Model;

import SaamAlgo.Operations.Constants;
import SaamAlgo.Graph.Edge.Edge;
import SaamAlgo.Graph.Graph;
import SaamAlgo.Graph.IConflict;
import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Graph.Route;
import SaamAlgo.Operations.IAgent;
import SaamAlgo.Operations.IDecision;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

//TODO action listener between aircraft and conflict

public class Aircraft implements IAgent {

    public enum vortexCat{HEAVY, MEDIUM, LIGHT}

    private final String id;
    private int speed;
    private Route route;
    private double timeIn; // in hours
    private final double rta; // in hours
    private int vortexCat;
    private final Node preferedRumway;
    private double timeInArc;
    private Decision decision;
    private final Graph graph;
    private final Node entry;
    private double reward;

    private final List<IConflict> nodeConflicts;
    private final List<IConflict> edgeConflicts;

    public Aircraft(Graph graph, String id, int speed, double timeIn, vortexCat vortexCat, Node entry, Node runway, Decision decision) {
        nodeConflicts = new LinkedList<>();
        edgeConflicts = new LinkedList<>();

        this.graph = graph;
        this.entry = entry;

        this.id = id;
        this.decision = decision;
        this.speed = speed;
        this.timeIn = timeIn;

        this.preferedRumway =runway;
        this.route = graph.getRoute(entry, runway);

        this.rta = timeIn + route.getFlyingTime(speed);

        switch (vortexCat){
            case HEAVY: this.vortexCat = 2;break;
            case MEDIUM: this.vortexCat = 1;break;
            case LIGHT: this.vortexCat = 0;break;
        }

        setNewDecision(decision);
    }

    public Aircraft(Graph graph, String id, int speed, double timeIn, vortexCat vortexCat, Node entry, Node runway){
        this(graph, id, speed, timeIn, vortexCat, entry, runway, new Decision(3, 0, runway, Constants.standardTimeInArc));
    }

    public void removeDecision(){
        removeAircraftFromGraph();

        speed -= decision.getDeltaV();
        timeIn -= (decision.getDelatTIn() / 60.); // to convert mins to hours
        route = graph.getRoute(entry, preferedRumway);
        timeInArc = Constants.standardTimeInArc;

        this.decision = null;

    }

    public void removeAircraftFromGraph(){

        graph.removeAircraft(this);

        for (IConflict nodeConflict : nodeConflicts){
            nodeConflict.disableConflict();
        }
        for (IConflict edgeConflict : edgeConflicts){
            edgeConflict.disableConflict();
        }

        deleteConflicts();
    }

    private void deleteConflicts(){
        nodeConflicts.removeIf(conflict -> conflict.getReward() == 0);
        edgeConflicts.removeIf(conflict -> conflict.getReward() == 0);
    }

    private void setNewDecision(IDecision decision){
        Decision newDecision = (Decision) decision;
        newDecision.setAircraft(this);

        speed += newDecision.getDeltaV();
        timeIn += (newDecision.getDelatTIn() / 60.); // to convert mins to hours
        route = graph.getRoute(entry, newDecision.getRunway());
        timeInArc = newDecision.getDeltaT();

        this.decision = newDecision;
        this.graph.addAircraft(this);
    }

    public void setDecision(IDecision decision){
        removeDecision();
        setNewDecision(decision);
    }

    public void changeDecision(Decision decision){
        removeAircraftFromGraph();
        setDecision(decision);
    }

    public List<Edge> getRoute() {
        return route.getRoute();
    }

    public int getSpeed() {
        return speed;
    }

    public double getTimeIn() {
        return timeIn;
    }

    public void setReward(){
        deleteConflicts();
        double reward = 0;
        for(IConflict nodeConflict : nodeConflicts){
            reward += nodeConflict.getReward();
        }

        for(IConflict edgeConflict : edgeConflicts){
            reward += edgeConflict.getReward();
        }

        double rtaReward = Math.abs(rta - getTimeOnRunway()) * 60 * Constants.rtaReward;

        if(!decision.getRunway().equals(preferedRumway) ){
            reward += Constants.runwayReward;
        }

        this.reward = reward + rtaReward;

    }

    public double getReward() {
        this.setReward();
        return this.reward;
    }

    public String getId() {
        return id;
    }

    public Node getEntry() {
        return entry;
    }

    public void addNodeConflict(IConflict nodeConflict) {
        nodeConflicts.add(nodeConflict);
    }

    public void removeNodeConflict(IConflict nodeConflict){
        nodeConflicts.remove(nodeConflict);
    }

    public void addEdgeConflict(IConflict edgeConflict){
        edgeConflicts.add(edgeConflict);
    }

    public void removeEdgeConflict(IConflict edgeConflict){
        edgeConflicts.remove(edgeConflict);
    }

    public double getRta() {
        return rta;
    }

    public double getTimeInArc() {
        return timeInArc;
    }

    public IDecision getDecision() {
        return decision;
    }

    public int getVortexCat() {
        return vortexCat;
    }

    public List<IConflict> getNodeConflicts() {
        deleteConflicts();
        return nodeConflicts;
    }

    public List<IConflict> getEdgeConflicts() {
        deleteConflicts();
        return edgeConflicts;
    }

    public int getEdgeConflictNumber(){
        deleteConflicts();
        return edgeConflicts.size();
    }

    public int getNodeConflictNumber(){
        deleteConflicts();
        return nodeConflicts.size();
    }

    public double getTimeOnRunway(){
        return timeIn + route.getFlyingTime(speed);
    }

    public String printReward(){
        deleteConflicts();
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder result = new StringBuilder();
        result.append("aircraft : ").append(this.id);
        double reward = 0;
        for(IConflict nodeConflict : nodeConflicts){
            result.append("; Node ").append(nodeConflict.getName()).append(df.format(nodeConflict.getReward()));
            reward += nodeConflict.getReward();
        }

        for(IConflict edgeConflict : edgeConflicts){
            result.append("; Edge ").append(edgeConflict.getName()).append(df.format(edgeConflict.getReward()));
            reward += edgeConflict.getReward();
        }

        double rtaReward = Math.abs(rta - getTimeOnRunway() ) * 60 * Constants.rtaReward;
        result.append("; rta ").append(df.format(rtaReward));
        reward +=rtaReward;

        if(!decision.getRunway().equals(preferedRumway) ){
            result.append("; not preferredRunway ").append(Constants.runwayReward);
            reward += Constants.runwayReward;
        }

        result.append("; TotalReward ").append(df.format(reward));
        return result.toString();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aircraft)) return false;
        Aircraft aircraft = (Aircraft) o;
        return getId().equals(aircraft.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "id='" + id + '\'' +
                ", speed=" + speed +
                ", timeIn=" + timeIn +
                ", rta=" + rta +
                ", reward=" + getReward() +
                ", vortexCat=" + vortexCat +
                ", timeInArc=" + timeInArc +
                '}';
    }
}
