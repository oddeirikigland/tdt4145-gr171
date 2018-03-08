package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MachineDatabaseController implements DatabaseCRUD {

    PreparedStatement statement;

    public int create(Object object) {
        Machine m1 = (Machine) isMachine(object);
        String sql = "INSERT INTO machine "
                + "(name, description) "
                + "VALUES(?, ?)";

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, m1.getName());
            statement.setString(2, m1.getDescription());
            statement.executeUpdate();
            // TODO: Ikke ferdig
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }

    private Object isMachine(Object obj) {
        if (obj instanceof Machine) {
            return obj;
        }
        else {
            throw new IllegalArgumentException("Object must be a Machine");
        }
    }
}
