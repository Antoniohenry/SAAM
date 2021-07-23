package SaamAlgo.Model;

import SaamAlgo.Operations.Constants;

public class SlidingWindowParameters {

    public enum status {COMPLETED, ONGOING, ACTIVE, PLANNED};

    private double initialEntryTMA;
    private final double earliestEntryTMA;
    private final double latestEntryTMA;
    private double landing;
    private double earliestLanding;
    private final double latestLanding;

    public SlidingWindowParameters(double initialEntryTMA, double earliestEntryTMA, double latestEntryTMA, double landing, double earliestLanding, double latestLanding) {
        this.initialEntryTMA = initialEntryTMA;
        this.earliestEntryTMA = earliestEntryTMA;
        this.latestEntryTMA = latestEntryTMA;
        this.landing = landing;
        this.earliestLanding = earliestLanding;
        this.latestLanding = latestLanding;
    }

    public static SlidingWindowParameters getInstance(Aircraft aircraft){

        return new SlidingWindowParameters(aircraft.getInitialTimeInTMA(),
                aircraft.getInitialTimeInTMA() - Constants.deltaTInMin,
                aircraft.getInitialTimeInTMA() + Constants.deltaTInMax,
                aircraft.getTimeOnRunway(),
                aircraft.getInitialTimeInTMA() + aircraft.getRoute().getFlyingTime(aircraft.getMinimalApproachSpeed(), aircraft.getLandingSpeed()),
                aircraft.getInitialTimeInTMA() + aircraft.getRoute().getFlyingTime(Constants.nominalApproachSpeed, aircraft.getLandingSpeed()));
    }

    public status getStatus(double startTime, double endTime){
        if(latestLanding <= startTime){
            return status.COMPLETED;
        }
        if(earliestEntryTMA <= startTime && startTime < latestLanding){
            return status.ONGOING;
        }
        if(startTime < earliestEntryTMA && latestEntryTMA <= endTime){
            return status.ACTIVE;
        }
        if(endTime < latestEntryTMA){
            return status.PLANNED;
        }
        throw new Error("cannot obtained status");
    }

}
