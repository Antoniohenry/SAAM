package SaamAlgo;

import SaamAlgo.Optimisation.BOBYQAOptimiser.BOBYQAOptimiser;
import SaamAlgo.Optimisation.DiscreetGA.DiscreetGA;
import SaamAlgo.Optimisation.EDA.EDA;
import SaamAlgo.Interface.IState;
import SaamAlgo.Optimisation.GA.GA;
import SaamAlgo.Optimisation.QL;
import SaamAlgo.Optimisation.SA.SA;


public class Saam {

    public static void main(String[]args) {

        //new QLearning(state, 550, 2E-2, 0.995, 35, 0.7,0.0, -50, 0.16, 0.93);

        //new QLearningSweep();

        //new EDA();

        //new GA(10);

        //new BOBYQAOptimiser();

        //new SA();

        new DiscreetGA();

        //new QL(5000, 1, 0.98, 30, 0.7, -150, 0.18, 0.9, 0.3, 1, 60, "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\DiscreetGA\\DiscreetGA" );

        //GA.init(200);

        //state.toDoc("Q_result.txt");



    }
}


