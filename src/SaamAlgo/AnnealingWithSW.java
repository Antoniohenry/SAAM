package SaamAlgo;

import SaamAlgo.Operations.*;

import java.util.List;
import java.util.Random;

public class AnnealingWithSW {

    public AnnealingWithSW(){

        IState state = IOperations.preProcessing();
        System.out.println("state.stateEvaluation() = " + state.stateEvaluation());

        double start = 0;
        double end = Constants.windowLength;

        while (start < 25 * 60 * 60){

            double temperature = heating(state);
            double finalTemperature = temperature / 100;
            int iterations = 20;

            System.out.println("SW before SA = " + state.stateEvaluation().getSWPerformanceString(start, end));

            while(temperature > finalTemperature){
                for(int i = 0; i < iterations; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(0.8, start, end);
                    //System.out.println("agents.size() = " + agents.size());
                    for (IAgent agent : agents) {
                        IDecision decision = agent.getDecision();
                        double oldReward = agent.getReward();
                        IDecision newDecision = decision.getNeighbour();
                        agent.setDecision(newDecision);
                        double newReward = agent.getReward();
                        if (!accept(oldReward, newReward, temperature)) {
                            agent.setDecision(decision);
                        }
                    }
                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature = decreaseTemperature(temperature);


            }

            System.out.println("SW after SA = " + state.stateEvaluation().getSWPerformanceString(start, end));

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


    public boolean accept(double oldReward, double newReward, double temperature) {
        return newReward < oldReward || new Random().nextDouble() < Math.exp(-(newReward - oldReward) / temperature);

    }

}
