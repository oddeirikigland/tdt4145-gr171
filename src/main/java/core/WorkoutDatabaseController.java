package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.lang.Math;

public class WorkoutDatabaseController implements DatabaseCRUD {

    PreparedStatement statement;

    public int create(Object object) {
    	Workout w1 = (Workout) isWorkout(object);
        String sql = "INSERT INTO workout "
                    + "(timestamp, duration, form, performance, note) "
                    + "VALUES (?, ?, ?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setDate(1, w1.getTimestamp());
            statement.setInt(2, w1.getDuration());
            statement.setInt(3, w1.getForm());
            statement.setInt(4, w1.getPerformance());
            statement.setString(5, w1.getNote());
            statement.executeUpdate();

            try {
                // Retrieves all generated keys and returns the ID obtained by java object
                // which is inserted into the database
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                	int i = Math.toIntExact(generatedKeys.getLong(1));
                	connection.close();
                    return i;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        }

       return -1;
    }

	public Workout retrieve(int id) {
        String sql = "SELECT * "
        			+ "FROM workout "
        			+ "WHERE workoutID=?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            Workout w1;
            if (rs.next()) {
            	w1 = new Workout(
            			rs.getInt("workoutID"),
            			rs.getDate("timestamp"),
            			rs.getInt("duration"),
            			rs.getInt("form"),
            			rs.getInt("performance"),
            			rs.getString("note")
            		);
            	connection.close();
            	return w1;
            }
            connection.close();
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return null;
	}

	public void update(Object object) {
		Workout w1 = (Workout) isWorkout(object);
		String sql = "UPDATE workout "
        			+ "SET timestamp=?, duration=?, form=?, performance=?, note=? "
        			+ "WHERE workoutID=?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setDate(1, w1.getTimestamp());
            statement.setInt(2, w1.getDuration());
            statement.setInt(3, w1.getForm());
            statement.setInt(4, w1.getPerformance());
            statement.setString(5, w1.getNote());
            statement.setInt(6, w1.getWorkoutID());
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}

	public void delete(int id) {
		String sql = "DELETE FROM workout "
					+ "WHERE workoutID=?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.close();
            
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	private Object isWorkout(Object obj) {
		if (obj instanceof Workout) {
			return obj;
		} else {
			throw new IllegalArgumentException("Object must be a Workout");
		}
	}

}
