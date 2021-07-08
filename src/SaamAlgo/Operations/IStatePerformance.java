package SaamAlgo.Operations;

import SaamAlgo.Model.Aircraft;

import java.util.List;

public interface IStatePerformance {

    double getReward();

   String getSWPerformanceString(double start, double end);

   List<Integer> getSWPerformance(double start, double stop);

    String toString();

}
