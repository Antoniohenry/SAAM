package SaamAlgo.Model;

import SaamAlgo.Interface.IDecision;

import java.util.Objects;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Decision implements IDecision {

    private final double speed; // in kt
    private final int deltaTIn; // in sec
    private final Boolean runwayChange;
    private final double arcLength; //in nm
    private final Aircraft.vortexCat aircraftCategory;
    private final Random random = new Random();

    public Decision(double speed, int deltaTIn, Boolean runwayChange, double arcLength, Aircraft.vortexCat vortexCat){
        super();
        this.deltaTIn = deltaTIn;
        this.speed = speed;
        this.arcLength = arcLength;
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

    public double getArcLength() {
        return arcLength;
    }

    public Decision speedUp(){
        return new Decision(min(Constants.nominalApproachSpeed, speed + Constants.speedStep), deltaTIn, runwayChange, arcLength, aircraftCategory);
    }

    public Decision speedDown(){
        switch (aircraftCategory){
            case HEAVY: return new Decision(max(Constants.minimalApproachSpeedH, speed - Constants.speedStep), deltaTIn, runwayChange, arcLength, aircraftCategory);
            case MEDIUM: return new Decision(max(Constants.minimalApproachSpeedM, speed - Constants.speedStep), deltaTIn, runwayChange, arcLength, aircraftCategory);
            case LIGHT: throw new Error("the light aircraft model is not implemented yet");
        }
        throw new Error("unable to speed down");
    }

    public Decision TMAUp(){
        return new Decision(speed, min(Constants.deltaTInMax, deltaTIn + Constants.timeStep), runwayChange, arcLength, aircraftCategory);
    }

    public Decision TMADown(){
        return new Decision(speed, max(Constants.deltaTInMin, deltaTIn - Constants.timeStep), runwayChange, arcLength, aircraftCategory);
    }

    public Decision runwayChange(){
        return new Decision(speed, deltaTIn, !runwayChange, arcLength, aircraftCategory);
    }

    public Decision arcUp(){
        return new Decision(speed, deltaTIn, runwayChange, min(Constants.maxPMLength, arcLength + Constants.lengthStep), aircraftCategory);
    }

    public Decision arcDown(){
        return new Decision(speed, deltaTIn, runwayChange, max(0, arcLength - Constants.lengthStep), aircraftCategory);
    }

    @Override
    public IDecision getNeighbour() {
        double nextDouble = random.nextDouble();

        if(nextDouble < 0.25){
            if(random.nextBoolean()){ //speed up
                return speedUp();
              }
            else { //speed down
                return speedDown();
            }
        }
        if(0.25 <= nextDouble && nextDouble < 0.5){
            if(random.nextBoolean()){ //entry time in TMA up
                return TMAUp();
            }
            else {//entry time in TMA down
                return TMADown();
            }
        }
        if(0.5 <= nextDouble && nextDouble < 0.75){//runway change
            return runwayChange();
}
        if(0.75 <= nextDouble) {//Change the time in the arc
            if(random.nextBoolean()) { //time in arc up
                return arcUp();
                 }
            else { //time in the arc down
                return arcDown();
                }
        }
        return null;
    }

    public Aircraft.vortexCat getCategory() {
        return aircraftCategory;
    }

    public static Decision getNeutralDecision(Aircraft.vortexCat vortexCat){
        return new Decision(Constants.nominalApproachSpeed, 0, false, 0, vortexCat);
    }


    @Override
    public String toString() {
        return "Decision{" +
                "speed=" + speed +
                ", deltaTIn=" + deltaTIn +
                ", runwayChange=" + runwayChange +
                ", arcLength=" + arcLength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Decision)) return false;
        Decision decision = (Decision) o;
        return Double.compare(decision.getSpeed(), getSpeed()) == 0 && getDeltaTIn() == decision.getDeltaTIn() && Double.compare(decision.getArcLength(), getArcLength()) == 0 && Objects.equals(isRunwayChanged(), decision.isRunwayChanged()) && getCategory() == decision.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSpeed(), getDeltaTIn(), isRunwayChanged(), getArcLength(), getCategory());
    }

}