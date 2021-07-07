package SaamAlgo.Model;

import SaamAlgo.Operations.Constants;
import SaamAlgo.Operations.IDecision;

import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Decision implements IDecision {

    private final double speed; // in kt
    private final int deltaTIn; // in sec
    private final Boolean runwayChange;
    private final double timeInMP; //in seconds
    private final Aircraft.vortexCat aircraftCategory;
    private static final Random generator = new Random();


    public Decision(double speed, int deltaTIn, Boolean runwayChange, double timeInMP, Aircraft.vortexCat vortexCat){
        this.deltaTIn = deltaTIn;
        this.speed = speed;
        this.timeInMP = timeInMP;
        this.runwayChange = runwayChange;
        this.aircraftCategory = vortexCat;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDeltaTIn() {
        return deltaTIn;
    }

    public Boolean isRunwayChanged() {
        return runwayChange;
    }

    public double getTimeInMP() {
        return timeInMP;
    }

    public Decision speedUp(){
        return new Decision(min(Constants.nominalApproachSpeed, speed + Constants.speedStep), deltaTIn, runwayChange, timeInMP, aircraftCategory);
    }

    public Decision speedDown(){
        switch (aircraftCategory){
            case HEAVY: return new Decision(max(Constants.minimalApproachSpeedH, speed - Constants.speedStep), deltaTIn, runwayChange, timeInMP, aircraftCategory);
            case MEDIUM: return new Decision(max(Constants.minimalApproachSpeedM, speed - Constants.speedStep), deltaTIn, runwayChange, timeInMP, aircraftCategory);
            case LIGHT: throw new Error("the light aircraft model is not implemented yet");
        }
        throw new Error("unable to speed down");
    }

    public Decision TMAUp(){
        return new Decision(speed, min(Constants.deltaTInMax, deltaTIn + Constants.timeStep), runwayChange, timeInMP, aircraftCategory);
    }

    public Decision TMADown(){
        return new Decision(speed, max(Constants.deltaTInMin, deltaTIn - Constants.timeStep), runwayChange, timeInMP, aircraftCategory);
    }

    public Decision runwayChange(){
        return new Decision(speed, deltaTIn, !runwayChange, timeInMP, aircraftCategory);
    }

    public Decision timeArcUp(){
        return new Decision(speed, deltaTIn, runwayChange, min(Constants.maxTimeInArc, timeInMP + Constants.timeStep), aircraftCategory);
    }

    public Decision timeArcDown(){
        return new Decision(speed, deltaTIn, runwayChange, max(0, timeInMP - Constants.timeStep), aircraftCategory);
    }

    @Override
    public IDecision getNeighbour() {
        double random = new Random().nextDouble();

        if(random < 0.25){
            if(new Random().nextBoolean()){ //speed up
                return speedUp();
              }
            else { //speed down
                return speedDown();
            }
        }
        if(0.25 <= random && random < 0.5){
            if(new Random().nextBoolean()){ //entry time in TMA up
                return TMAUp();
            }
            else {//entry time in TMA down
                return TMADown();
            }
        }
        if(0.5 <= random && random < 0.75){//runway change
            return runwayChange();
        }
        else {//Change the time in the arc
            if(new Random().nextBoolean()) { //time in arc up
                return timeArcUp();
                 }
            else { //time in the arc down
                return timeArcDown();
                }
        }

    }

    public Aircraft.vortexCat getAircraftCategory() {
        return aircraftCategory;
    }

    public Boolean getRunwayChange() {
        return runwayChange;
    }

    public static Decision getNeutralDecision(Aircraft.vortexCat vortexCat){
        return new Decision(Constants.nominalApproachSpeed, 0, false, Constants.standardTimeInArc, vortexCat);
    }

    @Override
    public String toString() {
        return "Decision{" +
                "speed=" + speed +
                ", delatTIn=" + deltaTIn +
                ", runwayChange=" + runwayChange +
                ", timeInMP=" + timeInMP +
                '}';
    }
}