package SaamAlgo;

import SaamAlgo.Model.Decision;
import SaamAlgo.Model.QTable;
import SaamAlgo.Operations.*;

import java.util.LinkedList;
import java.util.List;

public class QLearning {

    public double duration;

    public QLearning(IState state, double initialTemperature, double finalTemperature, double temperatureDecreasingFactor, int iterations, double threshold, double epsilon, double Qinit, double alpha, double gamma){
        long millis = System.currentTimeMillis();
        System.out.println(state.toString());

        List<Double> reward = new java.util.ArrayList<>(List.of(0.));
        List<Integer> actions = new java.util.ArrayList<>(List.of(0));

        for(IAgent agent : state.getAgents()){
            agent.getQ().resetQTable(Qinit);
        }

        double start = 0;
        double end = Constants.windowLength;

        List<Double> SWRewardList = new LinkedList<>();
        List<Double> Average = new LinkedList<>();

        while (start < 25 * 60 * 60){

            MovingAverage movingAverage = new MovingAverage(iterations);

            List<? extends IAgent> aircraftsInSW = state.getAircraftInSW(start, end);

            //System.out.println("SW before QL = " + state.getPerformanceString(aircraftsInSW));

            boolean flag = true;
            double temperature = initialTemperature;
            while(temperature > finalTemperature && flag){

                for(int i = 0; i < iterations; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(threshold, aircraftsInSW);
                    for (IAgent agent : agents) {

                        Decision decision = (Decision) agent.getDecision();

                        QTable q = agent.getQ();
                        int action = q.getGreedy(decision, temperature);

                        Decision newDecision = q.getDecision(decision, action);

                        /*
                        while (newDecision.equals(decision)){
                            action = q.getGreedy(decision, 1); //To avoid being stuck on bound
                            newDecision = q.getDecision(decision, action);
                        }

                         */

                        agent.setDecision(newDecision);
                        double newReward = agent.getReward();

                        q.updateQ(decision, action, newReward, alpha, gamma);

                        /*
                        if (agent.getId().equals("AFR565")) {
                            if(accept) {
                                reward.add(agent.getReward());
                                actions.add(action);
                                //System.out.println("q.getActionReward(decision) = " + Arrays.toString(q.getActionReward(decision)));
                            }
                        }

                         */

                    }

                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature *= temperatureDecreasingFactor;

                double SWReward = (double) state.getTotalPerformance(aircraftsInSW).get(0);
                double SWAverage = movingAverage.next(SWReward);


                if(((start / 3600) - 1) == 18){
                    SWRewardList.add(SWReward);
                    Average.add(SWAverage);
                }

                flag = (SWReward != SWAverage);

                if(movingAverage.size > movingAverage.n){
                    flag = true;
                }
                else {
                    if(!flag){
                        //System.out.println("SWReward = " + SWReward);
                        //System.out.println("temperature = " + temperature);
                    }
                }

            }

            //System.out.println("temperature = " + temperature/temperatureDecreasingFactor);


            //System.out.println("SW after QL = " + state.stateEvaluation().getSWPerformanceString(start, end));

            start += Constants.windowStep;
            end += Constants.windowStep;

            //System.out.println("time = " + ((start / 3600) - 1) + " hours");

        }

        System.out.println(state);

        //System.out.println("reward = " + reward);

        //System.out.println("actions = " + actions);

        //state.toDoc("QL-result");

        System.out.println("SWRewardList = " + SWRewardList);
        System.out.println("Average = " + Average);

        duration = (System.currentTimeMillis() - millis) / 1000.;
        System.out.println("compute time = " + duration);


    }

}
