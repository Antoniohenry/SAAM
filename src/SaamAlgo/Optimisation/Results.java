package SaamAlgo.Optimisation;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Results {

    private List<Result> results;
    private final Random generator = new Random();

    public Results(String filePath, int size) {
        ArrayList<Result> results_ = Result.getMean(Result.getFromFile(filePath));

        results_.sort(Collections.reverseOrder());

        results = results_.subList(0, min(size, results_.size()));

    }

    public ArrayList<Number> getNewParameters(boolean bool){
    ArrayList<Number> para = new ArrayList<>();

        SummaryStatistics initialTemperature = new SummaryStatistics();
        SummaryStatistics finalTemperature = new SummaryStatistics();
        SummaryStatistics decreasing = new SummaryStatistics();
        SummaryStatistics iteration = new SummaryStatistics();
        SummaryStatistics threshold = new SummaryStatistics();
        SummaryStatistics qInit = new SummaryStatistics();
        SummaryStatistics alpha = new SummaryStatistics();
        SummaryStatistics gamma = new SummaryStatistics();
        SummaryStatistics linear = new SummaryStatistics();
        SummaryStatistics rta = new SummaryStatistics();
        SummaryStatistics conflict = new SummaryStatistics();

        for(Result result : results){
            initialTemperature.addValue(result.initialTemperature);
            finalTemperature.addValue(result.finalTemperature);
            decreasing.addValue(result.decreasing);
            iteration.addValue(result.iteration);
            threshold.addValue(result.threshold);
            qInit.addValue(result.qInit);
            alpha.addValue(result.alpha);
            gamma.addValue(result.gamma);
            linear.addValue(result.linear);
            rta.addValue(result.rta);
            conflict.addValue(result.conflict);
        }


        if(bool) {
            para.add(generator.nextGaussian() * initialTemperature.getStandardDeviation() + initialTemperature.getMean());
            para.add(Math.exp(generator.nextGaussian() * min(finalTemperature.getStandardDeviation(), 3) + 0));
            para.add(min(1 - (Math.exp(generator.nextGaussian() * decreasing.getStandardDeviation() + 0) - 1), 0.995));
            para.add(generator.nextGaussian() * iteration.getStandardDeviation()  + iteration.getMean());
            para.add(generator.nextGaussian() * threshold.getStandardDeviation() + threshold.getMean());
            para.add(generator.nextGaussian() * qInit.getStandardDeviation() + qInit.getMean());
            para.add(max(generator.nextGaussian() * alpha.getStandardDeviation() + alpha.getMean(), 0.99));
            para.add(generator.nextGaussian() * gamma.getStandardDeviation() + gamma.getMean());
            para.add(generator.nextGaussian() * linear.getStandardDeviation() + linear.getMean());
            para.add(max(generator.nextGaussian() * rta.getStandardDeviation() + rta.getMean(), 1));
            para.add(max(generator.nextGaussian() * conflict.getStandardDeviation() + conflict.getMean(), 35));
        }

         else {

            para.add(nextRandom(50., 2000.));
            para.add(nextRandom(1E-2, 50));
            para.add(nextRandom(0.95, 0.993));
            para.add(nextRandom(25, 60));
            para.add(nextRandom(0.6, 0.8));
            para.add(nextRandom(-150, -20));
            para.add(nextRandom(0., 1.));
            para.add(nextRandom(0., 2.));
            para.add(nextRandom(0.1, 0.4));
            para.add(nextRandom(1, 4));
            para.add(nextRandom(40, 80));
        }

         /*

        para.add( generator.nextDouble() * 300 + 350);
        para.add( Math.exp(generator.nextDouble() *  5E-1 + 0 ) - 1);
        para.add( 1 - (Math.exp(generator.nextDouble() *  5E-1 + 0 ) -1));
        para.add( Math.round(generator.nextDouble() * 20 + 40));
        para.add( generator.nextDouble() * 0.1 + 0.7);
        para.add( Math.round(generator.nextDouble() * 30 + -70));
        para.add( generator.nextDouble() * 0.1 + 0.16);
        para.add( generator.nextDouble() * 0.1 + 0.9);
        para.add( generator.nextDouble() * 0.4 + 0.5);
        para.add( Math.round(generator.nextDouble() * 4 + 5));
        para.add( Math.round(generator.nextDouble() * 30 + 70));

          */

        return para;

    }

    public double nextRandom(double low, double high){
        return generator.nextDouble() * (high - low) + low;
    }

    public int nextRandom(int low, int high){
        return (int) (generator.nextDouble() * (high - low) + low);
    }

    public ArrayList<ArrayList<Number>> mutation(){
        ArrayList<ArrayList<Number>> newSet = new ArrayList<>();

        int size = results.size();

        for(int j =0; j< size; j++){
            ArrayList<Number> parameters = new ArrayList<>();

            parameters.add(results.get(generator.nextInt(size)).initialTemperature);
            parameters.add(results.get(generator.nextInt(size)).finalTemperature);
            parameters.add(results.get(generator.nextInt(size)).decreasing);
            parameters.add(results.get(generator.nextInt(size)).iteration);
            parameters.add(results.get(generator.nextInt(size)).threshold);
            parameters.add( results.get(generator.nextInt(size)).qInit);
            parameters.add( results.get(generator.nextInt(size)).alpha);
            parameters.add(results.get(generator.nextInt(size)).gamma);
            parameters.add(results.get(generator.nextInt(size)).linear);
            parameters.add( results.get(generator.nextInt(size)).rta);
            parameters.add( results.get(generator.nextInt(size)).conflict);

            newSet.add(parameters);

        }

        for (Result result : results){
            ArrayList<Number> parameters = new ArrayList<>();

            parameters.add(result.initialTemperature);
            parameters.add(result.finalTemperature);
            parameters.add(result.decreasing);
            parameters.add(result.iteration);
            parameters.add(result.threshold);
            parameters.add(result.qInit);
            parameters.add(result.alpha);
            parameters.add(result.gamma);
            parameters.add(result.linear);
            parameters.add(result.rta);
            parameters.add(result.conflict);

            System.out.println("parameters = " + parameters);
            if(generator.nextDouble() < 1){
                List<Number> random = getNewParameters(false);
                int indice = generator.nextInt(10);
                System.out.println("indice = " + indice);
                parameters.set(indice, random.get(indice));
            }
            System.out.println("parameters = " + parameters);

            newSet.add(parameters);

        }

        return newSet;

    }

}
