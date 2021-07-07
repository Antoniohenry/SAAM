package SaamAlgo.Model;

import SaamAlgo.Operations.Constants;
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
    private double speed;
    private Route route;
    private double timeIn; // in seconds
    private final double rta; // in seconds
    private int vortexCat;
    private final Node preferredRunway;
    private double timeInArc; //in secs
    private Decision decision;
    private final Graph graph;
    private final Node entry;
    private double reward;

    private final List<IConflict> nodeConflicts;
    private final List<IConflict> edgeConflicts;

    private final SlidingWindowParameters SWParameters;

    public Aircraft(Graph graph, String id, int speed, double timeIn, double rta, vortexCat vortexCat, Node entry, Node runway, Decision decision) {
        nodeConflicts = new LinkedList<>();
        edgeConflicts = new LinkedList<>();

        this.graph = graph;
        this.entry = entry;

        switch (vortexCat){
            case HEAVY: this.vortexCat = 2;break;
            case MEDIUM: this.vortexCat = 1;break;
            case LIGHT: this.vortexCat = 0;break;
        }

        this.id = id;
        this.decision = decision;
        this.speed = speed;
        this.timeIn = timeIn;

        this.preferredRunway =runway;
        this.route = graph.getRoute(entry, runway);

        this.rta = rta; // in seconds

        this.SWParameters = SlidingWindowParameters.getInstance(this);

        setNewDecision(decision);
    }

    public Aircraft(Graph graph, String id, int speed, double timeIn, double rta, vortexCat vortexCat, Node entry, Node runway){
        this(graph, id, speed, timeIn, rta, vortexCat, entry, runway, Decision.getNeutralDecision(vortexCat));
    }


    public void removeDecision(){
        removeAircraftFromGraph();

        speed = Constants.nominalApproachSpeed;
        timeIn -= decision.getDeltaTIn(); // in seconds
        route = graph.getRoute(entry, preferredRunway);
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

        speed = newDecision.getSpeed();
        timeIn += newDecision.getDeltaTIn(); // in seconds
        route = graph.getRoute(entry, graph.getADifferentRunway(preferredRunway));
        timeInArc = newDecision.getTimeInMP();

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

    public Route getRoute() {
        return route;
    }

    public double getSpeed() {
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

        double rtaReward = Math.abs(rta - getTimeOnRunway()) / 60 * Constants.rtaReward; // in mins

        if(decision.isRunwayChanged()){
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
        return timeIn + route.getFlyingTime(getSpeed(), getLandingSpeed()); //in seconds
    }

    public double getInitialTimeInTMA(){
        return timeIn - decision.getDeltaTIn();
    }

    public double getLandingSpeed(){
        double speed;
        switch (vortexCat){
            case 2 : speed = Constants.nominalLandingSpeedH; break;
            case 1 : speed = Constants.nominalLandingSpeedM; break;
            default:
                throw new IllegalStateException("Unexpected value for wake vortex cat: " + vortexCat);
        }
        return speed;
    }

    public double getMinimalApproachSpeed(){
        double speed;
        switch (vortexCat){
            case 2 : speed = Constants.minimalApproachSpeedH; break;
            case 1 : speed = Constants.minimalApproachSpeedM; break;
            default:
                throw new IllegalStateException("Unexpected value for wake vortex cat: " + vortexCat);
        }
        return speed;
    }

    public SlidingWindowParameters.status getStatus(double start, double end) {
        return SWParameters.getStatus(start, end);
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

        double rtaReward = Math.abs(rta - getTimeOnRunway() ) / 60 * Constants.rtaReward; //in mins
        result.append("; rta ").append(df.format(rtaReward));
        reward +=rtaReward;

        if(decision.isRunwayChanged()){
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
                ", routes=" + route +
                '}';
    }
}
