package SaamAlgo.Model;

import SaamAlgo.Interface.IDecision;

import java.util.*;

import static java.lang.Double.min;


public class QTable implements IQTable {

    private final int numberOfActionsPossible = 8;
    private final Random random = new Random();

    int dimSpeed;
    int dimEntryTime;
    int dimMP;
    int dimRunway;

    public static int qInit = -60;

    private final double[][][][][] q;

    public QTable(Aircraft aircraft) {

        dimSpeed = (int) ((Constants.nominalApproachSpeed - aircraft.getMinimalApproachSpeed()) / Constants.speedStep) + 1;
        dimEntryTime = (Constants.deltaTInMax - Constants.deltaTInMin) / Constants.timeStep + 1;
        dimMP = (int) Constants.maxTimeInArc / Constants.timeStep + 1;
        dimRunway = 2;

        q = new double[dimSpeed][dimEntryTime][dimMP][dimRunway][numberOfActionsPossible];

        resetQTable(qInit);

    }

    public void resetQTable(double Qinit){
        for(int s = 0; s < dimSpeed; s++){
            for(int tma = 0; tma < dimEntryTime; tma++ ){
                for(int mp = 0; mp < dimMP; mp ++){
                    for(int r = 0; r < dimRunway; r++){
                        for(int act = 0; act < numberOfActionsPossible; act ++){
                            q[s][tma][mp][r][act] = Qinit + random.nextDouble() ;
                        }
                    }
                }
            }
        }
    }

    public double[] getActionReward(Decision decision){

        int minimalApproachSpeed;
        switch (decision.getCategory()){
            case HEAVY : minimalApproachSpeed = Constants.minimalApproachSpeedH; break;
            case MEDIUM : minimalApproachSpeed = Constants.minimalApproachSpeedM; break;
            default:
                throw new IllegalStateException("Unexpected value for wake vortex cat: " + decision.getCategory());
        }


        int speed = ((int) (decision.getSpeed()) - minimalApproachSpeed) / Constants.speedStep;
        int tma = (decision.getDeltaTIn() - Constants.deltaTInMin) / Constants.timeStep;
        int mp = (int) (decision.getTimeInMP() / Constants.timeStep);
        int runway;
        if(decision.isRunwayChanged()){
            runway = 1;
        }
        else runway = 0;

        return q[speed][tma][mp][runway];

    }

    public List<Number> getBestAction(Decision decision){
        double[] actionReward = getActionReward(decision);

        int maxIndex = 0;
        double max = -10000000;
        for(int i = 0; i < actionReward.length; i++){
            if(actionReward[i] > max){
                max = actionReward[i];
                maxIndex = i;
            }
        }

        return List.of(maxIndex, max);

    }

    @Override
    public IDecision getDecision(IDecision oldIDecision, int action){

        Decision oldDecision = (Decision) oldIDecision;

        switch (action){
            case 0 : return oldDecision.speedUp();
            case 1 : return oldDecision.speedDown();
            case 2 : return oldDecision.TMAUp();
            case 3 : return oldDecision.TMADown();
            case 4 : return oldDecision.timeArcUp();
            case 5 : return oldDecision.timeArcDown();
            case 6 : return oldDecision.runwayChange();
            case 7 : return new Decision(oldDecision.getSpeed(), oldDecision.getDeltaTIn(), false, oldDecision.getTimeInMP(), oldDecision.getCategory());
            default: throw new Error("unknown action");
        }
    }

    @Override
    public void updateQ(IDecision oldIDecision, int action, double reward, double alpha, double gamma){
        Decision newDecision = (Decision) getDecision(oldIDecision, action);
        List<Number> bestAction = getBestAction(newDecision);
        getActionReward((Decision) oldIDecision)[action] += alpha * (reward + gamma * ((double) bestAction.get(1)) - getActionReward((Decision) oldIDecision)[action]);
    }

    @Override
    public int getGreedy(IDecision oldDecision_, double epsilon){
        //0.1 5.0E-4 0,992 40 0.7 -80 0,18 0,92 0.3 1 60 -427,122 -6,172 0,953 0 0 18,461
        Decision oldDecision = (Decision) oldDecision_;

        int action;

        if(random.nextDouble() < epsilon){
            action = random.nextInt(numberOfActionsPossible);
        }
        else {
            action = (int) getBestAction(oldDecision).get(0);

        }
        return action;
    }

    @Override
    public int getBoltzmann(IDecision oldDecision, double temperature){

        double [] qs = getActionReward((Decision) oldDecision).clone();

        double sum = 0;
        for(int i = 0; i < qs.length; i++){
            qs[i] = min(Math.exp((qs[i]) / temperature), 1E300 / 8);
            sum += qs[i];

        }

        if(sum == 0){
            System.out.println("random action taken");
            return random.nextInt(numberOfActionsPossible);
        }

        double aleat = random.nextDouble() * sum;

        double sum_ = 0;
        for(int i = 0; i < qs.length; i++){
            sum_ += qs[i];
            if(sum_ >= aleat){
                return i;
            }
        }

        throw new Error("cannot find boltzmann action");

    }

}
