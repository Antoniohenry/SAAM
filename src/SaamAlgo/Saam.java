package SaamAlgo;


import SaamAlgo.Optimisation.BOBYQAOptimiser.BOBYQAOptimiser;
import SaamAlgo.Optimisation.DiscreetGA.DiscreetGA;
import SaamAlgo.Optimisation.QLearningSweep;

public class Saam {

    public static void main(String[]args) {

        //new QLearningSweep();

        //new BOBYQAOptimiser();


        new DiscreetGA(2, 1, 1, 0.2);


        /*
        for(int i =0; i <1; i++) {
            new QL(2000, 1, 0.99, 30, 0.7, -150, 0.2, 0.88, 0.3, 1, 60, "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\DiscreetGA\\toto");
        }

         */


        //state.toDoc("Q_result.txt");

    }
}


