package SaamAlgo.Model;

import SaamAlgo.Interface.IDecision;

public interface IQTable {

    /**
     * @param oldIDecision
     * @param action
     * @return the new decision if the action is taken
     */
    IDecision getDecision(IDecision oldIDecision, int action);

    /**
     * @param oldIDecision the current state
     * @param action the action to do
     * @param reward the reward of the agent if the action is taken
     * @param alpha QL parameter
     * @param gamma QL parameter
     */
    void updateQ(IDecision oldIDecision, int action, double reward, double alpha, double gamma);

    /**
     * @param oldDecision_ the current state
     * @param epsilon probability to take a random action
     * @return the action chosen according to a greedy exploration
     */
    int getGreedy(IDecision oldDecision_, double epsilon);

    /**
     * @param oldDecision the current state
     * @param temperature temperature
     * @return the action chosen according to a Boltzmann exploration
     */
    int getBoltzmann(IDecision oldDecision, double temperature);
}
