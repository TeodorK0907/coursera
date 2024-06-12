package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionManager {
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

    private static final String DB_URL
            = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "root";

    public Connection connectDB() {

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER,
                        PASS)
        ) {
            Class.forName(POSTGRESQL_DRIVER);
            return connection;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}