package SaamAlgo;

import SaamAlgo.Model.Decision;
import SaamAlgo.Model.QTable;
import SaamAlgo.Operations.*;

import java.util.List;
import java.util.Random;

public class QLearning {

    public QLearning(double initialTemperature, double finalTemperature, double temperatureDecreasingFactor, int iterations, double threshold, double epsilon ){

        long millis = System.currentTimeMillis();

        IState state = IOperations.preProcessing();
        System.out.println("state.stateEvaluation() = " + state.stateEvaluation());

        double start = 0;
        double end = Constants.windowLength;

        while (start < 25 * 60 * 60){

            double temperature = initialTemperature;

            //System.out.println("SW before QL = " + state.stateEvaluation().getSWPerformanceString(start, end));

            while(temperature > finalTemperature){
                for(int i = 0; i < iterations; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(threshold, start, end);
                    //System.out.println("agents.size() = " + agents.size());
                    for (IAgent agent : agents) {

                        Decision decision = (Decision) agent.getDecision();
                        double oldReward = agent.getReward();

                        QTable q = agent.getQ();

                        int action;
                        if(i == iterations - 1){
                            action = q.getGreedy(decision, 0);
                        }else {
                            action = q.getGreedy(decision, epsilon);
                        }

                        Decision newDecision = q.getDecision(decision, action);

                        agent.setDecision(newDecision);
                        double newReward = agent.getReward();

                        q.updateQ(decision, action, newReward);

                        if (!accept(oldReward, newReward, initialTemperature)) {
                            agent.setDecision(decision);
                        }

                    }
                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature *= temperatureDecreasingFactor;

                List<Integer> perf = state.stateEvaluation().getSWPerformance(start, end);

                if(perf.get(0) == 0 &&  perf.get(1) == 0){
                    break;
                }

            }

            //System.out.println("SW after QL = " + state.stateEvaluation().getSWPerformanceString(start, end));

            start += Constants.windowStep;
            end += Constants.windowStep;


            //System.out.println("time = " + ((start / 3600) - 1) + " hours");

        }

        System.out.println("statePerformance = " + state.stateEvaluation());

        System.out.println("Computing duration = " + (System.currentTimeMillis() - millis) / 1000 + " seconds");

    }


    public boolean accept(double oldReward, double newReward, double temperature) {
        return newReward < oldReward || new Random().nextDouble() < Math.exp(-(newReward - oldReward) / temperature);
    }

}
