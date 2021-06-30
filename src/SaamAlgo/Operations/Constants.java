package SaamAlgo.Operations;

public class Constants {

    public static double nodeRadius = 2.2; //nm
    public static double rtaReward = 0.1; //en point de reward par minute de retard ou d'avance
    public static int speedInArc = 180;// kts
    public static double overtakingReward = 3; //reward penalty en cas d'overtaking
    public static double standardTimeInArc = 3.0/60; //en heure
    public static double runwayReward = 1;

    public static int deltaVMax = 20; //en noeud
    public static int deltaVMin = -20;

    public static int deltaTInMax = 5; //en minute
    public static int deltaTInMin = -5;

    public static double maxTimeInArc = 6.; //en minute


    public static double[][] TABLE_SEPARATION= //nm
            {
                    {3, 3, 3},
                    {4, 3, 3},
                    {6, 5, 4}
            };
}
