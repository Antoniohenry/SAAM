package SaamAlgo;


//TODO separer operation et calcul de reward
public class Saam {

    public static void main(String[]args) {

        //new Annealing();
        //new AnnealingWithSW();
        new QLearning(10, 0.001, 0.99, 300, 0.7, 0.1);

    }
}


