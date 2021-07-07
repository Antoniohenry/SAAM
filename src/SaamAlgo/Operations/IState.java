package SaamAlgo.Operations;

import java.util.List;

public interface IState {

    /**
     * This set a neutral decision to each individual in the state
     */
     //IState getNeutralState();

    /** This evaluate the global performance of a state
     * @return IStatePerformance
     */
    IStatePerformance stateEvaluation();

    List<IAgent> getAgentsToHandled(double threshold, double SWStartingTime, double SWEndingTime);

}
