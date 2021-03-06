package SaamAlgo;

import SaamAlgo.Model.Constants;
import SaamAlgo.Interface.*;

import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {

    public SimulatedAnnealing(double initialTemperature, double finalTemperature, double decreasing, int iterations, double threshold, double linear, int rta, int conflict){

        long millis = System.currentTimeMillis(); // to compute the computational time

        Constants.setConflictLinear(linear);
        Constants.setRtaReward(rta);
        Constants.setConflictReward(conflict);

        IState state = IState.preProcessing();
        System.out.println("state = " + state);

        //SW parameters
        double start = 0;
        double end = Constants.windowLength;

        while (start < 25 * 60 * 60){
            List<? extends IAgent> aircraftInSW = state.getAircraftInSW(start, end);
            //System.out.println("SW before SimulatedAnnealing = " + state.getPerformanceString(aircraftInSW));

            double temperature = initialTemperature;
            while(temperature > finalTemperature){
                for(int i = 0; i < iterations; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(threshold, aircraftInSW);
                    //System.out.println("agents.size() = " + agents.size());
                    for (IAgent agent : agents) {
                        IDecision decision = agent.getDecision();
                        double oldReward = agent.setAndGetReward();
                        IDecision newDecision = decision.getNeighbour();
                        agent.setDecision(newDecision);
                        double newReward = agent.setAndGetReward();
                        if (!accept(oldReward, newReward, temperature)) {
                            agent.setDecision(decision);
                        }
                    }

                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature *= decreasing;
            }

            //System.out.println("SW after SimulatedAnnealing = " + state.getPerformanceString(aircraftInSW));

            //SW parameters update
            start += Constants.windowStep;
            end += Constants.windowStep;

            //System.out.println("start = " + ((start / 3600) - 1));

        }

        System.out.println("state = " + state);
        System.out.println("Computing duration = " + (System.currentTimeMillis() - millis) / 1000 + " seconds");

        state.decisionToDoc("SW-result");

    }


    public boolean accept(double oldReward, double newReward, double temperature) {
        return newReward > oldReward || new Random().nextDouble() < Math.exp((newReward - oldReward) / temperature);

    }

}
