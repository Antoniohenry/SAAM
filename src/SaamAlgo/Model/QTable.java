package SaamAlgo.Model;

import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Decision;
import SaamAlgo.Operations.Constants;

import java.util.List;
import java.util.Random;


public class QTable {

    private final int numberOfActionsPossible = 8;
    private final Random random = new Random();

    double alpha = 0.1;
    double gamma = 0.9;


    private double[][][][][] q;

    public QTable(Aircraft aircraft) {

        int dimSpeed = (int) ((Constants.nominalApproachSpeed - aircraft.getMinimalApproachSpeed()) / Constants.speedStep) + 1;
        int dimEntryTime = (Constants.deltaTInMax - Constants.deltaTInMin) / Constants.timeStep + 1;
        int dimMP = (int) Constants.maxTimeInArc / Constants.timeStep + 1;
        int dimRunway = 2;

        q = new double[dimSpeed][dimEntryTime][dimMP][dimRunway][numberOfActionsPossible];

        for(int s = 0; s < dimSpeed; s++){
            for(int tma = 0; tma < dimEntryTime; tma++ ){
                for(int mp = 0; mp < dimMP; mp ++){
                    for(int r = 0; r < dimRunway; r++){
                        for(int act = 0; act < numberOfActionsPossible; act ++){
                            q[s][tma][mp][r][act] = 1000;
                        }
                    }
                }
            }
        }

    }

    public double[] getActionReward(Decision decision){

        int minimalApproachSpeed;
        switch (decision.getAircraftCategory()){
            case HEAVY : minimalApproachSpeed = Constants.minimalApproachSpeedH; break;
            case MEDIUM : minimalApproachSpeed = Constants.minimalApproachSpeedM; break;
            default:
                throw new IllegalStateException("Unexpected value for wake vortex cat: " + decision.getAircraftCategory());
        }


        int speed = ((int) (decision.getSpeed()) - minimalApproachSpeed) / Constants.speedStep;
        int tma = (decision.getDeltaTIn() - Constants.deltaTInMin) / Constants.timeStep;
        int mp = (int) (decision.getTimeInMP() / Constants.timeStep);
        int runway;
        if(decision.getRunwayChange()){
            runway = 1;
        }
        else runway = 0;

        return q[speed][tma][mp][runway];

    }

    public List<Number> getBestAction(Decision decision){
        double[] actionReward = getActionReward(decision);

        int maxIndex = 0;
        double max = 10000;
        for(int i = 0; i < actionReward.length; i++){
            if(actionReward[i] < max){
                max = actionReward[i];
                maxIndex = i;
            }
        }

        return List.of(maxIndex, max);

    }

    public Decision getDecision(Decision oldDecision, int action){
        switch (action){
            case 0 : return oldDecision.speedUp();
            case 1 : return oldDecision.speedDown();
            case 2 : return oldDecision.TMAUp();
            case 3 : return oldDecision.TMADown();
            case 4 : return oldDecision.timeArcUp();
            case 5 : return oldDecision.timeArcDown();
            case 6 : return oldDecision.runwayChange();
            case 7 : return oldDecision;
            default: throw new Error("unknown action");
        }
    }

    public void updateQ(Decision oldDecision, int action, double reward){
        Decision newDecision = getDecision(oldDecision, action);
        List<Number> bestAction = getBestAction(newDecision);
        getActionReward(oldDecision)[action] += alpha * (reward + gamma * (double) bestAction.get(1) - getActionReward(oldDecision)[action]);
    }

    public int getGreedy(Decision oldDecision, double epsilon){
        int action;

        if(random.nextDouble() < epsilon){
            action = random.nextInt(numberOfActionsPossible);
        }
        else {
            action = (int) getBestAction(oldDecision).get(0);
        }
        return action;
    }


}
