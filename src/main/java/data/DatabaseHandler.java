package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

	/**
	 *	Wipes or creates new database based on flag 
	 * @param flag	 		true wipes database, false creates new database
	 * @throws IOException
	 */
    public static void database(boolean flag) throws IOException {
		
        String url = "jdbc:sqlite:database.db";
    	
        String create = "src/main/resources/database.sql";
        String wipe = "src/main/resources/wipe.sql";
        String path;

        if (flag) {
        	path = wipe;
        } else {
        	path = create;
        }

        BufferedReader in = new BufferedReader(new FileReader(path));
        String str;
        StringBuffer sql = new StringBuffer();
        while ((str = in.readLine()) != null) {
        		sql.append(str + "\n ");
        }
        in.close();
 
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                //DatabaseMetaData meta = conn.getMetaData();
                
                String[] sqlArr = sql.toString().split("(;\\n)");
                Statement stmt = conn.createStatement();
                for (String s : sqlArr) {
                		s += ";";
                		stmt.execute(s);
                }
            }
 
        } catch (SQLException ignore) {
        }
    }
 
    /**
     * @param args the command line arguments
     * @throws IOException 
     */
    public static void main(String[] args) {
    	try {
    		database(false);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

}
