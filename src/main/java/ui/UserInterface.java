package ui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import core.DatabaseHandler;
import core.Machine;
import core.MachineDatabaseController;
import core.WorkoutDatabaseController;
import data.DataLoader;
import net.efabrika.util.DBTablePrinter;

public class UserInterface {
	
	public static void main(String[] args) {
		startInterface();
	}
	
	/**
	 * Loads up CLI and asks user for input, performing actions based on that
	 */
	public static void startInterface() {
		int input = 0;
		Scanner keyboard = new Scanner(System.in);
		boolean quit = false;
		
		try {
			DatabaseHandler.database(true);
			DatabaseHandler.database(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DataLoader.load();

		while(!quit) {
			System.out.println("1: Register machine\n"
							+ "2: Register exercise\n"
							+ "3: Register workout\n"
							+ "4: Load previous workouts\n"
							+ "5: View result-log\n"
							+ "6: Register exercise group\n"
							+ "7: Get workout based on machine\n"
							+ "8: Quit\n");
			try {
				input = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Input must be a number!");
				continue;
			} 

			switch (input) {
			case 1: registerMachine();  break;
			case 2: registerExercise(); break;
			case 3: registerWorkout(); break;
			case 4: viewWorkouts(); break;
			case 5: viewResultLog(); break;
			case 6: registerExercise(); break;
			case 7: viewWorkoutOnMachine(); break;
			case 8: quit = true; break;
			default: System.out.println("Number must be 1-8"); continue;
			}
		}
		keyboard.close();
	}

	/**
	 * Get information about the n-previous Workouts with their notes, 
	 * based on input from user
	 */
	private static void viewWorkouts() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("How many workouts do you want to see?\n");
		int input = 0;

		try {
			input = keyboard.nextInt();
		} catch (InputMismatchException e) {
			System.err.println("Input must be a number!");
		} 

		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");
			DBTablePrinter.printTable(conn, "workout", input, 120);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get information of workout based on what machine it was performed on
	 */
	private static void viewWorkoutOnMachine() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Registers Exercise with associated data
	 */
	private static void registerExercise() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * For every exercise, retrieve a result log for a given time interval
	 * where the end-points of the interval is specified by the user
	 */
	private static void viewResultLog() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Registers Workout with associated data
	 */
	private static void registerWorkout() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Registers Machine with associated data
     * user writes name for machine, and the machine will be saved in database
     *
     * @author OE
	 */
	private static void registerMachine() {
		// TODO Auto-generated method stub
		MachineDatabaseController machineDatabaseController = new MachineDatabaseController();
		Machine machine;
		Scanner keyboard = new Scanner(System.in);

		System.out.println("What's the name of the machine\n");
		String name = "";
		//String description = "";

		try {
			name = keyboard.nextLine();
            //System.out.println("Describe the machine");
            //description = keyboard.nextLine();
		} catch (InputMismatchException e) {
			System.err.println("Input must be text!");
		}
		machine = new Machine(name); //,description);

        machineDatabaseController.create(machine);


	}
}
