package core;

import net.efabrika.util.DBTablePrinter;

import java.sql.*;

public class ViewWorkoutOnMachineDatabaseController {

    PreparedStatement statement;

    public void printMachines() {
        System.out.println("----MACHINES----");
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            DBTablePrinter.printTable(conn, "machine", 100, 120);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retrieveMachineByID(int id) {
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
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            DBTablePrinter.printResultSet(rs, 120);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
