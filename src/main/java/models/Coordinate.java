package models;

/**
 * @author migueloliveira
 */
public class Coordinate
{
    private Integer r;
    private Integer c;

    public Coordinate(Integer r, Integer c)
    {
        this.r = r;
        this.c = c;
    }

    public Integer getR()
    {
        return r;
    }

    public Integer getC()
    {
        return c;
    }

    public void incrementR()
    {
        this.r++;
    }

    public void incrementC()
    {
        this.c++;
    }

    public void decrementR()
    {
        this.r--;
    }

    public void decrementC()
    {
        this.c--;
    }
}
