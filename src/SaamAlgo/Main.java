package SaamAlgo;

import SaamAlgo.Optimisation.DiscreetGA.DiscreetGA;
import SaamAlgo.Optimisation.QLearningSweep;

public class Main {

    public static void main(String[]args) {

        String file;
        if (args.length >= 1) {
            file = args[0];
        }
        else {
            file = "Results";
        }

        //new QLearningSweep();

        //new SimulatedAnnealing(100, 1, 0.99, 40, 0.7, 0.3, 1, 60);

        new DiscreetGA(10, 10, 5, 0.2);

        /**
         * A new QL is run with the given parameters, and the result will be store in the file 'filePath'
         * @param initialTemperature
         * @param finalTemperature
         * @param decreasing
         * @param iteration
         * @param threshold
         * @param qInit
         * @param alpha
         * @param gamma
         * @param linear
         * @param rta
         * @param conflict
         * @param filePath the file where the results will be store. The file is create if it doesn't exist. Otherwise results are just added at the end of the file.
         */
        for(int i = 0; i < 1; i++) {
            //new QL(5000, 1, 0.99, 75, 0.7, -150, 0.18, 0.9, 0.3, 1, 60, file);
        }

    }
}


