package codespace.traffic;

import java.util.ArrayList;
import java.util.Random;

/*
	Class CommonVars.
	This class will hold common variables
*/
public class CommonVars
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 100;

	public static final int MAX_SPEED = 6;
	public static final int MIN_SPEED = 1;

	public static final int MAX_CARS = 50;
	public static final int CAR_SIZE = 10;

	public static Random random = new Random();

	public static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	public static ArrayList<VehicleT> vehiclesT = new ArrayList<VehicleT>();

	public static int conflictV = 0;
	public static int conflictVT = 0;
}
