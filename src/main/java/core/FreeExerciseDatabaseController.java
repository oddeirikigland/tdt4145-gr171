package core;

import java.sql.*;

public class FreeExerciseDatabaseController implements DatabaseCRUD {

    PreparedStatement statement;

    public int create(Object object) {
        FreeExercise fe1 = (FreeExercise) isFreeExercise(object);
        String sql =    "INSERT INTO exercise" +
                "(name)" +
                "VALUES (?)";
        try {
            int id = -1;
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            /* For Exercise */
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, fe1.getName());
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
            /* For FreeExercise */
            sql = "INSERT INTO free_exercise" +
                    "(exercise_id, description)" +
                    "VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, fe1.getDescription());
            connection.close();
            return id; // returns ID for both exercise and free_exercise table
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Object retrieve(int id) {
        String sql = "SELECT *" +
                "FROM exercise NATURAL JOIN free_exercise" +
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


    private Object isFreeExercise(Object obj) {
        if (obj instanceof FreeExercise) {
            return obj;
        }
        else {
            throw new IllegalArgumentException("Object must be FreeExercise");
        }
    }
}
