package SaamAlgo;

import SaamAlgo.Interface.IState;


public class Saam {

    public static void main(String[]args) {

        IState state = IState.preProcessing();

        state.toDoc("firststate");

        new QLearningSweep();

        //state.toDoc("Q_result.txt");



    }
}


