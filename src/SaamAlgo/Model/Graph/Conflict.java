package SaamAlgo.Model.Graph;

import SaamAlgo.Model.Constants;

public abstract class Conflict implements IConflict{

    public static double linear = 0.3;

    protected final IFlight flight1;
    protected final IFlight flight2;
    protected double reward;
    private final String name;

    public Conflict(IFlight flight1, IFlight flight2, String name, double criticize) {
        this.flight1 = flight1;
        this.flight2 = flight2;
        this.name = name;
        this.reward = - (linear + (criticize * (1- linear))) * Constants.conflictReward;
        /*
        if(criticize ==1){
            System.out.println(" OVERTAKING ");
        }*/
    }

    public IFlight getFlight1() {
        return flight1;
    }

    public IFlight getFlight2() {
        return flight2;
    }

    public void disableConflict(){
        reward = 0;
    }

    public double getReward() {
        return reward;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Conflict{" +
                "flight1=" + flight1 +
                ", flight2=" + flight2 +
                ", reward=" + reward +
                '}';
    }
}
