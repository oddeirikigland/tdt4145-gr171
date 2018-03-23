package core;

import java.sql.Connection;
import java.sql.ResultSet;

public class ResultSetConnection {
	
	private ResultSet set;
	private Connection connection;

	public ResultSetConnection(ResultSet set, Connection connection) {
		this.set = set;
		this.connection = connection;
	}
	
	public ResultSet getSet() {
		return set;
	}
	
	public Connection getConnection() {
		return connection;
	}
}
