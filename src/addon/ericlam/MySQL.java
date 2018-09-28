package addon.ericlam;

import MySQL.HyperNite.SQLDataSourceManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static addon.ericlam.Variable.MYsql;

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
