package core;

import net.efabrika.util.DBTablePrinter;

import java.sql.Connection;
import java.sql.Date;
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
                    + "(timestamp, duration, form, performance, notes) "
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

   public ResultSet getNWorkouts(int n) { 
	   String sql = "SELECT * "
					 + "FROM workout LIMIT ?";
	   try {
		   Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
		   statement = connection.prepareStatement(sql);
		   statement.setInt(1, n);
		   ResultSet rs = statement.executeQuery();
		   connection.close();
		   return rs;
		   
	   } catch (SQLException e) {
		   e.printStackTrace();
	   }
	   return null;
   }
   
    public ResultSetConnection retrieveWorkoutBasedOnExercieAndTime
   									(int exerciseID, Date start, Date end) {
		String sql = "SELECT ed.duration, w.form, w.performance, w.notes "
				   + "FROM workout as w "
				   + "JOIN exercise_done as ed on (ed.workout_id = w.workout_id) "
				   + "JOIN exercise as e on (e.exercise_id = ed.exercise_id) "
				   + "WHERE (e.exercise_id=?) "
				   + "and (w.timestamp BETWEEN ? AND ?)";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			statement = connection.prepareStatement(sql);
			statement.setInt(1, exerciseID);
			statement.setDate(2, start);
			statement.setDate(3, end);
			ResultSet rs = statement.executeQuery();
			
			ResultSetConnection rsConn = new ResultSetConnection(rs, connection);
			return rsConn;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
   }
    
	public Workout retrieve(int id) {
        String sql = "SELECT * "
        			+ "FROM workout "
        			+ "WHERE workout_id=?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            Workout w1;
            if (rs.next()) {
            	w1 = new Workout(
            			rs.getInt("workout_id"),
            			rs.getDate("timestamp"),
            			rs.getInt("duration"),
            			rs.getInt("form"),
            			rs.getInt("performance"),
            			rs.getString("notes")
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
        			+ "SET timestamp=?, duration=?, form=?, performance=?, notes=? "
        			+ "WHERE workout_id=?";
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
					+ "WHERE workout_id=?";
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

    public ResultSetConnection retrieveWorkoutBasedOnMachineID(int machineID) {
        String sql = "SELECT * "
                + "FROM workout "
                + "WHERE workout_id IN " +
                "(SELECT workout_id " +
                "FROM (machine NATURAL JOIN machine_exercise) " +
                "NATURAL JOIN exercise_done " +
                "WHERE machine_id=?)";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, machineID);
            ResultSet rs = statement.executeQuery();

            ResultSetConnection rsConn = new ResultSetConnection(rs, connection);
            return rsConn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private Object isWorkout(Object obj) {
		if (obj instanceof Workout) {
			return obj;
		} else {
			throw new IllegalArgumentException("Object must be a Workout");
		}
	}
}
