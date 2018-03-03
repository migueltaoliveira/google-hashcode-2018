import models.Coordinate;
import utils.Constants;
import utils.Functions;
import models.Ride;
import models.Vehicle;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class SelfDrivingRides
{
    private static Map<Integer, Ride> rideHash; // only for score purpose

    private static List<Vehicle> vehicleList;
    private static List<Ride> rideList;
    private static List<Ride> boundRides;

    public static void main(String [] args) throws IOException
    {
        if (args.length != 2)
        {
            System.out.println("Usage: SelfDrivingRides <input-file> <output-file>");
            return;
        }

        // args[1] = input file; args[2] = output file
        String input = args[0];
        String output = args[1];

        // Initialize
        rideHash = new HashMap<>();

        vehicleList = new ArrayList<>();
        rideList = new ArrayList<>();

        fileReader(input);
        findBestSolution();
        generateOutput(output);
    }

    // Read the input and fills all the models
    public static void fileReader(String path)
    {
        String thisLine = null;
        BufferedReader br = null;

        try
        {
            br = new BufferedReader(new FileReader(path));
            thisLine = br.readLine();

            String[] split = thisLine.split(" ");

            int F, N;

            // ignore the R (rows) and the C (columns)
            F = Integer.parseInt(split[2]);
            N = Integer.parseInt(split[3]);
            Constants.BONUS = Integer.parseInt(split[4]);
            Constants.STEPS = Integer.parseInt(split[5]);

            for (int i = 0; i < F; i++)
            {
                vehicleList.add(new Vehicle());
            }

            for (int i = 0; i < N; i++)
            {
                thisLine = br.readLine();

                String [] readRide = thisLine.split(" ");

                int a, b, x, y, s, f;

                a = Integer.parseInt(readRide[0]);
                b = Integer.parseInt(readRide[1]);
                x = Integer.parseInt(readRide[2]);
                y = Integer.parseInt(readRide[3]);
                s = Integer.parseInt(readRide[4]);
                f = Integer.parseInt(readRide[5]);

                Coordinate startIntersection = new Coordinate(a, b);
                Coordinate finishIntersection = new Coordinate(x, y);

                Ride ride = new Ride(startIntersection, finishIntersection, s, f, i);
                rideHash.put(i, ride);
                rideList.add(ride);
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e)
                {
                    // ignore
                }
            }
        }
    }

    public static void findBestSolution()
    {
        boundRides = new ArrayList<>();

        // Bind vehicles with ride
        for (int i = 0; i < Constants.STEPS; i++)
        {
            Stream<Vehicle> vehicleStream = vehicleList.stream().filter(vehicle -> !vehicle.getUsed());

            // Bind ride for each vehicle
            final int step = i;
            vehicleStream.forEach(vehicle ->
            {
                Optional<Ride> availableRide = getAvailableRides(step, vehicle.getCoordinate());

                if (availableRide.isPresent())
                {
                    Ride ride = availableRide.get();

                    ride.setBound(vehicle);
                    boundRides.add(ride);
                }
            });

            for (Iterator<Ride> it = boundRides.iterator(); it.hasNext(); )
            {
                Ride ride = it.next();
                boolean rideFinished = ride.iterateRide(i);

                if (rideFinished)
                {
                    it.remove();
                    rideList.remove(ride);
                }
            }

            if (step % ((double)Constants.STEPS/100) == 0)
            {
                System.out.println(((double) step / Constants.STEPS) * 100.0 + " %");
            }
        }
    }

    private static Optional<Ride> getAvailableRides(final int step, final Coordinate coordinate)
    {
        // Only consider the rides not started and not bound by any vehicle and
        // the rides that are impossible to deliver at the expected time
        Stream<Ride> rideStream = rideList.stream().filter(ride ->
            !ride.isStarted() && !ride.isBound() &&
                (ride.getLatestStart() - ride.getEarliestStart()) >= ride.getDistance() &&
                (ride.getLatestStart() >= (step + ride.getDistance() + Functions.calculateDistance(ride.getStartIntersection(), coordinate))));

        double max = - Double.MAX_VALUE;
        Ride bestRide = null;

        for (Iterator<Ride> iterator = rideStream.iterator(); iterator.hasNext();)
        {
            Ride ride = iterator.next();

            double function = Functions.targetFunction(ride, coordinate, step);

            if (function > max)
            {
                max = function;
                bestRide = ride;
            }
        }

        return Optional.ofNullable(bestRide);
    }

    public static void generateOutput(String path) throws FileNotFoundException
    {
        StringBuilder solution = new StringBuilder();
        int points = 0;

        for (Vehicle vehicle : vehicleList)
        {
            for (Integer integer : vehicle.getRideList())
            {
                Ride ride = rideHash.get(integer);

                if (ride.isFinished())
                {
                    points += ride.getFinishedAt() <= ride.getLatestStart() ? (ride.getDistance() +  (ride.getPickedAt().intValue() == ride.getEarliestStart().intValue() ? Constants.BONUS : 0)) : 0;
                }
            }

            solution.append(vehicle.getRideCount()).append(" ").append(vehicle.getRideCount() == 0 ? "" : vehicle.getRideList().stream().map(String::valueOf).reduce((a, b) -> a.concat(" ").concat(b)).get()).append("\n");
        }

        System.out.println("Total points [file = " + path + "] : " + points);
        fileWriter(path, solution.toString());
    }

    public static void fileWriter(String path, String solution) throws FileNotFoundException
    {
        try (PrintWriter out = new PrintWriter(path))
        {
            out.print(solution);
        }
    }

}
