package SaamAlgo.Model;

import SaamAlgo.Model.Graph.Conflict;

public class Constants {

    public static double rtaReward = 1; //en point de reward par minute de retard ou d'avance
    public static double runwayReward = 5;
    public static double conflictReward = 60;

    public static double nodeRadius = 2.2; // in nm

    public static double maxPMLength = 10; // in nm
    public static double lengthStep = 0.7; // in nm

    public static int nominalApproachSpeed = 250;
    public static int minimalApproachSpeedM = 190; // 1.3 * (101.30 * 1.06) + 50 = 189.59 kt
    public static int nominalLandingSpeedM = 155; //  1.3 * (101.30 * 1.04) + 20 = 156.95 kt maybe 140

    public static int minimalApproachSpeedH = 220; // 1.3 * (122.00 * 1.06) + 50 = 218.11 kt;
    public static int nominalLandingSpeedH = 185; // 1.3 * (122.00 * 1.04) + 20 = 184.94 kt maybe 165

    public static int speedStep = 10; //in kt

    public static int deltaTInMax = 6  * 60; //in seconds
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

    public static void setConflictReward(double conflictReward_){
        conflictReward = conflictReward_;
    }

    public static void setRtaReward(double rtaReward_){
        rtaReward = rtaReward_;
    }

    public static void setConflictLinear(double linear){
        Conflict.linear = linear;
    }

    public static void setQ0(int Q0){
        QTable.qInit = Q0;
    }

}
