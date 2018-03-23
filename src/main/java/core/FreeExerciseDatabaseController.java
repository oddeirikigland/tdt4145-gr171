package core;

import java.sql.*;

public class FreeExerciseDatabaseController implements DatabaseCRUD {

    PreparedStatement statement;

    public int create(Object object) {
        FreeExercise exercise = (FreeExercise) isFreeExercise(object);
        String superSql = "INSERT INTO exercise "
                + "(name) "
                + "VALUES(?)";
        String subSql = "INSERT INTO free_exercise "
                    + "(exercise_id, description) "
                    + "VALUES(?, ?)";

        try {
            int id = -1;
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(superSql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, exercise.getName());

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

            Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = conn.prepareStatement(subSql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setString(2, exercise.getDescription());
            statement.executeUpdate();

            connection.close();
            return id; // returns ID for both exercise and free_exercise table
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public FreeExercise retrieve(int id) {
        String sql = "SELECT *" +
                "FROM exercise NATURAL JOIN free_exercise " +
                "WHERE exercise_id=?";

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            FreeExercise fe1;
            if (rs.next()) {
                fe1 = new FreeExercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                connection.close();
                return fe1;
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

    public FreeExercise exerciseIDIsFreeExercise(int exerciseID) {
        FreeExercise exercise = null;
        try {
            String sql = "SELECT exercise_id " +
                    "FROM free_exercise";
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("exercise_id") == exerciseID) {
                    exercise = retrieve(exerciseID);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return exercise;
    }


    private Object isFreeExercise(Object obj) {
        if (obj instanceof FreeExercise) {
            return obj;
        }
        else {
            throw new IllegalArgumentException("Object must be FreeExercise");
        }
    }
}
