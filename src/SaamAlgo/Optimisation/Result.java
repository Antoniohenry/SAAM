package SaamAlgo.Optimisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.StringTokenizer;

@Deprecated
public class Result implements Comparable<Result> {

    public double initialTemperature;
    public double finalTemperature;
    public double decreasing;
    public int iteration;
    public double threshold;
    public int qInit;
    public double alpha;
    public double gamma;
    public double linear;
    public int rta;
    public int conflict;
    public double totalReward;
    public double worstReward;
    public double averageDelay;
    public int node;
    public int link;

    public Result(double initialTemperature, double finalTemperature, double decreasing, int iteration, double threshold, int qInit, double alpha, double gamma, double linear, int rta, int conflict, double totalReward, double worstReward, double averageDelay, int node, int link) {
        this.initialTemperature = initialTemperature;
        this.finalTemperature = finalTemperature;
        this.decreasing = decreasing;
        this.iteration = iteration;
        this.threshold = threshold;
        this.qInit = qInit;
        this.alpha = alpha;
        this.gamma = gamma;
        this.linear = linear;
        this.rta = rta;
        this.conflict = conflict;
        this.totalReward = totalReward;
        this.worstReward = worstReward;
        this.averageDelay = averageDelay;
        this.node = node;
        this.link = link;
    }

    public static Result getResultFromString(String str){
        StringTokenizer tokenizer = new StringTokenizer(str);
        return new Result(Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Integer.parseInt(tokenizer.nextToken()),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Integer.parseInt(tokenizer.nextToken()),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Integer.parseInt(tokenizer.nextToken()),
                Integer.parseInt(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Double.parseDouble(tokenizer.nextToken().replace(',', '.')),
                Integer.parseInt(tokenizer.nextToken()),
                Integer.parseInt(tokenizer.nextToken()));

    }

    public static ArrayList<Result> getFromFile(String pathname){

        ArrayList<Result> results = new ArrayList<>();
        try
        {
            File file = new File(pathname);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null)
            {
                results.add(Result.getResultFromString(line));
            }
            fr.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return results;

    }

    public static ArrayList<Result> getMean(ArrayList<Result> results){
        ArrayList<Result> mean = new ArrayList<>();

        for(int indice = 0; indice < results.size(); indice++) {

            LinkedList<Integer> sameToto = new LinkedList<>();

            int j = indice;
            while (results.get(indice).equals(results.get(j))) {
                sameToto.add(j);
                j++;
                if(j == results.size()){
                    break;
                }
            }


            double totalReward = 0;
            double worstReward = 0;
            double delay = 0;
            int node = 0;
            int edge = 0;

            for (int indiceToto : sameToto) {
                Result tt = results.get(indiceToto);
                totalReward += tt.totalReward;
                worstReward += tt.worstReward;
                delay += tt.averageDelay;
                node += tt.node;
                edge += tt.link;
            }

            Result oo = results.get(indice);
            mean.add(new Result(oo.initialTemperature, oo.finalTemperature, oo.decreasing, oo.iteration, oo.threshold, oo.qInit, oo.alpha, oo.gamma, oo.linear, oo.rta, oo.conflict, totalReward/sameToto.size(), worstReward/sameToto.size(), delay/sameToto.size(), node, edge) );
            indice = j;
        }
        return mean;
    }

    @Override
    public String toString() {
        return "Result{" +
                "initialTemperature=" + initialTemperature +
                ", finalTemperature=" + finalTemperature +
                ", decreasing=" + decreasing +
                ", iteration=" + iteration +
                ", threshold=" + threshold +
                ", qInit=" + qInit +
                ", alpha=" + alpha +
                ", gamma=" + gamma +
                ", linear=" + linear +
                ", rta=" + rta +
                ", conflict=" + conflict +
                ", totalReward=" + totalReward +
                ", worstReward=" + worstReward +
                ", averageDelay=" + averageDelay +
                ", node=" + node +
                ", link=" + link +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        Result result = (Result) o;
        return Double.compare(result.initialTemperature, initialTemperature) == 0 && Double.compare(result.finalTemperature, finalTemperature) == 0 && Double.compare(result.decreasing, decreasing) == 0 && iteration == result.iteration && Double.compare(result.threshold, threshold) == 0 && qInit == result.qInit && Double.compare(result.alpha, alpha) == 0 && Double.compare(result.gamma, gamma) == 0 && linear == result.linear && rta == result.rta && Double.compare(result.conflict, conflict) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(initialTemperature, finalTemperature, decreasing, iteration, threshold, qInit, alpha, gamma, linear, rta, conflict);
    }

    @Override
    public int compareTo(Result o) {
        return (Double.compare(totalReward, o.totalReward));
    }

    @Override
    protected Result clone() throws CloneNotSupportedException {
        return (Result) super.clone();
    }
}
