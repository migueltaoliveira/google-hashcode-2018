package models;

import utils.Functions;

/**
 * @author migueloliveira
 */
public class Ride
{
    private Integer id;
    private Coordinate startIntersection;
    private Coordinate finishIntersection;
    private int earliestStart;
    private int latestStart;
    private Integer pickedAt;
    private Integer finishedAt;

    private Vehicle vehicle;

    private int takenSteps;
    private int distance;
    private boolean started;
    private boolean finished;

    public Ride(Coordinate startIntersection, Coordinate finishIntersection, Integer earliestStart, Integer latestStart, Integer id)
    {
        this.id = id;
        this.startIntersection = startIntersection;
        this.finishIntersection = finishIntersection;
        this.earliestStart = earliestStart;
        this.latestStart = latestStart;
        this.pickedAt = null;
        this.finishedAt = null;

        this.vehicle = null;

        this.takenSteps = 0;
        this.distance = Functions.calculateDistance(startIntersection, finishIntersection);
        this.started = false;
        this.finished = false;
    }

    public boolean iterateRide(int i)
    {
        if (started)
        {
            this.takenSteps++;
            this.vehicle.move(finishIntersection);
            if (takenSteps == distance)
            {
                this.finished = true;
                this.finishedAt = i;

                this.vehicle.setUsed(false);
                this.vehicle = null;
            }
        } else
        {
            boolean move = vehicle.move(startIntersection);

            if (i >= this.earliestStart && move)
            {
                this.started = true;
                this.pickedAt = i;
            }
        }

        return this.finished;
    }

    public void setBound(Vehicle vehicle)
    {
        this.vehicle = vehicle;
        this.vehicle.setUsed(true);
        this.vehicle.registerRide(this.id);
    }

    public Coordinate getStartIntersection()
    {
        return startIntersection;
    }

    public int getDistance()
    {
        return distance;
    }

    public Integer getFinishedAt()
    {
        return finishedAt;
    }

    public boolean isStarted()
    {
        return started;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public Integer getEarliestStart()
    {
        return earliestStart;
    }

    public Integer getLatestStart()
    {
        return latestStart;
    }

    public boolean isBound()
    {
        return vehicle != null;
    }

    public Integer getPickedAt()
    {
        return pickedAt;
    }

}

