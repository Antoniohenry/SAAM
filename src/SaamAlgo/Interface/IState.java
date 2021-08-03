package SaamAlgo.Interface;

import SaamAlgo.Model.FlightSet;

import java.util.List;

public interface IState {

    /** This should
     * - load a graph
     * - load a flight set
     * @return ISate : in this case a flightSet = aircraft with neutral decision on a graph
     */
    static IState preProcessing() {
        return new FlightSet();
    }


    List<IAgent> getAgentsToHandled(double threshold, List<? extends IAgent> aircrafts);

    List<? extends IAgent> getAircraftInSW(double start, double end);

    List<Number> getTotalPerformance(List<? extends IAgent> aircrafts);

    String getPerformanceString(List<? extends IAgent> aircrafts);

    List<? extends IAgent> getAgents();

    void decisionToDoc(String pathName);

    void toDoc(String pathname);

}
