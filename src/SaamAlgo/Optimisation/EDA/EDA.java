package SaamAlgo.Optimisation.EDA;

import SaamAlgo.Graph.Conflict;
import SaamAlgo.Interface.IState;
import SaamAlgo.Model.Constants;
import SaamAlgo.Model.QTable;
import SaamAlgo.Optimisation.Results;
import SaamAlgo.TextFileWriter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EDA {

    public EDA() {

        DecimalFormat df = new DecimalFormat("#.####");

        String filePath = "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\EDA\\EDA result2";

        int maxj = 3;
        int maxi = 3;
        int tot = maxi * maxj;
        List<Double> times = new ArrayList<>();

        int count = 0;
        for (int j = 0; j < maxj; j++) {

            Results results = new Results(filePath, 10);

            ArrayList<Number> para = results.getNewParameters(true);

            double initialTemperature = (double) para.get(0);
            double finalTemperature = (double) para.get(1);
            double decreasing = (double) para.get(2);
            int iteration = (int) para.get(3).intValue();
            double threshold = (double) para.get(4);
            int qInit = (int) para.get(5).intValue();
            double alpha = (double) para.get(6);
            double gamma = (double) para.get(7);
            double linear = (double) para.get(8);
            int rta = (int) para.get(9).intValue();
            int conflict = (int) para.get(10).intValue();

            StringBuilder str = new StringBuilder(initialTemperature + " " + finalTemperature + " " + decreasing +  " " + iteration + " " + threshold + " " + qInit + " " + alpha + " " + gamma + " " +linear + " " + rta + " " + conflict);
            System.out.println(str);

            Conflict.setLinear(linear);
            Constants.setRtaReward(rta);
            Constants.setConflictReward(conflict);
            QTable.setqInit(qInit);

            for(int i = 0; i < maxi ; i++) {

                count++;
                System.out.println("   iterations " + count + " out of " + tot);

                long start = System.currentTimeMillis();
                IState state = IState.preProcessing();
                new QLearningEDA(state, initialTemperature, finalTemperature, decreasing, iteration, threshold, alpha, gamma);

                List<Number> perf = state.getTotalPerformance(state.getAgents());

                //System.out.println(str1);
                double computationalTime = (System.currentTimeMillis() - start) / 1000.;
                StringBuilder str1 = new StringBuilder();
                str1.append(" ").append(df.format(perf.get(0))).append(" ").append(df.format(perf.get(1))).append(" ").append(df.format(perf.get(2))).append(" ").append(perf.get(3)).append(" ").append(perf.get(4)).append(" ").append(df.format(computationalTime));
                //System.out.println(str1);
                String toto;
                toto = str.toString();
                TextFileWriter.append(filePath, toto + str1);

                System.out.println("time = " + computationalTime);
                times.add(computationalTime);
                double average = times.stream().mapToDouble(val -> val).average().orElse(0.0);
                System.out.println("remaining " + average * (tot - count) / 60 + " minutes");
            }

        }

    }

}
