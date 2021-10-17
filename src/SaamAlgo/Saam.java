package SaamAlgo;

import SaamAlgo.Optimisation.DiscreetGA.DiscreetGA;
import SaamAlgo.Optimisation.QLearningSweep;

public class Saam {

    public static void main(String[]args) {

        //new QLearningSweep();

        //new SimulatedAnnealing(100, 1, 0.99, 40, 0.7, 0.3, 1, 60);

        //new DiscreetGA(2, 1, 1, 0.2);

        for(int i = 0; i < 1; i++) {
            new QL(5000, 1, 0.992, 75, 0.7, -150, 0.18, 0.9, 0.3, 1, 60, "Print\\\\Results\\\\total");
        }

    }
}


