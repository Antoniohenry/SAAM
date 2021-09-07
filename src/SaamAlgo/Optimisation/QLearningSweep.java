package SaamAlgo.Optimisation;

import SaamAlgo.Interface.IState;
import SaamAlgo.QL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QLearningSweep {

    /**
     * Call the constructor to create a new sweep on one of the QL parameters
     */
    public QLearningSweep(){

        List<Double> range = List.of(0.6, 0.7, 0.8); // List of value for the sweep
        int maxj = 2; // how many times the QL has to be run on a particular value

        int tot = maxj * range.size(); // number of total iterations

        List<Double> times = new ArrayList<>(); // Store every computational time

        try {
            FileWriter fileWriter = new FileWriter("result.txt");
            //fileWriter.append("finalL " + 1E-6 + "decreasing " + 0.999 + " iteration " + 40 + " gamma " + 0.9 + " epsilon " + "var");
            fileWriter.append("initialT " + 550 + " finalT " + 1 + " decreasing " + 0.99 + " iterations " + 45 + " threshold " + "var"  + " Qinit " + -100 + " alpha " + 0.2 + " gamma " + 0.9 + " \n");
            fileWriter.append("threshold reward \n");

            for(int j = 0; j < maxj; j++) {
                int i = 1;

                for (double parameter : range) {
                    long start = System.currentTimeMillis();
                    QL ql = new QL(550., 1, 0.99, 45, parameter, -100, 0.2, 0.9, 0.7, 1, 60, "Result QL");
                    double reward = ql.totalReward;
                    fileWriter.append(parameter + " " + reward + "\n");
                    fileWriter.flush();

                    times.add((double) (System.currentTimeMillis() - start) / 1000); // computational time in seconds

                    int currentIteration = j * range.size() + i;

                    double average = times.stream().mapToDouble(val -> val).average().orElse(0.0);
                    System.out.println("remaining " + average * (tot - currentIteration) / 60 + " minutes");

                    System.out.println("  iterations " + currentIteration + " out of " + tot);

                    i++;
                }
            }
            fileWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
