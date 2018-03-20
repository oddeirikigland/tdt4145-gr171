package ui;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

import core.*;
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
			case 6: registerExerciseGroup(); break;
			case 7: viewWorkoutOnMachine(); break;
			case 9: viewMachineExercises(); break;
			case 8: quit = true; break;
			default: System.out.println("Number must be 1-8"); continue;
			}
		}
		keyboard.close();
	}

	private static void viewMachineExercises() {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");
			DBTablePrinter.printTable(conn, "machine_exercise");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Registers ExerciseGroup
	 */
	private static void registerExerciseGroup() {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("-----REGISTER EXERCISE GROUP----");
		System.out.println("Exercise group name: ");
		String name = null;

		try {
			name = keyboard.nextLine();
		} catch (InputMismatchException e) {
			System.err.println("Input must be text!");
		}

		ExerciseGroup eg = new ExerciseGroup(name);
		ExerciseGroupDatabaseController egdc = new ExerciseGroupDatabaseController();
		eg = new ExerciseGroup(egdc.create(eg), eg.getName());
		if (eg.getExerciseGroupID() != -1) {
			System.out.println("Successfully created exercise group " + name);
		} else {
			System.err.println("Something went wrong...");
		}
		System.out.println();
	}

	/**
	 * Get information about the n-previous Workouts with their notes, 
	 * based on input from user
	 */
	private static void viewWorkouts() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("-----VIEW WORKOUT LOG----");
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
	 * Registers Workout with associated data
	 */
	private static void registerWorkout() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("----REGISTER WORKOUT----");
		System.out.println("When was this workout? (format: yyyy-mm-dd hh:mm");

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
				keyboard.nextLine();
				System.out.println("Wrong format. Must be a number. Please enter again.");
				duration = -1;
			}
		}
		// The system asks for duration in minutes, but expects seconds
		duration = duration * 60;

		System.out.println("What was your form? (format: nn [number 1-10])");

		int form = -1;
		while (form == -1) {
			try {
				form = keyboard.nextInt();
				if (form < 1 || form > 10) {
					// Clear the scanner
					System.out.println("Invalid number. Must be between 1 and 10");
					form = -1;
				}
			}
			catch (InputMismatchException e) {
				// Clear the Scanner
				keyboard.nextLine();
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
				// Clear the Scanner
				keyboard.nextLine();
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

		int exerciseID = -1;
		while (exerciseID != 0) {
			while (exerciseID == -1) {
				try {
					exerciseID = keyboard.nextInt();
					if (exerciseID == 0) {
						break;
					}
				} catch (InputMismatchException e) {
					// Clear the Scanner
					keyboard.nextLine();
					System.out.println("Wrong format. Must be a number. Please enter again.");
					exerciseID = -1;
				}
			}
			try {
				addExerciseToWorkout(newWorkout, exerciseID);
			}
			catch (IllegalArgumentException e) {
				System.out.println("Illegal workoutID. Choose one from the list");
				exerciseID = -1;
			}

			if (exerciseID != -1) {
				System.out.println("Exercise registered. Enter a new exerciseID, or enter 0 to exit to main menu.");
			}

			try {
				exerciseID = keyboard.nextInt();
			}
			catch (InputMismatchException e) {
				// Clear the Scanner
				keyboard.nextLine();
                System.out.println("Wrong format. Must be a number. Please enter again.");
                exerciseID = -1;
			}
		}
	}

	public static void addExerciseToWorkout(Workout workout, int exerciseID) throws IllegalArgumentException {

		Scanner keyboard = new Scanner(System.in);
		MachineExerciseDatabaseController medc = new MachineExerciseDatabaseController();
		FreeExerciseDatabaseController fedc = new FreeExerciseDatabaseController();
		PreparedStatement statement;
		// Find out what type the exerciseID is
		Exercise exercise = null;

        try {
			String sql = "SELECT exercise_id " +
					"FROM free_exercise";
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				if (rs.getInt("exercise_id") == exerciseID)  {
					exercise = fedc.retrieve(exerciseID);
				}
			}
			if (exercise == null) {
				sql = "SELECT exercise_id " +
						"FROM machine_exercise";
				statement = connection.prepareStatement(sql);
				rs = statement.executeQuery();

				while (rs.next()) {
					if (rs.getInt("exercise_id") == exerciseID) {
						exercise = medc.retrieve(exerciseID);
					}
				}
			}
		}
		catch (SQLException e) {
        	e.printStackTrace();
		}

		if (exercise == null) {
        	throw new IllegalArgumentException("The database does not have a exercise with this exerciseID");
		}

		System.out.println("How long did you do this exercise?");

		int duration = -1;
		while (duration == -1) {
			try {
				duration = keyboard.nextInt();
			}
			catch (InputMismatchException e) {
				// Clear the Scanner
				keyboard.nextLine();
				System.out.println("Wrong format. Must be a number. Please enter again");
				duration = -1;
			}
		}

		duration = duration * 60;

		ExerciseDoneDatabaseController eddc = new ExerciseDoneDatabaseController();
		ExerciseDone exerciseDone = new ExerciseDone(duration, workout, exercise);
		eddc.create(exerciseDone);
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
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("----REGISTER EXERCISE----");
		System.out.println("Do you want to register the exercise to a group?(Y/n)\n");
		String input = "Y";

		try {
			input = keyboard.nextLine();
		} catch (InputMismatchException e) {
			System.err.println("Input must be a character!");
		}

		if (!input.toLowerCase().equals("n")) {
			// link Exercise to an ExerciseGroup
			Connection conn;
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:database.db");
				DBTablePrinter.printTable(conn, "exercise_group", 9999, 120);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.out.println("Select the ID of the ExerciseGroup to register new Exercise to: ");
			int id = 0;

			try {
				id = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Input must be a number!");
			}
			registerExercise(id);
			return;
		}
		// register exercise without a group
		registerExercise(0);
	}

	private static void registerExercise(int id) {
		Scanner keyboard = new Scanner(System.in);
		IncludesDatabaseController idc = new IncludesDatabaseController();

		System.out.println("Do you want to register a (1) FreeExercise or a  (2) MachineExercise? ");
		int input = 0;

		try {
			input = keyboard.nextInt();
			keyboard.nextLine();
		} catch (InputMismatchException e) {
			System.err.println("Input must be a number!");
		}

		System.out.println("Name of exercise: ");
		String name;
		name = keyboard.nextLine();

		if (input == 1) {
			// register FreeExercise
			System.out.println("Description of exercise: ");
			String desc = keyboard.nextLine();

			FreeExerciseDatabaseController fedc = new FreeExerciseDatabaseController();
			FreeExercise freeExercise = new FreeExercise(name, desc);
			fedc.create(freeExercise);

			if (id != 0) {
				idc.create(id, freeExercise);
			}
		}
		else if (input == 2) {
			// register MachineExercise
			Connection conn;
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:database.db");
				DBTablePrinter.printTable(conn, "machine", 99999, 120);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			int machineID = 0;
			int sets = 0;
			int kilograms = 0;

			try {
				System.out.println("MachineID: ");
				machineID = keyboard.nextInt();
				System.out.println("Sets: ");
				sets = keyboard.nextInt();
				System.out.println("Kilograms: ");
				kilograms = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Input must be a number!");
			}
			MachineDatabaseController mdc = new MachineDatabaseController();
			Machine machine = mdc.retrieve(machineID);
			MachineExerciseDatabaseController medc = new MachineExerciseDatabaseController();
			MachineExercise exercise
				= new MachineExercise(
					name,
					kilograms,
					sets,
					machine
				);
			medc.create(exercise);

			if (id != 0) {
				idc.create(id, exercise);
			}
		}
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
