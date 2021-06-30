package SaamAlgo.Model;

import SaamAlgo.Graph.Graph;
import SaamAlgo.Graph.Node.Node;
import SaamAlgo.Operations.Constants;
import SaamAlgo.Operations.IAgent;
import SaamAlgo.Operations.IDecision;

import java.util.Random;

public class Decision implements IDecision {

    private final int deltaV; // au noeud
    private final int delatTIn; // a la minute
    private final Node runway;
    private final double deltaT; // variable continue
    private Aircraft aircraft;
    private static final Random generator = new Random();


    public Decision(int delatV, int deltaTIn, Node runway, double deltaT){
        this.delatTIn = deltaTIn;
        this.deltaV = delatV;
        this.deltaT = deltaT;
        this.runway = runway;
        this.aircraft = null;
    }

    public int getDeltaV() {
        return deltaV;
    }

    public int getDelatTIn() {
        return delatTIn;
    }

    public Node getRunway() {
        return runway;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public IAgent getAgent() {
        return aircraft;
    }

    @Override
    public IDecision getNeighbour() {
        return null;
    }

    @Override
    public IDecision getNeighbour(double temperature) {
        return Decision.getAleatDecision();
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static Decision getAleatDecision(){
        int deltaSpeed = Constants.deltaVMin + generator.nextInt(Constants.deltaVMax - Constants.deltaVMin);
        int deltaTIn = Constants.deltaTInMin + generator.nextInt(Constants.deltaTInMax - Constants.deltaTInMin);
        double timeInArc = Constants.maxTimeInArc * Math.random();
        Node runway;
        if(Math.random() < 0.5){
            runway = Graph.lRunway;
        }
        else {
            runway = Graph.rRunway;
        }
        return new Decision(deltaSpeed, deltaTIn, runway, timeInArc);
    }


}