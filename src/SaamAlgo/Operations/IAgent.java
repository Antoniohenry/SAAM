package SaamAlgo.Operations;

import SaamAlgo.Model.QTable;


public interface IAgent {

    void setDecision(IDecision decision);

    String getId();

    void removeDecision();

    double getReward();

    int getEdgeConflictNumber();

    int getNodeConflictNumber();

    double getDelayInMin();

    void setReward();

    IDecision getDecision();

    QTable getQ();

}
