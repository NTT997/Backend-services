package com.salesmanager.core.business.services.sql;

import java.sql.*;
import java.util.*;

public class SqlUtilities {
	public static Connection getConnectionToDatabase() {
        // Database credentials
        String url = "jdbc:oracle:thin:@localhost:1521/orclpdb"; 
        String user = "SALESMANAGERS";
        String password = "system";
        Connection connection = null;
        
        try {
        	 connection = DriverManager.getConnection(url, user, password);    
        } catch (SQLException e) {
            e.printStackTrace();
        }  
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

	    System.out.println("print line 34 at syncDataFromLocalToRemote");
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


	

}