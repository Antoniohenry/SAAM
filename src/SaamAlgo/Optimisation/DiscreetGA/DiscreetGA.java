package SaamAlgo.Optimisation.DiscreetGA;

import SaamAlgo.QL;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.*;

public class DiscreetGA {

    // file to write the result
    static String filePath = "src\\\\SaamAlgo\\\\Optimisation\\\\DiscreetGA\\\\DiscreetGA600";


    private final Random generator = new Random();

    //initial set of parameters
    private final double[][] parameters = {
            {2500, 3500, 4500, 5500}, //initial temperature
            {1}, //final temperature
            {0.2}, // decreasing law parameter
            {80}, // number of iterations
            {0.7}, // critical flight set threshold
            {-160, -140, -120, -100}, // Q0, the initial value int he Q table
            {0.15, 0.18, 0.21, 0.24}, // alpha
            {0.84, 0.86, 0.88}, // gamma
            {0.3}, // the reward offset in case of conflict
            {1}, // RTA Reward
            {60}}; // conflict reward

    // reward, arrays containing the index of all parameters to be use
    TreeMap<Double, int[]> population = new TreeMap<>();


    /**
     * To be call to run a new discreet genetic algorithm
     */
    public DiscreetGA(int initialPopulationSize, int nbOfGenerations, int selectionSize, double mutationProbability) {

        init(initialPopulationSize);

        for(int it = 0; it < nbOfGenerations; it++) {

            List<int[]> newPop = mutation(cross(selection(selectionSize)), mutationProbability);
            for (int[] para : newPop) {
                population.put(function(para), para);
            }
        }

    }

    /** Generate a random population
     * @param size the size of the initial population
     */
    private void init(int size) {

        for (int j = 0; j < size; j++){

            int[] parameter = new int[11];

            for(int k = 0; k < parameters.length; k++){
                parameter[k] = generator.nextInt(parameters[k].length);
            }

            double reward = function(parameter);
            population.put(reward, parameter);

        }

    }


    /** Evaluation of an individual
     * @param parameters index of parameters representing the individual
     * @return the value of the fitness for this individual
     */
    public double function(int[] parameters){

        SummaryStatistics statistics = new SummaryStatistics();
        for(int j = 0; j < 1; j++){
            QL ql = new QL(this.parameters[0][parameters[0]], this.parameters[1][parameters[1]], this.parameters[2][parameters[2]], (int) (this.parameters[3][parameters[3]]), this.parameters[4][parameters[4]], (int) this.parameters[5][parameters[5]], this.parameters[6][parameters[6]], this.parameters[7][parameters[7]], this.parameters[8][parameters[8]], (int) this.parameters[9][parameters[9]], (int) this.parameters[10][parameters[10]], filePath);
            statistics.addValue(ql.totalReward);
        }

        return statistics.getMean() - statistics.getStandardDeviation() ;
    }

    /**
     * @param size the number of individual to select
     * @return the list of individuals
     */
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

    /**
     * @param selected the initial population to cross
     * @return a new crossed population, two times smaller than the initial one
     */
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
            int [] parameter = new int[11]; //child
            for(int k = 0; k < parameters.length; k++){
                if(generator.nextBoolean()){
                    parameter[k] = selected.get(j)[k]; // from first parent
                }
                else {
                    parameter[k] = selected.get(j + 1)[k]; // from second parent
                }
            }

            newOnes.add(parameter);

        }
        return newOnes;

    }

    /**
     * @param pop the population to be mutate
     * @param proba the probability for an individual to mutate
     * @return the new mutated population
     */
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
