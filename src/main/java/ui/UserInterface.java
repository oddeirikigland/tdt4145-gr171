package ui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

import core.DatabaseHandler;
import core.ExerciseGroup;
import core.ExerciseGroupDatabaseController;
import core.FreeExercise;
import core.FreeExerciseDatabaseController;
import core.IncludesDatabaseController;
import core.Machine;
import core.MachineDatabaseController;
import core.MachineExercise;
import core.MachineExerciseDatabaseController;
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
