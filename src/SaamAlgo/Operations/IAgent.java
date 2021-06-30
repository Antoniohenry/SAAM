package SaamAlgo.Operations;

public interface IAgent {

    void setDecision(IDecision decision);

    void removeDecision();

    double getReward();

    void setReward();

    IDecision getDecision();

}
