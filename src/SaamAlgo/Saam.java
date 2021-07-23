package SaamAlgo;


import SaamAlgo.Operations.IOperations;
import SaamAlgo.Operations.IState;

//TODO separer operation et calcul de reward
public class Saam {

    public static void main(String[]args) {

        IState state = IOperations.preProcessing();

        //new AnnealingWithSW(state, 1, 0.001, 0.9, 300, 0.8);

        //state.toDoc("SA_result.txt");

        //state = IOperations.preProcessing();

        //new QLearning(state, 1E-1, 1E-5, 0.993, 30, 0.6, 0., -52.5, 0.2, 0.93);

        new QLearningSweep();

        //state.toDoc("Q_result.txt");



    }
}


