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
					+ "(exercise_id, sets, kilo, machine_id) "
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


    public Object retrieve(int id) {
        String sql = "SELECT * " +
                    "FROM exercise NATURAL JOIN machine_exercise " +
                    "WHERE exercise_id=?";

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            MachineExercise me1;
            if (rs.next()) {
                Machine machine = new MachineDatabaseController().retrieve(rs.getInt("machine_id"));
                me1 = new MachineExercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getInt("kilo"),
                        rs.getInt("sets"),
                        machine
                );
                connection.close();
                return me1;
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public void update(Object object) {

    }

    @Deprecated
    public void delete(int id) {

    }

    private Object isMachineExercise(Object object) {
        if (object instanceof MachineExercise) {
            return object;
        } else {
            throw new IllegalArgumentException("Object must be a MachineExercise");
        }
    }
}
