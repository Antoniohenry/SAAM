package SaamAlgo.Operations;

public class Constants {

    public static double rtaReward = 1; //en point de reward par minute de retard ou d'avance
    public static double runwayReward = 2;
    public static double conflictReward = 100;

    public static double nodeRadius = 2.2;

    public static double maxTimeInArc = 4. * 60; //in seconds
    public static double standardTimeInArc = 1. * 60; //in sec

    public static int nominalApproachSpeed = 250;
    public static int minimalApproachSpeedM = 190; // 1.3 * (101.30 * 1.06) + 50 = 189.59 kt
    public static int nominalLandingSpeedM = 155; //  1.3 * (101.30 * 1.04) + 20 = 156.95 kt

    public static int minimalApproachSpeedH = 220; // 1.3 * (122.00 * 1.06) + 50 = 218.11 kt;
    public static int nominalLandingSpeedH = 185; // 1.3 * (122.00 * 1.04) + 20 = 184.94 kt

    public static int speedStep = 10; //in kt

    public static int deltaTInMax = 5  * 60; //in seconds
    public static int deltaTInMin = -1 * 60;

    public static int timeStep = 20; // in Seconds

    public static double windowLength = 18 * 60; //in seconds
    public static double windowStep = 6 * 60; //in seconds

    public static double offsetTimeStartingTheSimulation = windowLength; //in seconds

    public static double[][] TABLE_SEPARATION= //nm
            {
                    {3, 3, 3},
                    {4, 3, 3},
                    {6, 5, 4}
            };

    public static double HOURS_TO_SEC = 3600;
    public static double SEC_TO_HOURS = 1/3600.;

}
