package SaamAlgo.Optimisation;

import SaamAlgo.Graph.Conflict;
import SaamAlgo.Interface.IAgent;
import SaamAlgo.Interface.IDecision;
import SaamAlgo.Interface.IState;
import SaamAlgo.Model.Constants;
import SaamAlgo.Model.QTable;
import SaamAlgo.MovingAverage;
import SaamAlgo.TextFileWriter;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;

public class QL {

    public double duration;
    public double totalReward;

    public QL(double initialTemperature, double finalTemperature, double decreasing, int iteration, double threshold, int qInit, double alpha, double gamma, double linear, int rta, int conflict, String filePath){

        long beginning = System.currentTimeMillis();

        Conflict.setLinear(linear);
        Constants.setRtaReward(rta);
        Constants.setConflictReward(conflict);
        QTable.setqInit(qInit);

        DecimalFormat df = new DecimalFormat("#.###");
        StringBuilder str = new StringBuilder( initialTemperature + " " + finalTemperature + " " + df.format(decreasing) +  " " + iteration + " " + threshold + " " + qInit + " " + df.format(alpha) + " " + df.format(gamma) + " " +linear + " " + rta + " " + conflict);
        System.out.print(str);

        IState state = IState.preProcessing();;
        //System.out.println(state.toString());
        //state.toDoc("Print/firstState");

        //List<Double> reward = new java.util.ArrayList<>(List.of(0.));
        //List<Integer> actions = new java.util.ArrayList<>(List.of(0));
        MovingAverage aircraftMovingAverage = new MovingAverage(100);
        List<Double> aircraftAverage = new LinkedList<>();

        double start = 0;
        double end = Constants.windowLength;

        //List<Double> SWRewardList = new LinkedList<>();
        //List<Double> Average = new LinkedList<>();

        while (start < 25 * 60 * 60){

            MovingAverage movingAverage = new MovingAverage(min(100, iteration));

            List<? extends IAgent> aircraftsInSW = state.getAircraftInSW(start, end);

            //System.out.println("SW before QL = " + state.getPerformanceString(aircraftsInSW));

            boolean flag = true;
            double temperature = initialTemperature;
            while(temperature > finalTemperature && flag){

                for(int i = 0; i < iteration; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(threshold, aircraftsInSW);
                    for (IAgent agent : agents) {

                        //double oldReward = agent.setAndGetReward();
                        IDecision decision = agent.getDecision();

                        QTable q = agent.getQ();
                        int action = q.getBoltzmann(decision, temperature);
                        //int action = q.getGreedy(decision, temperature);

                        IDecision newDecision = q.getDecision(decision, action);

                        /*
                        while (newDecision.equals(decision)){
                            action = q.getGreedy(decision, 1); //To avoid being stuck on bound
                            newDecision = q.getDecision(decision, action);
                        }

                         */

                        agent.setDecision(newDecision);
                        double newReward = agent.setAndGetReward();
                        q.updateQ(decision, action, newReward, alpha, gamma);

                        /*
                        if (agent.getId().equals("AFR565")) {
                            //reward.add(agent.getReward());
                            //actions.add(action);
                            aircraftAverage.add(aircraftMovingAverage.next(agent.getReward()));
                        }

                         */



                    }



                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature *= decreasing;

                double SWReward = (double) state.getTotalPerformance(aircraftsInSW).get(0);
                double SWAverage = movingAverage.next(SWReward);


                /*
                if(((start / 3600) - 1) == 5){
                    SWRewardList.add(SWReward);
                    Average.add(SWAverage);
                }

                 */

                flag = (SWReward != SWAverage);

                if(movingAverage.size > movingAverage.n){
                    flag = true;
                }
                /*
                else {
                    if(!flag){
                        System.out.println("SWReward = " + SWReward);
                        System.out.println("temperature = " + temperature);
                     }
                }

                 */

            }

            //System.out.println("temperature = " + temperature/temperatureDecreasingFactor);


            //System.out.println("SW after QL = " + state.stateEvaluation().getSWPerformanceString(start, end));

            start += Constants.windowStep;
            end += Constants.windowStep;

            //System.out.println("time = " + ((start / 3600) - 1) + " hours");

        }

        //System.out.println(state);

        //System.out.println("reward = " + reward);

        //System.out.println("actions = " + actions);

        //state.toDoc("QL-result");

        //System.out.println("SWRewardList = " + SWRewardList);
        //System.out.println("Average = " + Average);

        duration = (System.currentTimeMillis() - beginning) / 1000.;
        //System.out.println("compute time = " + duration);

        List<Number> perf = state.getTotalPerformance(state.getAgents());

        //System.out.println(str1);
        StringBuilder str1 = new StringBuilder();
        str1.append(" ").append(df.format(perf.get(0))).append(" ").append(df.format(perf.get(1))).append(" ").append(df.format(perf.get(2))).append(" ").append(perf.get(3)).append(" ").append(perf.get(4)).append(" ").append(df.format(duration));
        System.out.println(str1);
        String toto;
        toto = str.toString();
        TextFileWriter.append(filePath, toto + str1);

        totalReward = (double) perf.get(0);

        //state.toDoc("Print/lastState");

        //System.out.println("aircraftAverage = " + aircraftAverage);

    }

}
