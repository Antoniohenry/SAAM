package SaamAlgo;


import SaamAlgo.Optimisation.BOBYQAOptimiser.BOBYQAOptimiser;
import SaamAlgo.Optimisation.DiscreetGA.DiscreetGA;
import SaamAlgo.Optimisation.QLearningSweep;
import SaamAlgo.Optimisation.SA.SA;

public class Saam {

    public static void main(String[]args) {

        //new QLearningSweep();

        //new SimulatedAnnealing(100, 1, 0.99, 40, 0.7, 0.3, 1, 60);

        //new BOBYQAOptimiser();

        new SA(1000, 1, 0.99);

        //new DiscreetGA(2, 1, 1, 0.2);


        //new QL(2000, 1, 0.99, 30, 0.7, -150, 0.2, 0.88, 0.3, 1, 60, "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\DiscreetGA\\toto");


    }
}


