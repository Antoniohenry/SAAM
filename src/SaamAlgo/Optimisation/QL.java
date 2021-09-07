package SaamAlgo.Optimisation;

import SaamAlgo.Graph.Conflict;
import SaamAlgo.Interface.IAgent;
import SaamAlgo.Interface.IDecision;
import SaamAlgo.Interface.IState;
import SaamAlgo.Model.Constants;
import SaamAlgo.Model.Decision;
import SaamAlgo.Model.QTable;
import SaamAlgo.MovingAverage;
import SaamAlgo.TextFileWriter;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;

public class QL {

    public double duration;
    public double totalReward;

    public QL(double initialTemperature, double finalTemperature, double decreasing, int iteration, double threshold, int qInit, double alpha, double gamma, double linear, int rta, int conflict, String filePath){

        long beginning = System.currentTimeMillis();

        Conflict.setLinear(linear);
        Constants.setRtaReward(rta);
        Constants.setConflictReward(conflict);
        QTable.setqInit(qInit);

        DecimalFormat df = new DecimalFormat("#.###");
        StringBuilder str = new StringBuilder( initialTemperature + " " + finalTemperature + " " + df.format(decreasing) +  " " + iteration + " " + threshold + " " + qInit + " " + df.format(alpha) + " " + df.format(gamma) + " " +linear + " " + rta + " " + conflict);
        System.out.print(str);

        IState state = IState.preProcessing();;
        //System.out.println(state.toString());
        //state.toDoc("Print/firstState");

        MovingAverage dataAverage = new MovingAverage(3);
        List<Double> data = new LinkedList<>();

        double start = 0;
        double end = Constants.windowLength;

        while (start < 25 * 60 * 60){

            MovingAverage movingAverage = new MovingAverage(min(100, iteration));

            List<? extends IAgent> aircraftsInSW = state.getAircraftInSW(start, end);

            //System.out.println("SW before QL = " + state.getPerformanceString(aircraftsInSW));

            boolean flag = true;
            double temperature = initialTemperature;
            while(temperature > finalTemperature && flag){

                for(int i = 0; i < iteration; i++) {
                    List<IAgent> agents = state.getAgentsToHandled(threshold, aircraftsInSW);
                    for (IAgent agent : agents) {

                        //double oldReward = agent.setAndGetReward();
                        IDecision decision = agent.getDecision();

                        QTable q = agent.getQ();
                        int action = q.getBoltzmann(decision, temperature);
                        //int action = q.getGreedy(decision, temperature);

                        IDecision newDecision = q.getDecision(decision, action);


                        agent.setDecision(newDecision);
                        double newReward = agent.setAndGetReward();
                        q.updateQ(decision, action, newReward, alpha, gamma);

                        /*
                        if (agent.getId().equals("AFR565")) {
                            double toto = agent.getQ().getActionReward((Decision) decision)[action];
                            data.add(dataAverage.next(toto));
                        }
                        */

                    }

                }

                //System.out.println("state.stateEvaluation = " + state.stateEvaluation());
                temperature *= decreasing;

                double SWReward = (double) state.getTotalPerformance(aircraftsInSW).get(0);
                double SWAverage = movingAverage.next(SWReward);




                if(((start / 3600) - 1) == 5){
                    data.add(dataAverage.next(SWAverage));
                }


                flag = (SWReward != SWAverage);

                if(movingAverage.size > movingAverage.n){
                    flag = true;
                }

            }

            //System.out.println("temperature = " + temperature/temperatureDecreasingFactor);

            //System.out.println("SW after QL = " + state.stateEvaluation().getSWPerformanceString(start, end));

            start += Constants.windowStep;
            end += Constants.windowStep;

            //System.out.println("time = " + ((start / 3600) - 1) + " hours");

        }

        //System.out.println(state);

        //state.toDoc("QL-result");


        duration = (System.currentTimeMillis() - beginning) / 1000.;
        //System.out.println("compute time = " + duration);

        List<Number> perf = state.getTotalPerformance(state.getAgents());

        if( (int) perf.get(3) + (int) perf.get(4) > 0){
            System.out.println(" conflict ");
        }

        //System.out.println(str1);
        StringBuilder str1 = new StringBuilder();
        str1.append(" ").append(df.format(perf.get(0))).append(" ").append(df.format(perf.get(1))).append(" ").append(df.format(perf.get(2))).append(" ").append(perf.get(3)).append(" ").append(perf.get(4)).append(" ").append(df.format(duration));
        System.out.println(str1);
        String toto;
        toto = str.toString();
        TextFileWriter.append(filePath, toto + str1);

        totalReward = (double) perf.get(0);

        //state.toDoc("Print/lastState");

        System.out.println("data = " + data);
    }

}
