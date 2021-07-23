package SaamAlgo;

import SaamAlgo.Operations.*;

import java.util.List;
import java.util.Random;

//TODO made a copy before changes
public class Annealing implements IAnnealing {

    public Annealing() {
        IState state = IOperations.preProcessing();
        double temperature = heating(state);
        double finalTemperature = temperature / 500;
        int iterations = 100;

        while(temperature > finalTemperature){
            for(int i = 0; i < iterations; i++) {
                List<IAgent> worstAgents = state.getAgentsToHandled(0, state.getAgents());
                //System.out.println("number of aircrafts handled " + worstAgents.size());
                for (IAgent agent : worstAgents) {
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

            System.out.println("state = " + state);

            temperature = decreaseTemperature(temperature);

        }
        //System.out.println("state = " + state);
        System.out.println("state = " + state);

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
        return newReward > oldReward || new Random().nextDouble() < Math.exp((newReward - oldReward) / temperature);

    }
}
