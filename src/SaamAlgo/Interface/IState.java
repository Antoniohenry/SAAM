package SaamAlgo.Interface;

import SaamAlgo.Model.FlightSet;

import java.util.List;

public interface IState {

    /** This
     * - load a graph
     * - load a flight set
     * @return ISate : in this case a flightSet = aircraft with neutral decision on a graph
     */
    static IState preProcessing() {
        return new FlightSet();
    }


    /**
     * @param threshold the threshold defining the critical flight set
     * @param aircraft aircraft in the initial flight set
     * @return aircraft in 'aircraft' above the threshold
     */
    List<IAgent> getAgentsToHandled(double threshold, List<? extends IAgent> aircraft);


    /**
     * @param start beginning of the SW in seconds
     * @param end the end of the SW in seconds
     * @return only active flights (those in the SW)
     */
    List<? extends IAgent> getAircraftInSW(double start, double end);


    /**
     * @param aircraft aircraft to evaluate
     * @return List.of(reward, worstReward, averageDelay, nodeConflict, edgeConflict)
     */
    List<Number> getTotalPerformance(List<? extends IAgent> aircraft);

    /**
     * @param aircraft the aircraft to evaluate
     * @return @see getTotalPerformance
     */
    String getPerformanceString(List<? extends IAgent> aircraft);


    /**
     * @return all agents in the flight set
     */
    List<? extends IAgent> getAgents();

    void decisionToDoc(String pathName);

    void toDoc(String pathname);

}
