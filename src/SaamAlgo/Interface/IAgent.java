package SaamAlgo.Interface;

import SaamAlgo.Model.IQTable;


public interface IAgent {

    /**
     * @param decision the new decision to be apply to this agent
     */
    void setDecision(IDecision decision);

    /**
     * @return the callsign
     */
    String getId();

    /**
     * Put a neutral decision instead
     */
    void removeDecision();

    double setAndGetReward();

    double getReward();

    int getEdgeConflictNumber();

    int getNodeConflictNumber();

    double getDelayInMin();

    void setReward();

    IDecision getDecision();

    default IQTable getQ() {
        throw new Error("not implemented yet");
    }

    /**
     * @return a string representing the agent
     */
    default String getPrint(){return this.toString();}

}
