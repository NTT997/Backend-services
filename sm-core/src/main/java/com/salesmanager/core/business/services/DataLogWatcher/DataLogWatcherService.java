package com.salesmanager.core.business.services.DataLogWatcher;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.dcn.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.config.LogWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import oracle.jdbc.OracleStatement;
import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.jdbc.dcn.TableChangeDescription;


@Service
public class DataLogWatcherService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LogWebSocketHandler logWebSocketHandler;

    private OracleConnection oracleConn;
    private DatabaseChangeRegistration dcr;
    private Statement watchStatement;

    @PostConstruct
    public void init() {
        System.out.println("DataLogWatcherService initialized!");
        try {
            // 1. Get OracleConnection
            this.oracleConn = dataSource.getConnection().unwrap(OracleConnection.class);

            // 2. DCN properties
            Properties prop = new Properties();
            prop.setProperty(OracleConnection.DCN_NOTIFY_ROWIDS, "true");

            // 3. Register change notification
            this.dcr = oracleConn.registerDatabaseChangeNotification(prop);
            System.out.println("[WATCHER] DCN registered successfully.");

            // 4. Listener for DB changes
            this.dcr.addListener(event -> {
                System.out.println("[WATCHER] Change event received.");
                for (TableChangeDescription tableChange : event.getTableChangeDescription()) {
                    String changedTable = tableChange.getTableName();
                    System.out.println("[WATCHER] Changed table: " + changedTable);

                    if ("TIENLOCSUPER.DATA_LOG".equalsIgnoreCase(changedTable)) {
                        for (RowChangeDescription rowChange : tableChange.getRowChangeDescription()) {
                            String rowId = rowChange.getRowid().stringValue();
                            System.out.println("[WATCHER] Changed ROWID: " + rowId);

                            // Fetch and send the log data
                            String logData = fetchLogByRowId(rowId);
                            if (logData != null) {
                                logWebSocketHandler.sendLogToClients(logData);
                            }
                        }
                    }
                }
            });

            // 5. Create statement and attach DCN
            this.watchStatement = oracleConn.createStatement();
            ((OracleStatement) this.watchStatement).setDatabaseChangeRegistration(this.dcr);

            // 6. Execute a query that you want to monitor
            this.watchStatement.executeQuery("SELECT LOGID, METHOD, LOG_MESSAGE, LOG_TIME, LOG_IP_ADDRESS FROM TIENLOCSUPER.DATA_LOG");

            System.out.println("[WATCHER] Listening to DB_LOG table changes...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchLogByRowId(String rowId) {
        String sql = "SELECT LOGID, METHOD, LOG_MESSAGE, LOG_TIME, LOG_IP_ADDRESS " +
                     "FROM TIENLOCSUPER.DATA_LOG WHERE ROWID = ?";
        try (PreparedStatement ps = oracleConn.prepareStatement(sql)) {
            ps.setString(1, rowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int logId = rs.getInt("LOGID");
                    String method = rs.getString("METHOD");
                    String message = rs.getString("LOG_MESSAGE");
                    String IP_ADDRESS = rs.getString("LOG_IP_ADDRESS");
                    Timestamp time = rs.getTimestamp("LOG_TIME");

                    String logData = String.format(
                            "LogID: %d, Method: %s, Message: %s, CreatedAt: %s, IP_ADDRESS: %s",
                            logId, method, message, time,IP_ADDRESS);
                    System.out.println("[WATCHER] Sending log to clients: " + logData);
                    return logData;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PreDestroy
    public void shutdown() throws Exception {
        System.out.println("[WATCHER] Shutting down watcher...");
        if (dcr != null) {
            try {
                oracleConn.unregisterDatabaseChangeNotification(dcr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (watchStatement != null) {
            try {
                watchStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (oracleConn != null) {
            try {
                oracleConn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}