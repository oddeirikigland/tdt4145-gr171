package data;

import java.sql.Date;

import core.ExerciseDone;
import core.ExerciseDoneDatabaseController;
import core.ExerciseGroup;
import core.ExerciseGroupDatabaseController;
import core.FreeExercise;
import core.FreeExerciseDatabaseController;
import core.Machine;
import core.MachineDatabaseController;
import core.Workout;
import core.WorkoutDatabaseController;

public class DataLoader {
	
	public static void load() {
		createWorkouts();
		createMachines();
		createExerciseGroups();
		createExercises();
	}

	private static void createExercises() {
		FreeExerciseDatabaseController fedc = new FreeExerciseDatabaseController();
		WorkoutDatabaseController wdc = new WorkoutDatabaseController();
		ExerciseDoneDatabaseController eddc = new ExerciseDoneDatabaseController();

		FreeExercise freeExercise = new FreeExercise("Some name", "Some desc");
		freeExercise = new FreeExercise(fedc.create(freeExercise), freeExercise.getName(), freeExercise.getDescription());
		Workout workout = wdc.retrieve(1);
		ExerciseDone exerciseDone = new ExerciseDone(3600, workout, freeExercise);
		eddc.create(exerciseDone);
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
		mdc.create(new Machine("Some machine"));	
	}
	
	private static void createExerciseGroups() {
		ExerciseGroupDatabaseController egdc = new ExerciseGroupDatabaseController();
		egdc.create(new ExerciseGroup("SomeExerciseGroup"));
	}
}
