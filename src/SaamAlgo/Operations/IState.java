package SaamAlgo.Operations;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.FlightSet;

import java.io.Serializable;
import java.util.List;

public interface IState extends Serializable {


    List<IAgent> getAgentsToHandled(double threshold, List<? extends IAgent> aircrafts);

    List<? extends IAgent> getAircraftInSW(double start, double end);

    List<Number> getTotalPerformance(List<? extends IAgent> aircrafts);

    String getPerformanceString(List<? extends IAgent> aircrafts);

    List<? extends IAgent> getAgents();

    void toDoc(String pathName);

}
