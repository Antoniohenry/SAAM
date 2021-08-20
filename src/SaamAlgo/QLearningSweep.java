package SaamAlgo;

import SaamAlgo.Interface.IState;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QLearningSweep {

    public QLearningSweep(){

        List<Double> range = List.of(0.7);
        int maxj = 7;

        int tot = maxj * range.size();

        List<Double> times = new ArrayList<>();

        try {
            FileWriter fileWriter = new FileWriter("result.txt");
            //fileWriter.append("finalL " + 1E-6 + "decreasing " + 0.999 + " iteration " + 40 + " gamma " + 0.9 + " epsilon " + "var");
            fileWriter.append("initialT " + 550 + " finalT " + 2e-3 + " decreasing " + 0.995 + " iterations " + 45 + " threshold " + "var" + " epsilon " + "var" + " Qinit " + -60 + " alpha " + 0.16 + " gamma " + 0.93 + " \n");
            fileWriter.append("threshold reward \n");

            for(int j = 0; j < maxj; j++) {
                int i = 1;

                for (double parameter : range) {
                    long start = System.currentTimeMillis();
                    IState state = IState.preProcessing();
                    new QLearning(state, 550, 2E-3, 0.995, 45, parameter, 0.0, -60, 0.16, 0.93);
                    //new QL(state, 1E-6, 0.999, 40, 0.7, -50, 0.5, epsilon);
                    double reward = (double) state.getTotalPerformance(state.getAgents()).get(0);
                    fileWriter.append(parameter + " " + reward + "\n");
                    fileWriter.flush();

                    times.add((double) (System.currentTimeMillis() - start) / 1000);

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
