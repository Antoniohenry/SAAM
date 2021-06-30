package SaamAlgo.Operations;

public interface IDecision {

    static IDecision getAleatDecision(){
        return null;
    }

    IAgent getAgent();

    IDecision getNeighbour();

    IDecision getNeighbour(double temperature);

}
