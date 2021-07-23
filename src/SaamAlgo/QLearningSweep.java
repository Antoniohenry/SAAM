package SaamAlgo;

import SaamAlgo.Operations.IOperations;
import SaamAlgo.Operations.IState;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QLearningSweep {

    public QLearningSweep(){

        List<Double> range = List.of(0.01, 0.1, 0.2, 0.3);
        int maxj = 10;

        int tot = maxj * range.size();

        List<Double> times = new ArrayList<>();

        try {
            FileWriter fileWriter = new FileWriter("result.txt");
            fileWriter.append("initialT " + 1E-1 + " finalT " + 1E-5 + " decreasing " + 0.993 + " iterations " + 35 + " threshold " + 0.6 + " epsilon " + "var" + " Qinit " + -52.5 + " alpha " + 0.2 + " gamma " + 0.93 + " \n");
            fileWriter.append("alpha reward \n");

            for(int j = 0; j < maxj; j++) {
                int i = 1;

                for (double alpha : range) {
                    long start = System.currentTimeMillis();
                    IState state = IOperations.preProcessing();
                    new QLearning(state, 1E-1, 1E-5, 0.993, 35, 0.6, 0.0, -52.5, alpha, 0.93);
                    double reward = (double) state.getTotalPerformance(state.getAgents()).get(0);
                    fileWriter.append(alpha + " " + reward + "\n");
                    fileWriter.flush();

                    times.add((double) (System.currentTimeMillis() - start) / 1000);

                    int currentIteration = j * range.size() + i;

                    double average = times.stream().mapToDouble(val -> val).average().orElse(0.0);
                    System.out.println(" remaining " + average * (tot - currentIteration) / 60 + " minutes");

                    System.out.println(" iterations " + currentIteration + " out of " + tot);

                    i++;
                }
            }
            fileWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Double> range(double start, double stop, double step){
        List<Double> range = new LinkedList<>();
        range.add(start);
        double val = start;
        while (val < stop){
            range.add(val+step);
            val += step;
        }
        return range;
    }

}
