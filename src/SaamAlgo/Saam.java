package SaamAlgo;


import SaamAlgo.Model.Aircraft;
import SaamAlgo.Model.Decision;
import SaamAlgo.Model.FlightSet;
import SaamAlgo.Operations.IAgent;
import SaamAlgo.Operations.IOperations;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

//TODO separer operation et calcul de reward
public class Saam {

    public static void main(String[]args) {

        /*
        FlightSet flightSet = new FlightSet();

        for(int j = 0; j<3; j++) {

            for (Aircraft aircraft : flightSet.getAircrafts()) {
                System.out.println(aircraft.printReward());
            }

            int nodeConflicts = flightSet.getNodeConflictNumber();
            int edgeConflicts = flightSet.getEdgeConflictNumber();
            System.out.println("edgeConflicts = " + edgeConflicts);
            System.out.println("nodeConflicts = " + nodeConflicts);

            double reward = flightSet.getFlightSetReward();
            System.out.println("reward before modification = " + reward);

            List<IAgent> criticalAircrafts = flightSet.getWorstAgents(1.1);

            for (ListIterator<IAgent> i = criticalAircrafts.listIterator(); i.hasNext(); ) {
                IAgent aircraft = i.next();
                IOperations.putDecision(aircraft, Decision.getAleatDecision());
                i.set(aircraft);
            }

            reward = flightSet.getFlightSetReward();
            System.out.println("reward after modification = " + reward);

        } */


        new Recuit();

    }
}


