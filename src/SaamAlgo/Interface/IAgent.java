package SaamAlgo.Interface;

import SaamAlgo.Model.QTable;


public interface IAgent {

    void setDecision(IDecision decision);

    String getId();

    void removeDecision();

    double setAndGetReward();

    double getReward();

    int getEdgeConflictNumber();

    int getNodeConflictNumber();

    double getDelayInMin();

    void setReward();

    IDecision getDecision();

    default QTable getQ(){return null;}

    default String getPrint(){return this.toString();}

}
