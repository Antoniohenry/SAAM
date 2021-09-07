package SaamAlgo.Interface;

import SaamAlgo.Model.Aircraft;

public interface IDecision {


    /**
     * @return a new decision, different from the original one
     */
    IDecision getNeighbour();

    /**
     * @return the wake vortex category to which this decision could be apply
     */
    Aircraft.vortexCat getCategory();

}
