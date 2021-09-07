package SaamAlgo.Optimisation.GA;

import SaamAlgo.QL;
import SaamAlgo.Optimisation.Results;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class GA {

    int populationSize;

    DecimalFormat df = new DecimalFormat("#.####");

    static String filePath = "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\GA\\GA";

    public GA(int populationSize) {
        this.populationSize = populationSize;

        int gaIteration = 1;
        int mean = 3;
        int tot = mean * gaIteration * populationSize * 2;
        List<Double> times = new ArrayList<>();

        int count = 0;

        for(int it = 0; it< gaIteration; it++){

            Results population = new Results(filePath, populationSize);

            for(ArrayList<Number> para : population.mutation()){

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

                for(int m = 0; m < mean ; m++) {

                    count++;
                    System.out.println("   iterations " + count + " out of " + tot);

                    QL ql = new QL(initialTemperature, finalTemperature, decreasing, iteration, threshold, qInit, alpha, gamma, linear, rta, conflict, filePath);

                    System.out.println("time = " + ql.duration);
                    times.add(ql.duration);
                    double average = times.stream().mapToDouble(val -> val).average().orElse(0.0);
                    System.out.println("remaining " + average * (tot - count) / 60 + " minutes");
                }

            }

        }

    }

    public static void init(int populationSize){
        Results results = new Results(filePath, populationSize);

        List<Double> times = new ArrayList<>();

        for(int i = 1; i <= populationSize; i++) {

            ArrayList<Number> para = results.getNewParameters(false);

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

            System.out.println("   iterations " + i + " out of " + populationSize);

            QL ql = new QL(initialTemperature, finalTemperature, decreasing, iteration, threshold, qInit, alpha, gamma, linear, rta, conflict, filePath);

            System.out.println("time = " + ql.duration);
            times.add(ql.duration);
            double average = times.stream().mapToDouble(val -> val).average().orElse(0.0);
            System.out.println("remaining " + average * (populationSize - i) / 60 + " minutes");

        }

    }

}
