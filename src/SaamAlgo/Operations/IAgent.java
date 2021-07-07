package SaamAlgo.Operations;

public interface IAgent {

    void setDecision(IDecision decision);

    void removeDecision();

    double getReward();

    int getEdgeConflictNumber();

    int getNodeConflictNumber();

    void setReward();

    IDecision getDecision();

}
