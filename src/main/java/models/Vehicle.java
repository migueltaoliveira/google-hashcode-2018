package models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author migueloliveira
 */
public class Vehicle
{
    private Coordinate coordinate;
    private Boolean used;
    private List<Integer> rideList;

    public Vehicle()
    {
        this.coordinate = new Coordinate(0, 0);
        this.used = false;
        this.rideList = new ArrayList<>();
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public Boolean getUsed()
    {
        return used;
    }

    public void setUsed(Boolean used)
    {
        this.used = used;
    }

    public boolean move(Coordinate finish)
    {
        if (coordinate.getR() > finish.getR())
        {
            coordinate.decrementR();
        } else if (coordinate.getR() < finish.getR())
        {
            coordinate.incrementR();
        } else if (coordinate.getC() > finish.getC())
        {
            coordinate.decrementC();
        } else if (coordinate.getC() < finish.getC())
        {
            coordinate.incrementC();
        } else
        {
            return true;
        }

        return false;
    }

    public int getRideCount()
    {
        return this.rideList.size();
    }

    public void registerRide(Integer id)
    {
        this.rideList.add(id);
    }

    public List<Integer> getRideList()
    {
        return this.rideList;
    }

}
