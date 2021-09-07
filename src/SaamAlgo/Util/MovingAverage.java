package SaamAlgo.Util;

import java.util.Arrays;

/**
 * This class handle a moving average list
 */
public class MovingAverage {
    private final double [] window;
    private int index;
    public int size;
    private double sum;
    public int n;

    public MovingAverage(int size) {
        window = new double[size];
        this.size = size;
        this.index = 0;
        this.n = 0;
    }

    /**
     * @param val new value ti be add in the list
     * @return the average of all value in the list
     */
    public double next(double val) {

        if (n < window.length){
            n++;
        }
        else {
            sum -= window[index];
        }

        sum += val;
        window[index] = val;

        if(n == window.length) {
            if (Arrays.stream(window).min().getAsDouble() == Arrays.stream(window).max().getAsDouble()) {
                return val;
            }
        }

        index = (index + 1) % window.length;
        return sum / n;
    }
}
