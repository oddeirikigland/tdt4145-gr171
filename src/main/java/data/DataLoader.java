package data;

import java.sql.Date;
import java.sql.SQLException;


import controllers.*;
import core.ExerciseDone;
import core.ExerciseGroup;
import core.FreeExercise;
import core.Machine;
import core.Workout;
import core.MachineExercise;
import core.Exercise;

public class DataLoader {
	
	public static void load() {
		createWorkouts();
		createMachines();
		createExerciseGroups();
		createWorkoutBasedOnMachineData();
		createExercises();
	}

	private static void createExercises() {
		FreeExerciseDatabaseController fedc = new FreeExerciseDatabaseController();
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		ExerciseDoneDatabaseController eddc = new ExerciseDoneDatabaseController();
		IncludesDatabaseController idc = new IncludesDatabaseController();

		FreeExercise freeExercise = new FreeExercise("Some name", "Some desc");
		freeExercise = new FreeExercise(fedc.create(freeExercise), freeExercise.getName(), freeExercise.getDescription());
		Workout workout = wdc.retrieve(1);
		ExerciseDone exerciseDone = new ExerciseDone(3600, workout, freeExercise);

		try {
			eddc.create(exerciseDone);
			idc.create(1, freeExercise);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void createWorkouts() {
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		Workout w1, w2, w3, w4, w5;
		
		w1 = new Workout(new Date(1520539200L * 1000), 3600, 6, 7, "Good warmup");
		w1 = new Workout(wdc.create(w1), w1.getTimestamp(), w1.getDuration(), w1.getForm(), w1.getPerformance(), w1.getNote());
		
		w2 = new Workout(new Date(1520352000L * 1000), 5400, 5, 6, "Went for a long run");
		w2 = new Workout(wdc.create(w2), w2.getTimestamp(), w2.getDuration(), w2.getForm(), w2.getPerformance(), w2.getNote());
		
		w3 = new Workout(new Date(1520085600L * 1000), 3600, 5, 6, "Leg day!");
		w3 = new Workout(wdc.create(w3), w3.getTimestamp(), w3.getDuration(), w3.getForm(), w3.getPerformance(), w3.getNote());
		
		w4 = new Workout(new Date(1529920000L * 1000), 10800, 8, 9, "Another long run!");
		w4 = new Workout(wdc.create(w4), w4.getTimestamp(), w4.getDuration(), w4.getForm(), w4.getPerformance(), w4.getNote());
		
		w5 = new Workout(new Date(1529394400L * 1000), 1800, 8, 8, "Quick Yoga session");
		w5 = new Workout(wdc.create(w5), w5.getTimestamp(), w5.getDuration(), w5.getForm(), w5.getPerformance(), w5.getNote());
	}
	
	private static void createMachines() {
		MachineDatabaseController mdc = new MachineDatabaseController();
		mdc.create(new Machine("Some machine", "Machine Description"));
	}
	
	private static void createExerciseGroups() {
		ExerciseGroupDatabaseController egdc = new ExerciseGroupDatabaseController();
		egdc.create(new ExerciseGroup("SomeExerciseGroup"));
	}

	private static void createWorkoutBasedOnMachineData() {
	    MachineDatabaseController mdc = new MachineDatabaseController();
	    int machine_id = mdc.create(new Machine("nedtrekkStativ", "trekk ned stanga"));
	    Machine machine = mdc.retrieve(machine_id);

	    MachineExerciseDatabaseController medc = new MachineExerciseDatabaseController();
	    int machineExercise_id = medc.create(new MachineExercise("nedtrekk",200, 3, machine));
	    Exercise exercise = (Exercise) medc.retrieve(machineExercise_id);

	    WorkoutDatabaseController wdc = new WorkoutDatabaseController();
	    int workout_id = wdc.create(new Workout(new Date(1619394400L), 2000, 3, 5, "Boling"));
	    Workout workout = wdc.retrieve(workout_id);

        ExerciseDoneDatabaseController eddc = new ExerciseDoneDatabaseController();
        try {
			eddc.create(new ExerciseDone(34, workout, exercise));
		}
		catch (SQLException e){
        	e.printStackTrace();
		}


    }
}
