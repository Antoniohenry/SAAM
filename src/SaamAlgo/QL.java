package SaamAlgo;

import SaamAlgo.Interface.*;
import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Constants;
import SaamAlgo.Model.IQTable;

import SaamAlgo.Util.MovingAverage;
import SaamAlgo.Util.TextFileWriter;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;

public class QL {

    public double duration; // computational time
    public double totalReward;

    /**
     * By calling the constructor, a new QL will be runned with the given parameter, and the result will be store in the file 'filePath'
     * @param initialTemperature
     * @param finalTemperature
     * @param decreasing
     * @param iteration
     * @param threshold
     * @param qInit
     * @param alpha
     * @param gamma
     * @param linear
     * @param rta
     * @param conflict
     * @param filePath the file where the results1 will be store. The file is create if it doesn't exist. Other results1 are just added at the end of the file.
     */
    public QL(double initialTemperature, double finalTemperature, double decreasing, int iteration, double threshold, int qInit, double alpha, double gamma, double linear, int rta, int conflict, String filePath){

        long beginning = System.currentTimeMillis();

        // Set different parameters
        Constants.setConflictLinear(linear);
        Constants.setRtaReward(rta);
        Constants.setConflictReward(conflict);
        Constants.setQ0(qInit);

        // The string to be write in the file
        DecimalFormat df = new DecimalFormat("#.###");
        StringBuilder str = new StringBuilder( initialTemperature + " " + finalTemperature + " " + df.format(decreasing) +  " " + iteration + " " + threshold + " " + qInit + " " + df.format(alpha) + " " + df.format(gamma) + " " +linear + " " + rta + " " + conflict);
        System.out.print(str);

        IState state = IState.preProcessing();
        //System.out.println(state.toString());
        //state.toDoc("Print/firstState");

        // to extract and print different data all along the algorithm
        MovingAverage dataAverage = new MovingAverage(3);
        List<Double> data = new LinkedList<>();

        // Sliding window parameters
        double start = 0;
        double end = Constants.windowLength;

        while (start < 25 * 60 * 60){
            // new sliding window

            // Use to stop the algorithm if the reward of the sliding window has not change in the last 'min(100, iteration)' iterations
            MovingAverage movingAverage = new MovingAverage(min(100, iteration));
            boolean flag = true;

            List<? extends IAgent> aircraftInSW = state.getAircraftInSW(start, end);
            //System.out.println("SW before QL = " + state.getPerformanceString(aircraftInSW));

            double temperature = initialTemperature;
            while(temperature > finalTemperature && flag){
                //new temperature value

                for(int i = 0; i < iteration; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(threshold, aircraftInSW);
                    for (IAgent agent : agents) {

                        //double oldReward = agent.setAndGetReward();
                        IDecision decision = agent.getDecision();

                        IQTable q = agent.getQ();
                        int action = q.getBoltzmann(decision, temperature);
                        //int action = q.getGreedy(decision, temperature);

                        IDecision newDecision = q.getDecision(decision, action);

                        agent.setDecision(newDecision);
                        double newReward = agent.setAndGetReward();
                        q.updateQ(decision, action, newReward, alpha, gamma);

                        /*
                        // uncomment to get Q value from "AFR565"
                        if (agent.getId().equals("AFR565")) {
                            double toto = agent.getQ().getActionReward((Decision) decision)[action];
                            data.add(dataAverage.next(toto));
                        }
                        */

                    }

                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature *= decreasing;

                // SWAverage is the moving average of the sliding window reward for the last xxx iterations
                double SWReward = (double) state.getTotalPerformance(aircraftInSW).get(0);
                double SWAverage = movingAverage.next(SWReward);


                /*
                // uncomment to get the reward evolution of the SW starting at 5am
                if(((start / 3600) - 1) == 5){
                    data.add(dataAverage.next(SWAverage));
                }
                 */


                // if the reward of a SW hasn't change over the last xxx iterations, go to the next SW
                flag = (SWReward != SWAverage);
                if(movingAverage.size > movingAverage.n){
                    // to be sure that a minimum of iterations is done at each SW
                    flag = true;
                }

            }

            //System.out.println("temperature = " + temperature/temperatureDecreasingFactor);
            //System.out.println("SW after QL = " + state.stateEvaluation().getSWPerformanceString(start, end));

            // SW parameters update
            start += Constants.windowStep;
            end += Constants.windowStep;

            //System.out.println("time = " + ((start / 3600) - 1) + " hours");

        }

        //System.out.println(state);
        //state.toDoc("QL-result");


        duration = (System.currentTimeMillis() - beginning) / 1000.; // computational time in seconds
        //System.out.println("compute time = " + duration);

        List<Number> perf = state.getTotalPerformance(state.getAgents());

        // String representing the QL results1, to be write in the result file
        StringBuilder str1 = new StringBuilder();
        str1.append(" ").append(df.format(perf.get(0))).append(" ").append(df.format(perf.get(1))).append(" ").append(df.format(perf.get(2))).append(" ").append(perf.get(3)).append(" ").append(perf.get(4)).append(" ").append(df.format(duration));
        System.out.println(str1);
        String toto;
        toto = str.toString();
        TextFileWriter.append(filePath, toto + str1);

        totalReward = (double) perf.get(0);

        //state.toDoc("Print/lastState");

        // data contains whatever has been added all along the algorithm
        //System.out.println("data = " + data);

        StringBuilder results = new StringBuilder();
        for(IAgent agent : state.getAgents()){
            results.append(((Aircraft) agent).toDoc()).append('\n');
        }
        TextFileWriter.append("Print\\\\Results\\\\results2", results.toString());

    }

}
