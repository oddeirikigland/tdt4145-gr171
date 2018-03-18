package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MachineExerciseDatabaseController {
	
	PreparedStatement statement;
	
	   public int create(Object object) {
	        MachineExercise exercise = (MachineExercise) isMachineExercise(object);
	        String superSql = "INSERT INTO exercise "
	                + "(name) "
	                + "VALUES(?)";
	        String subSql = "INSERT INTO machine_exercise "
	                    + "(exercise_id, sets, kilograms, machine_id) "
	                    + "VALUES(?, ?, ?, ?)";

	        try {
	            int i = -1;
	            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
	            statement = connection.prepareStatement(superSql, PreparedStatement.RETURN_GENERATED_KEYS);
	            statement.setInt(1, exercise.getExerciseID());
	            statement.setString(2, exercise.getName());
	            statement.executeUpdate();

	            try {
	                // Retrieves all generated keys, and returns the key of the newly inserted
	                // row. Assumes the row only has one key
	                ResultSet generatedKeys = statement.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    i = Math.toIntExact(generatedKeys.getLong(1));
	                }

	            }
	            catch (SQLException e) {
	                e.printStackTrace();
	            }

	            statement = connection.prepareStatement(subSql, PreparedStatement.RETURN_GENERATED_KEYS);
	            statement.setInt(1, i);
	            statement.setInt(2, exercise.getSets());
	            statement.setInt(3, exercise.getKilograms());
	            statement.setInt(4, exercise.getMachine().getMachineID());
	            statement.executeUpdate();
	            connection.close();
	            return i;
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return -1;
	    }	

	    private Object isMachineExercise(Object obj) {
	        if (obj instanceof MachineExercise) {
	            return obj;
	        }
	        else {
	            throw new IllegalArgumentException("Object must be MachineExercise");
	        }
	    }
}
