package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import core.ExerciseDone;

public class ExerciseDoneDatabaseController {
	
	PreparedStatement statement;
	
	public void create(Object object) {
		ExerciseDone exerciseDone = (ExerciseDone) isExerciseDone(object);
		String sql = "INSERT INTO exercise_done "
					+ "(workout_id, exercise_id, duration) "
					+ "VALUES (?, ?, ?)";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.prepareStatement(sql);
			statement.setInt(1, exerciseDone.getWorkout().getWorkoutID());
			statement.setInt(2, exerciseDone.getExercise().getExerciseID());
			statement.setInt(3, exerciseDone.getDuration());
			statement.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Object isExerciseDone(Object obj) {
		if (obj instanceof ExerciseDone) {
			return obj;
		} else {
			throw new IllegalArgumentException("Object must be a ExerciseDone");
		}
	}

}
