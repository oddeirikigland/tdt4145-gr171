package core;

import java.sql.*;

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

            try {
                // Retrieves all generated keys and returns the key for the java object
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    @Deprecated
    public void delete(int id) {
    }
    @Deprecated
    public void update(Object object) {
    }

    public Machine retrieve(int id) {
        String sql = "SELECT * "
                    + "FROM machine "
                    + "WHERE machine_id=?";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            Machine m1;
            if (rs.next()) {
                m1 = new Machine(
                        rs.getInt("machine_id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                connection.close();
                return m1;
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getMachines() {
        String sql = "SELECT * "
                + "FROM machine";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            connection.close();
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
