package addon.ericlam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static addon.ericlam.Variable.MYsql;

public class MySQL {
        private static MySQL mysql;
        public static MySQL getinstance () {
        if (mysql == null) mysql = new MySQL();
        return mysql;
    }
        public Connection connection;
        private String host = Variable.config.getString("General.MySQL.host");
        private String database = Variable.config.getString("General.MySQL.database");
        private String username = Variable.config.getString("General.MySQL.username");
        private String password = Variable.config.getString("General.MySQL.password");
        private int port = Variable.config.getInt("General.MySQL.port");
        public void openConnection () throws SQLException, ClassNotFoundException {
        mysql = this;
        if (connection != null && !connection.isClosed()) {
            return;
        }
        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }



}
