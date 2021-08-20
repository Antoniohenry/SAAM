package SaamAlgo.Optimisation.DiscreetGA;

import SaamAlgo.Optimisation.QL;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.*;

public class DiscreetGA {

    static String filePath = "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\DiscreetGA\\DiscreetGA51";

    private final Random generator = new Random();

    private final double[][] parameters = {
            {3000, 2000, 1000},
            {1},
            {0.992, 0.993},
            {70, 80},
            {0.7},
            {-180, -160, -140, -120},
            {0.18, 0.2, 0.22},
            {0.84, 0.86, 0.88},
            {0.0, 0.1, 0.2},
            {1},
            {60}};

    TreeMap<Double, int[]> population = new TreeMap<>();


    public DiscreetGA() {

        init();

        for(int it = 0; it < 4; it++) {

            List<int[]> newPop = mutation(cross(selection(25)), 0.2);
            for (int[] para : newPop) {
                population.put(function(para), para);
            }
        }


    }

    private void init() {

        int iteration = 1;
        for (double[] doubles : parameters) {
            iteration *= doubles.length;
        }

        iteration = iteration / 10;

        for (int j = 0; j < iteration; j++){

            int[] parameter = new int[11];

            for(int k = 0; k < parameters.length; k++){
                parameter[k] = generator.nextInt(parameters[k].length);
            }

            double reward = function(parameter);
            population.put(reward, parameter);

        }

    }


    public double function(int[] parameter){

        SummaryStatistics statistics = new SummaryStatistics();
        for(int j = 0; j < 1; j++){
            QL ql = new QL(parameters[0][parameter[0]], parameters[1][parameter[1]], parameters[2][parameter[2]], (int) (parameters[3][parameter[3]]), parameters[4][parameter[4]], (int) parameters[5][parameter[5]], parameters[6][parameter[6]], parameters[7][parameter[7]], parameters[8][parameter[8]], (int) parameters[9][parameter[9]], (int) parameters[10][parameter[10]], filePath);
            statistics.addValue(ql.totalReward);
        }

        return statistics.getMean();
    }

    private List<int []> selection(int size){
        List<int []> selected = new LinkedList<>();

        double currentKey = population.lastKey();

        for (int i = 0; i < size; i++){
            selected.add(population.get(currentKey));
            currentKey = population.lowerKey(currentKey);
            if(currentKey == population.firstKey()){
                break;
            }
        }

        return selected;

    }

    private List<int []> cross(List<int []> selected){
        List<int []> newOnes = new LinkedList<>();
        Collections.shuffle(selected);

        int  i;
        if(selected.size() % 2 != 0){
            i  =selected.size() - 1;
        }
        else {
            i = selected.size();
        }

        for (int j = 0; j < i; j += 2){
            int [] parameter = new int[11];
            for(int k = 0; k < parameters.length; k++){
                if(generator.nextBoolean()){
                    parameter[k] = selected.get(j)[k];
                }
                else {
                    parameter[k] = selected.get(j + 1)[k];
                }
            }

            newOnes.add(parameter);

        }

        newOnes.addAll(selected);

        return newOnes;

    }

    private List<int []> mutation(List<int []> pop, double proba){
        for(int [] para : pop){
            for(int i = 0; i < para.length; i++){
                if(generator.nextDouble() < proba){
                    para[i] = generator.nextInt(parameters[i].length);
                }
            }
        }

        return pop;
    }


}
