package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.javafx.collections.MappingChange.Map;

import core.ExerciseGroup;

public class ExerciseGroupDatabaseController {
	
	PreparedStatement statement;
	
	public int create(Object object) {
		ExerciseGroup eg = (ExerciseGroup) isExerciseGroup(object);
		String sql = "INSERT INTO exercise_group "
				   + "(name) "
				   + "VALUES (?)";
		try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, eg.getName());
            statement.executeUpdate();

            try {
                // Retrieves all generated keys, and returns the key of the newly inserted
                // row. Assumes the row only has one key
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = Math.toIntExact(generatedKeys.getLong(1));
                    connection.close();
                    return id;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            } 
            connection.close();
            
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public ResultSetConnection retrieveExercises(int id) {
		String sql = "SELECT e.name "
					+ "FROM exercise as e "
					+ "JOIN includes as i on (i.exercise_id = e.exercise_id) "
					+ "JOIN exercise_group as eg on (eg.exercise_group_id = i.exercise_group_id) "
					+ "WHERE i.exercise_group_id=?";
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet rs = statement.executeQuery();
			return new ResultSetConnection(rs, connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    private Object isExerciseGroup(Object obj) {
        if (obj instanceof ExerciseGroup) {
            return obj;
        }
        else {
            throw new IllegalArgumentException("Object must be ExerciseGroup");
        }
    }
}
