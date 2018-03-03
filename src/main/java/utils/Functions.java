package utils;

import models.Coordinate;
import models.Ride;

/**
 * @author migueloliveira
 */
public class Functions
{

    public static Integer calculateDistance(Coordinate start, Coordinate end)
    {
        return Math.abs(end.getC() - start.getC()) + Math.abs(end.getR() - start.getR());
    }

    public static Double targetFunction(Ride ride, Coordinate coordinate, int step)
    {
        Integer carDistance = Functions.calculateDistance(ride.getStartIntersection(), coordinate);

        // The time the vehicle waits until the ride begins
        Integer waitingTime = ride.getEarliestStart() - step - carDistance;

        // Consider the bonus plus the ride distance; it is favoured the minimum waiting time and the rides that finishes early
        return ((waitingTime >= 0 ? Constants.BONUS : 0) + ride.getDistance()) /
            ((ride.getLatestStart() - step) * (waitingTime == 0 ? 1 : (double)Math.abs(waitingTime)));
    }

}
