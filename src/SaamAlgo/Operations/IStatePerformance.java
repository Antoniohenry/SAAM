package SaamAlgo.Operations;

import SaamAlgo.Model.Aircraft;

import java.util.List;

public interface IStatePerformance {

    double getReward();

   String getSWPerformance(double start, double end);

    String toString();

}
