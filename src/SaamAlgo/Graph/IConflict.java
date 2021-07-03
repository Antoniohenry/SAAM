package SaamAlgo.Graph;

public interface IConflict {

    /**
     An easy way to put an end to the conflict and update reward on both aircraft
     */
    void disableConflict();

    /**
     An easy way to set conflict on both aircraft
     */
    void setConflict();

    /**
     Get the reward added to both aircraft because of this conflict
     @return Reward
     */
    double getReward();

    /**
     Get tthe edgeName or the nodeName to a conflict
     @return edgeNae or nodeName
     */
    String getName();

}
