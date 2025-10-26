import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:students.db";

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
