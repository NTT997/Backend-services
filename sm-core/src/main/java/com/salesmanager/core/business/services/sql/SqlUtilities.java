package com.salesmanager.core.business.services.sql;

import java.sql.*;
import java.sql.Date;
import java.util.*;


public class SqlUtilities {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orclpdb";
    private static final String DB_USER = "tienlocsuper";
    private static final String DB_PASSWORD = "tienlocvuive12";
    
    public static Connection getConnectionToDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        System.out.println("Database connection successful.");
        return connection;
    }
	public static ResultSet getDataFromTableNamed(String TableName) throws SQLException {
        // SQL query
        String query = "SELECT * FROM "+TableName;
        Connection connection = getConnectionToDatabase();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}

	public static void syncDataFromLocalToRemote() throws SQLException {
	    ResultSet localData = getDataFromTableNamed("PRODUCT_AVAILABILITY");
	    ResultSet remoteData = getDataFromTableNamed("REMOTE_PRODUCT_AVAILABILITY");

	    Map<Integer, Integer> localMap = new HashMap<>();
	    Map<Integer, Integer> remoteMap = new HashMap<>();

	    // Load local table into map
	    while (localData.next()) {
	        int id = localData.getInt("PRODUCT_AVAIL_ID");
	        int quantity = localData.getInt("QUANTITY");
	        localMap.put(id, quantity);
	    }

	    // Load remote table into map
	    while (remoteData.next()) {
	        int id = remoteData.getInt("PRODUCT_AVAIL_ID");
	        int quantity = remoteData.getInt("QUANTITY");
	        remoteMap.put(id, quantity);
	    }

	    // Get a connection to the remote DB
	    try (Connection remoteConn = getConnectionToDatabase()) {

	        // Prepare statements for update and insert
	        String updateSql = "UPDATE REMOTE_PRODUCT_AVAILABILITY SET QUANTITY = ? WHERE PRODUCT_AVAIL_ID = ?";
	        String insertSql = "INSERT INTO REMOTE_PRODUCT_AVAILABILITY (PRODUCT_AVAIL_ID, QUANTITY) VALUES (?, ?)";

	        try (PreparedStatement updateStmt = remoteConn.prepareStatement(updateSql);
	             PreparedStatement insertStmt = remoteConn.prepareStatement(insertSql)) {

	            for (Map.Entry<Integer, Integer> entry : localMap.entrySet()) {
	                int id = entry.getKey();
	                int localQty = entry.getValue();

	                if (!remoteMap.containsKey(id)) {
		                System.out.println("Inserting productAvailId=" + id + " with quantity=" + localQty);
	                    insertStmt.setInt(1, id);
	                    insertStmt.setInt(2, localQty);
	                    insertStmt.executeUpdate();
	                } else {
	                    int remoteQty = remoteMap.get(id);
	                    if (localQty != remoteQty) {
	                        // Update quantity in remote table
	                        System.out.println("Updating productAvailId=" + id +
	                                           " from remoteQty=" + remoteQty +
	                                           " to localQty=" + localQty);
	                        updateStmt.setInt(1, localQty);
	                        updateStmt.setInt(2, id);
	                        updateStmt.executeUpdate();
	                    }
	                }
	            }
	        }
	    }
	}
	
	public static void loggingERRORToLogDB(
	        String METHOD,
	        String logMessage,
	        String LOG_USERID_FK,  // optional, ignore if not used
	        String LOG_IP_ADDRESS,
	        String LogMenu,
	        String MESSAGE_TEMPLATE,
	        String EXCEPTION,
	        String PROPERTIES
	) {
	    // Build SQL string with values directly
	    String sql = "INSERT INTO \"TIENLOCSUPER\".\"APPLOG\" (" +
	            "\"LOG_METHOD\", \"LOG_MESSAGE\", \"LOG_IP_ADDRESS\", \"LOG_TIME\", " +
	            "\"LOG_MENU\", \"MESSAGE_TEMPLATE\", \"LOGLEVEL\", \"EXCEPTION\", \"PROPERTIES\"" +
	            ") VALUES (" +
	            "'" + METHOD.replace("'", "''") + "'," +
	            "'" + logMessage.replace("'", "''") + "'," +
	            "'" + LOG_IP_ADDRESS.replace("'", "''") + "'," +
	            "SYSDATE," +  // Oracle current date/time
	            "'" + LogMenu.replace("'", "''") + "'," +
	            "'" + MESSAGE_TEMPLATE.replace("'", "''") + "'," +
	            "'INFO'," +
	            "'" + EXCEPTION.replace("'", "''") + "'," +
	            "'" + PROPERTIES.replace("'", "''") + "'" +
	            ")";

	    System.out.println("Executing SQL: " + sql);

	    try (Connection conn = getConnectionToDatabase();
	         Statement stmt = conn.createStatement()) {

	        stmt.executeUpdate(sql);
	        System.out.println("Log inserted successfully.");

	    } catch (SQLException e) {
	        System.err.println("Failed to write log to DB: " + e.getMessage());
	    }
	}


    public static void printAllTables() {
        String schema = "TIENLOCSUPER"; // change to any schema you want

        String sql = "SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = ? ORDER BY TABLE_NAME";

        try (Connection connection = getConnectionToDatabase();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, schema.toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Tables in schema " + schema + ":");
                while (rs.next()) {
                    System.out.println(rs.getString("TABLE_NAME"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching tables: " + e.getMessage());
        }
    }



	

}