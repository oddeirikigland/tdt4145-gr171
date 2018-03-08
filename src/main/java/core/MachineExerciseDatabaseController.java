package core;

import java.sql.*;

public class MachineExerciseDatabaseController implements DatabaseCRUD {

    PreparedStatement statement;

    public int create(Object object) {
        MachineExercise me1 = (MachineExercise) isMachineExercise(object);
        String sql =    "INSERT INTO exercise" +
                        "(name)" +
                        "VALUES (?)";
        try {
            int id = -1;
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            /* For Exercise */
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, me1.getName());
            statement.executeUpdate();

            try {
                // Retrieves all generated keys and returns the ID obtained by java object
                // which is inserted into the database
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    id = Math.toIntExact(generatedKeys.getLong(1));
                    connection.close();
                    // return i;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /* For MachineExercise */
            sql = "INSERT INTO machine_exercise" +
                    "(exercise_id, kilo, sets, machine_id)" +
                    "VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, me1.getKilograms());
            statement.setInt(3, me1.getSets());
            statement.setInt(4, me1.getMachine().getMachineID());
            connection.close();
            return id; // returns ID for both exercise and machine_exercise table
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object retrieve(int id) {
        String sql = "SELECT *" +
                    "FROM exercise NATURAL JOIN machine_exercise" +
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
