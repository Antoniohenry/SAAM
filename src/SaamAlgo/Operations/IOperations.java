package SaamAlgo.Operations;

import SaamAlgo.Model.FlightSet;

public interface IOperations {

    /** This should
     * - load a graph
     * - load a flight set
     * @return ISate : in this case a flightSet = aircraft with neutral decision on a graph
     */
    static IState preProcessing() {
        return new FlightSet();
    }


    /** This set a new decision
     * @param decision contain the individual to set the decision
     */
    static void putDecision(IAgent agent, IDecision decision){
        agent.setDecision(decision);
    }

    /** This remove a decision
     * @param agent to remove the decision
     */
    static void removeDecision(IAgent agent){
        agent.removeDecision();
    }


    /** Compute conflicts and set reward
     * @param agent to evaluate
     */
    static void evaluateDecision(IAgent agent){
        agent.setReward();
    }


    /**
     * @param decision the old decision
     * @return the new decision
     */
    static IDecision getNeighbour(IDecision decision){
        return decision.getNeighbour();
    }

}
