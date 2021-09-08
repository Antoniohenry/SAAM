package SaamAlgo.Optimisation.SA;

import SaamAlgo.QL;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SA {

    static String filePath = "src\\\\SaamAlgo\\\\Optimisation\\\\SA\\\\SA";

    private final Random generator = new Random();

    private final double[][] parameters = { {2000, 1500, 1000},
            {10, 1, 1E-1},
            {0.99, 0.991, 0.992},
            {25, 30., 40., 50.},
            {0.7},
            {-100, -90, -80, -70, -60},
            {0.12, 0.14, 0.16, 0.18, 0.2},
            {0.9, 0.92, 0.94, 0.96},
            {0.3},
            {1},
            {60}};

    //point = [2, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0]
    //point = [1, 1, 1, 0, 0, 1, 1, 3, 0, 0, 0]
    private int[] initialGuess = {2, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0};

    public SA(double initialTemperature, double finalTemperature, double decreasing) {

        double t = initialTemperature;

        int[] point = initialGuess;
        double reward = function(point);

        while (t > finalTemperature){

            int[] newPoint = getNeighbour(point);
            double newReward = function(newPoint);

            if(accept(reward, newReward, t)){
                point = newPoint;
                reward = newReward;
            }

            t *= decreasing;

        }

        System.out.println("reward = " + reward);
        System.out.println("point = " + Arrays.toString(point));

    }


    public int[] getNeighbour(int[] indices){
        int[] newIndices = indices.clone();

        // choose the parameter to be slightly changed
        int i = generator.nextInt(indices.length);
        while (parameters[i].length == 0) {
            i = generator.nextInt(indices.length);
        }

        int actual = indices[i];

        //get the new value
        int newIndice;
        if(generator.nextBoolean()){
            newIndice = actual + 1;
        }
        else {
            newIndice = actual - 1;
        }

        //be sure that the indice is between the boundaries
        newIndice = max(0, newIndice);
        newIndice = min(newIndice, parameters[i].length -1);

        newIndices[i] = newIndice;

        // a neighbour has to be different from the initial individual
        if(newIndices == indices){
            return getNeighbour(indices);
        }

        return newIndices;
    }


    /**
     * @param parameter the individual to evaluate
     * @return the value of the objective function
     */
    public double function(int[] parameter){
        SummaryStatistics statistics = new SummaryStatistics();
        for(int j = 0; j < 5; j++){
            QL ql = new QL(parameters[0][parameter[0]], parameters[1][parameter[1]], parameters[2][parameter[2]], (int) (parameters[3][parameter[3]]), parameters[4][parameter[4]], (int) parameters[5][parameter[5]], parameters[6][parameter[6]], parameters[7][parameter[7]], parameters[8][parameter[8]], (int) parameters[9][parameter[9]], (int) parameters[10][parameter[10]], filePath);
            statistics.addValue(ql.totalReward);
        }

        return statistics.getMean() - statistics.getStandardDeviation();
    }

    public boolean accept(double oldReward, double newReward, double temperature) {
        return newReward > oldReward || new Random().nextDouble() < Math.exp((newReward - oldReward) / temperature);

    }

}
