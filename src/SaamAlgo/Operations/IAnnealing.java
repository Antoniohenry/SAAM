package SaamAlgo.Operations;

public interface IAnnealing {


    /**
     * @param state the initial state
     * @return the temperature
     */
    double heating(IState state);


    /** temperature decreasing law
     * @param temperature old temperature
     * @return new temperature
     */
    double decreaseTemperature(double temperature);

    boolean accept(double oldReward, double newReward, double temperature);

}
