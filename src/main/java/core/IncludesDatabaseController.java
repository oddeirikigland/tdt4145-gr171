package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class IncludesDatabaseController {
	
	PreparedStatement statement;
	
	public void create(int exerciseGroupID, Exercise exercise) {
		String sql = "INSERT INTO includes "
					+ "(exercise_group_id, exercise_id) "
					+ "VALUES (?, ?)";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.prepareStatement(sql);
			statement.setInt(1, exerciseGroupID);
			statement.setInt(2, exercise.getExerciseID());
			statement.executeUpdate();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
