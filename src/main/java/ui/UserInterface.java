package ui;

import java.io.IOException;

import java.sql.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

import controllers.*;

import core.*;

import data.DataLoader;
import data.DatabaseHandler;

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

		String path = "database.db";
		Path dbPath = Paths.get(path);

		if (! Files.exists(dbPath)) {
			try {
				DatabaseHandler.database(false);
				DataLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		while(!quit) {
			System.out.println();
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
			case 8: quit = true; break;
			default: System.out.println("Number must be 1-8"); continue;
			}
		}
		keyboard.close();
	}


	/**
	 * Registers ExerciseGroup
	 */
	private static void registerExerciseGroup() {
		Scanner keyboard = new Scanner(System.in);

		System.out.println("-----REGISTER EXERCISE GROUP----");
		System.out.println("Exercise group name: ");
		String name = null;

		while (name == null) {
			try {
				name = keyboard.nextLine();
			} catch (InputMismatchException e) {
				System.err.println("Input must be text!");
			}	
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

		while (input == 0) {
			try {
				input = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Input must be a number!");
			} finally {
				keyboard.nextLine();
			}
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
				System.out.println("Illegal exerciseID. Choose one from the list");
				exerciseID = -1;
			}
			catch (IllegalStateException e) {
				System.out.println("You have already registered this exercise to this workout. Duplicates not possible. Register a new exercise to enter more.");
				System.out.println("Select exercises that were a part of this workout. [1, 2, ...]. Enter 0 to end.");
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

	public static void addExerciseToWorkout(Workout workout, int exerciseID) throws IllegalArgumentException, IllegalStateException {

		Scanner keyboard = new Scanner(System.in);
		MachineExerciseDatabaseController medc = new MachineExerciseDatabaseController();
		FreeExerciseDatabaseController fedc = new FreeExerciseDatabaseController();
		PreparedStatement statement;
		// Find out what type the exerciseID is
		Exercise exercise = null;

        exercise = fedc.exerciseIDIsFreeExercise(exerciseID);
        if (exercise == null) {
            exercise = medc.exerciseIDIsMachineExercise(exerciseID);
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
		try {
			eddc.create(exerciseDone);
		}
		catch (SQLException e) {
			// Restriction of the database
			throw new IllegalStateException("Cannot select the same exerciseID twice in the same workout");
		}
	}


	/**
	 * Get information of workout based on what machine it was performed on
     * Print first all machines registered, max 100
     * User chose machine, by machine id
     * all workouts done on that machine is printed
     * 
	 */
	private static void viewWorkoutOnMachine() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();

		// List machines, showing ID
		System.out.println("----MACHINES----");
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");
			DBTablePrinter.printTable(conn, "machine", 9999, 120);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ask user to choose machine ID
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Choose machine ID: ");
		int machineID = 0;

		// Print workouts on the chosen machine
		while (machineID == 0) {
			try {
                machineID = keyboard.nextInt();
                wdc.retrieveWorkoutBasedOnMachineID(machineID);
            } catch (InputMismatchException e) {
                System.err.println("Input must be a number!");
            } finally {
			    keyboard.nextLine();
            }
		}
        ResultSetConnection rsConn
                = wdc.retrieveWorkoutBasedOnMachineID(machineID);
        DBTablePrinter.printResultSet(rsConn.getSet());
        try {
            rsConn.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
		} catch (InputMismatchException ignore) {
			// ignores since Y is default
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

			while (id < 1) {
				try {
					id = keyboard.nextInt();
				} catch (InputMismatchException e) {
					System.err.println("Input must be a positive number!");
				} finally {
					keyboard.nextLine();
				}
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

		while (input == 0) {
			try {
				input = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Input must be 1 or 2!");
			} finally {
				keyboard.nextLine();
      }
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
			
			System.out.println("MachineID: ");
			while (machineID == 0) {
				try {
					machineID = keyboard.nextInt();
				} catch (InputMismatchException e) {
					System.err.println("Input must be a positive number!");
				} finally {
					keyboard.nextLine();
				}
			}
			
			System.out.println("Sets: ");
			while (sets == 0) {
				try {
					sets = keyboard.nextInt();
				} catch (InputMismatchException e) {
					System.err.println("Input must be a number between 1 and 10!");
				} finally {
					keyboard.nextLine();
				}
			}
			
			System.out.println("Kilograms: ");
			while (kilograms == 0) {
			try {
					kilograms = keyboard.nextInt();
				} catch (InputMismatchException e) {
					System.err.println("Input must be a positive number!");
				} finally {
					keyboard.nextLine();
				}
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
	 * For some exercise, retrieve a result log for a given time interval
	 * where the end-points of the interval is specified by the user
	 */
	private static void viewResultLog() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Scanner keyboard = new Scanner(System.in);

		System.out.println("----VIEW RESULT LOG----");

		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:database.db");
			DBTablePrinter.printTable(conn, "exercise", 9999, 120);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Which exercise do you want to view log of: ");
		int id = 0;
		
		while (id == 0) {
			try {
				id = keyboard.nextInt();
			} catch (InputMismatchException e) {
				System.err.println("Input must be a number!");
			} finally {
				keyboard.nextLine();
			}
		}
		
		System.out.println("Start point of interval (format: yyyy-mm-dd hh:mm:ss)");
		String timestamp = "";
		Date start = null;
		boolean flag = true;

		while (flag) {
			try {
				timestamp = keyboard.nextLine();
				Calendar c = Calendar.getInstance();
				c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp));
			    start = new Date(c.getTimeInMillis());
				flag = false;
			} catch (ParseException e) {
				System.err.println("Illegal format! Must be (yyyy-MM-dd HH:mm:ss");
				flag = true;
			}
		}

		System.out.println("End point of interval (format: yyyy-MM-dd HH:mm:ss)");
		timestamp = "";
		Date end = null;
		flag = true;
		while (flag) {
			try {
				timestamp = keyboard.nextLine();
				Calendar c = Calendar.getInstance();
				c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp));
				end = new Date(c.getTimeInMillis());
				flag = false;
			} catch (ParseException e) {
				System.err.println("Illegal format! Must be (yyyy-MM-dd HH:mm:ss)");
				flag = true;
			}
		}

		ResultSetConnection rsConn
			= wdc.retrieveWorkoutBasedOnExercieAndTime(id, start, end);
		DBTablePrinter.printResultSet(rsConn.getSet());
		try {
			rsConn.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers Machine with associated data
     * user writes name for machine and description, and the machine will be saved in database
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
		String description = "";

		try {
			name = keyboard.nextLine();
            System.out.println("Describe the machine");
            description = keyboard.nextLine();
		} catch (InputMismatchException e) {
			System.err.println("Input must be text!");
		}
		machine = new Machine(name, description);

        machineDatabaseController.create(machine);
		System.out.println("Machine registered");
	}
}
