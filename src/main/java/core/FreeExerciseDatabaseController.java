package core;

import java.sql.*;

public class FreeExerciseDatabaseController implements DatabaseCRUD {

    PreparedStatement statement;

    public int create(Object object) {
        FreeExercise exercise = (FreeExercise) isFreeExercise(object);
        String superSql = "INSERT INTO exercise "
                + "(name)"
                + "VALUES(?)";
        String subSql = "INSERT INTO free_exercise "
                    + "(exercise_id, name, description) "
                    + "VALUES(?, ?, ?)";

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
            statement.setString(2, exercise.getName());
            statement.setString(3, exercise.getDescription());
            statement.executeUpdate();
            connection.close();
            return i;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object retrieve(int exercise_id) {
        String sql = "SELECT * "
                    + "FROM free_exercise NATURAL JOIN exercise "
                    + "WHERE exercise_ID = ?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, exercise_id);
            ResultSet rs = statement.executeQuery();

            FreeExercise e1;
            if (rs.next()) {
                e1 = new FreeExercise(
                        rs.getInt("exercise_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                connection.close();
                return e1;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Not needed in this projectt
     * @param object to be persisted
     */
    @Deprecated
    public void update(Object object) {
        return;
    }

    /**
     * Not needed in this project
     * @param id of object to delete
     */
    @Deprecated
    public void delete(int id) {
        return;
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
