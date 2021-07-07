package SaamAlgo;

import SaamAlgo.Model.Decision;
import SaamAlgo.Model.QTable;
import SaamAlgo.Operations.*;

import java.util.List;
import java.util.Random;

public class QLearning {

    public QLearning(){

        IState state = IOperations.preProcessing();
        System.out.println("state.stateEvaluation() = " + state.stateEvaluation());

        double start = 0;
        double end = Constants.windowLength;

        while (start < 25 * 60 * 60){

            double temperature = heating(state);
            double finalTemperature = temperature / 1000;
            int iterations = 200;

            System.out.println("SW before SA = " + state.stateEvaluation().getSWPerformance(start, end));

            while(temperature > finalTemperature){
                for(int i = 0; i < iterations; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(0.8, start, end);
                    //System.out.println("agents.size() = " + agents.size());
                    for (IAgent agent : agents) {

                        Decision decision = (Decision) agent.getDecision();

                        QTable q = agent.getQ();
                        double epsilon;
                        if(i == iterations - 1){
                            epsilon = 0;
                        }else epsilon = 0.1;

                        int action = q.getGreedy(decision, epsilon);
                        Decision newDecision = q.getDecision(decision, action);

                        agent.setDecision(newDecision);
                        double newReward = agent.getReward();

                        q.updateQ(decision, action, newReward);
                    }
                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature = decreaseTemperature(temperature);

            }

            System.out.println("SW after SA = " + state.stateEvaluation().getSWPerformance(start, end));

            start += Constants.windowStep;
            end += Constants.windowStep;


            System.out.println("start = " + ((start / 3600) - 1));

        }

        System.out.println("statePerformance = " + state.stateEvaluation());

    }


    public double heating(IState state) {
        return 200;
    }


    public double decreaseTemperature(double temperature) {
        return temperature * 0.99;
    }


}
