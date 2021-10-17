package SaamAlgo.Util;

public class Statistics {

    Double sum;
    Double quadSum;
    int length;

    public Statistics() {
        sum = 0.0;
        quadSum = 0.0;
        length = 0;
    }

    public void add(Number num){
        double number = (double) num;
        sum += number;
        quadSum += Math.pow(number, 2);
        length += 1;

    }

    public double getMean() {
        return sum / length;
    }

    public double getStandardDeviation(){
        return Math.sqrt(quadSum / length);
    }


}


