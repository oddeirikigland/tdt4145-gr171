package core;

public class ExerciseDone {
	
	private int duration;
	private Workout workout;
	private Exercise exercise;
	
	public ExerciseDone(int duration, Workout workout, Exercise exercise) {
		this.duration = duration;
		this.workout = workout;
		this.exercise = exercise;
	}
	
	public Workout getWorkout() {
		return workout;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public Exercise getExercise() {
		return exercise;
	}
}
