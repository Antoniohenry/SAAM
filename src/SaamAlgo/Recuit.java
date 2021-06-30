package SaamAlgo;

import SaamAlgo.Model.FlightSet;
import SaamAlgo.Operations.*;

import java.util.List;

//TODO made a copy before changes
public class Recuit implements IAnnealing {

    public Recuit() {
        IState state = IOperations.preProcessing();
        double temperature = heating(state);
        double finalTemperature = temperature / 1000;

        while(temperature > finalTemperature){
            List<IAgent> worstAgents = state.getWorstAgents(1);
            for(IAgent agent : worstAgents){
                IDecision decision = agent.getDecision();
                double oldReward = agent.getReward();
                IDecision newDecision = decision.getNeighbour(temperature);
                agent.setDecision(newDecision);
                double newReward = agent.getReward();
                if(!accept(oldReward, newReward, temperature)){
                    agent.setDecision(decision);
                }

            }
            IStatePerformance statePerformance = state.stateEvaluation();
            System.out.println("statePerformance = " + statePerformance);
            temperature = decreaseTemperature(temperature);

        }

    }


    @Override
    public double heating(IState state) {
        return 100;
    }

    @Override
    public double decreaseTemperature(double temperature) {
        return temperature * 0.99;
    }

    @Override
    public boolean accept(double oldReward, double newReward, double temperature) {
        double aleat = Math.random();
        return aleat > 0.5;
    }
}
