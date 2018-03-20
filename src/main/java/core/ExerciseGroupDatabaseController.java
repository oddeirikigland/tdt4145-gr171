package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.javafx.collections.MappingChange.Map;

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
	
    private Object isExerciseGroup(Object obj) {
        if (obj instanceof ExerciseGroup) {
            return obj;
        }
        else {
            throw new IllegalArgumentException("Object must be ExerciseGroup");
        }
    }
}
