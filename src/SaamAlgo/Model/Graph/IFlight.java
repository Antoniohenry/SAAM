package SaamAlgo.Model.Graph;

import SaamAlgo.Model.Aircraft;

public interface IFlight {

    /**
     Define if a flight is in conflict with an other on
     @param other : the other flight
     @return the importance in percentage (0 : no conflict, 1 : huge conflict)
     */
    double isInConflict(IFlight other);

    /**
     Add a conflict to both aircraft
     @param other : the other flight
     @param conflict : the conflict
     */
    void addConflict(IFlight other, IConflict conflict);

    /**
    get the entryTime of the aircraft on the nodeEntry
     @return time in hours
     */
    double getEntryTime();

    /**
     get the aircraft in this flight
     @return a Aircraft
     */
    Aircraft getAircraft();

    /**
     get the exitTime of the aircraft on a runway
     @return time in hours
     */
    double getExitTime();

}
