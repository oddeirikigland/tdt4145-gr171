package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FreeExerciseDatabaseController {

    PreparedStatement statement;

    public int create(Object object) {
        FreeExercise exercise = (FreeExercise) isFreeExercise(object);
        String sql = "INSERT INTO free_exercise "
                    + "(name, description)"
                    + "VALUES(?, ?)";

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, exercise.getName());
            statement.setString(2, exercise.getDescription());
            
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
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
