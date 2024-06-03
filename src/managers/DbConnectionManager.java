package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionManager {
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

    private final String DB_URL
            = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "insert username here";
    private final String PASS = "insert password here";

    public Connection connectDB() {
        Connection connection = null;

        // Try block to check exceptions
        try {

            // Loading DB(SQL) drivers
            Class.forName(POSTGRESQL_DRIVER);

            // Registering SQL drivers
            connection = DriverManager.getConnection(DB_URL, USER,
                    PASS);
        }

        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
        return connection;
    }
}