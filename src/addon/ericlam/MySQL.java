package addon.ericlam;

import mysql.hypernite.mc.SQLDataSourceManager;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQL {
        private Connection connection;
        public void openConnection () throws SQLException {
        SQLDataSourceManager dataSourceManager = SQLDataSourceManager.getInstance();
        if (connection != null && !connection.isClosed()) {
            return;
        }
        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            connection = dataSourceManager.getFuckingConnection();
        }
    }



}
