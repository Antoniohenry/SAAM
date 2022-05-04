package SaamAlgo.Model;

import SaamAlgo.Model.Graph.Edge.Arc;
import SaamAlgo.Model.Graph.Edge.Edge;
import SaamAlgo.Model.Graph.Edge.FinalEdge;
import jdk.dynalink.linker.LinkerServices;

import java.util.List;

public class SlidingWindowParameters {

    public enum status {COMPLETED, ONGOING, ACTIVE, PLANNED}

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

        double earliestEntry = aircraft.getInitialTimeInTMA() - Constants.deltaTInMin;
        double latestEntry = aircraft.getInitialTimeInTMA() + Constants.deltaTInMax;

        double earliestLanding = earliestEntry;
        double latestLanding = latestEntry;

        List<? extends Edge> edges = aircraft.getRoute().getEdges();
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i) instanceof FinalEdge) {
                FinalEdge finalEdge = (FinalEdge) edges.get(i);
                earliestLanding += finalEdge.getLength() / aircraft.landingSpeed;
                latestLanding += finalEdge.getLength() / aircraft.landingSpeed;
            } else if (edges.get(i) instanceof Arc) {
                Arc arc = (Arc) edges.get(i);
                earliestLanding += arc.getLength() / aircraft.maxApproachSpeed;
                latestLanding += (arc.getLength() + Constants.maxPMLength) / aircraft.minApproachSpeed;
            } else {
                earliestLanding += edges.get(i).getLength() / aircraft.maxApproachSpeed;
                latestLanding += edges.get(i).getLength() / aircraft.minApproachSpeed;
            }
        }

        return new SlidingWindowParameters(aircraft.getInitialTimeInTMA(),
                earliestEntry,
                latestEntry,
                aircraft.getTimeOnRunway(),
                earliestLanding,
                latestLanding);
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
