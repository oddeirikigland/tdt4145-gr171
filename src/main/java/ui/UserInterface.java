package ui;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

import core.DatabaseHandler;
import core.Workout;
import core.WorkoutDatabaseController;
import data.DataLoader;
import net.efabrika.util.DBTablePrinter;

public class UserInterface {
	
	public static void main(String[] args) {
		//startInterface();
		printExercises();
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
			DBTablePrinter.printResultSet(wdc.getNWorkouts(input));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers Workout with associated data
	 */
	private static void registerWorkout() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("When was this exercise? (format: yyyy-mm-dd hh:mm");

		String timestamp = "";
		Date convertedCurrentDate = null;
		while (timestamp.equals("")) {
			try {
				timestamp = keyboard.nextLine();
				Calendar c = Calendar.getInstance();
				c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timestamp));
				convertedCurrentDate = new Date(c.getTimeInMillis());
			}
			catch (ParseException e) {
				System.out.println("Wrong format. Please enter again as yyyy-mm-dd hh:mm");
				timestamp = "";
			}
		}

		System.out.println("What was the duration? (format: nn [minutes])");

		int duration = -1;
		while (duration == -1) {
			try {
				duration = keyboard.nextInt();
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong format. Must be a number. Please enter again.");
				duration = -1;
			}
		}

		System.out.println("What was your form? (format: nn [number 1-10])");

		int form = -1;
		while (form == -1) {
			try {
				form = keyboard.nextInt();
				if (form < 1 || form > 10) {
					System.out.println("Invalid number. Must be between 1 and 10");
					form = -1;
				}
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong format. Must be a number. Please enter again.");
				form = -1;
			}
		}

		System.out.println("What was your performance? (format: nn [number 1-10])");

		int performance = -1;
		while (performance == -1) {
			try {
				performance = keyboard.nextInt();
				if (performance < 1 || performance > 10) {
					System.out.println("Invalid number. Must be between 1 and 10");
					performance = -1;
				}
			}
			catch (InputMismatchException e ) {
				System.out.println("Wrong format. Must be a number. Please enter again");
				performance = -1;
			}
		}

		System.out.println("Please enter a one-line note. End with return.");

		String note = "";
		while (note.length() == 0) {
			note = keyboard.nextLine();
		}

		Workout newWorkout = new Workout(convertedCurrentDate, duration, form, performance, note);
		int workoutID = wdc.create(newWorkout);
		newWorkout.setWorkoutID(workoutID);


		DBTablePrinter tablePrinter = new DBTablePrinter();
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			tablePrinter.printTable(connection, "exercise");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Select exercises that were a part of this workout. [1, 2, ...]. Enter 0 to end.");

		int input = -1;
		while (input != 0) {
			while (input == -1) {
				try {
					input = keyboard.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("Wrong format. Must be a number. Please enter again.");
					input = -1;
				}

			}
			try {
				addExerciseToWorkout(workoutID, input);
			}
			catch (IllegalArgumentException e) {
				System.out.println("Illegal workoutID. Choose one from the list");
				input = -1;
			}


		}

	}

	public static void addExerciseToWorkout(int workoutID, int exerciseID) throws IllegalArgumentException {

		Scanner keyboard = new Scanner(System.in);
		System.out.println("How long did you do this exercise?");

		int duration = -1;
		while (duration == -1) {
			try {
				duration = keyboard.nextInt();
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong format. Must be a number. Please enter again");
				duration = -1;
			}
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
	 * Registers Machine with associated data
	 */
	private static void registerMachine() {
		// TODO Auto-generated method stub
		
	}
}
