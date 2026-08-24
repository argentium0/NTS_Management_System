package oopProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseManager implements the Singleton connection manager and DDL schema initialization
 * for the persistent SQLite database ('nts_database.db').
 * 
 * Table schemas map strictly 1:1 to UML class diagram attributes:
 * - Admins (username PK, password)
 * - Candidates (idCard PK, formNo, name, fname, phoneNo, candidateEmail, candidatePass)
 * - Invigilators (employeeID PK, idCard, name, fname, phoneNo, employeeCity, experience, invig_allowance, designation, superintendentName, supdtPhone)
 * - Superintendents (employeeID PK, idCard, name, fname, phoneNo, employeeCity, experience, spdt_allowance, interval)
 * - Tests (testID PK, testName, marks, charges, passingPer)
 * - TestCentres (testCentreNo PK, testCentreBuilding, testCentreAdress, allocationDate)
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:nts_database.db";
    private static DatabaseManager instance;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseManager] SQLite JDBC Driver not found: " + e.getMessage());
        }
    }

    private DatabaseManager() {
        initializeDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Enable WAL mode for high concurrency
            stmt.execute("PRAGMA journal_mode=WAL;");

            // 1. Table Admins
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Admins (
                    username TEXT PRIMARY KEY,
                    password TEXT NOT NULL
                );
            """);

            // 2. Table Candidates (Mapped to Person -> Candidate)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Candidates (
                    idCard TEXT PRIMARY KEY,
                    formNo INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    fname TEXT NOT NULL,
                    phoneNo TEXT NOT NULL,
                    candidateEmail TEXT NOT NULL,
                    candidatePass TEXT NOT NULL
                );
            """);

            // 3. Table Invigilators (Mapped to Person -> Employee -> Invigilator)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Invigilators (
                    employeeID INTEGER PRIMARY KEY,
                    idCard TEXT NOT NULL,
                    name TEXT NOT NULL,
                    fname TEXT NOT NULL,
                    phoneNo TEXT NOT NULL,
                    employeeCity TEXT NOT NULL,
                    experience INTEGER NOT NULL,
                    invig_allowance REAL NOT NULL,
                    designation TEXT NOT NULL,
                    superintendentName TEXT NOT NULL,
                    supdtPhone INTEGER NOT NULL
                );
            """);

            // 4. Table Superintendents (Mapped to Person -> Employee -> Superintendent)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Superintendents (
                    employeeID INTEGER PRIMARY KEY,
                    idCard TEXT NOT NULL,
                    name TEXT NOT NULL,
                    fname TEXT NOT NULL,
                    phoneNo TEXT NOT NULL,
                    employeeCity TEXT NOT NULL,
                    experience INTEGER NOT NULL,
                    spdt_allowance REAL NOT NULL,
                    interval INTEGER NOT NULL
                );
            """);

            // 5. Table Tests (Mapped to Test)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Tests (
                    testID INTEGER PRIMARY KEY,
                    testName TEXT NOT NULL,
                    marks INTEGER NOT NULL,
                    charges REAL NOT NULL,
                    passingPer REAL NOT NULL
                );
            """);

            // 6. Table TestCentres (Mapped to TestCentre)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS TestCentres (
                    testCentreNo INTEGER PRIMARY KEY,
                    testCentreBuilding TEXT NOT NULL,
                    testCentreAdress TEXT NOT NULL,
                    allocationDate TEXT NOT NULL
                );
            """);

            // Insert default Admin user if Admins table is empty
            seedDefaultAdmin(conn);

        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Error initializing SQLite database schema: " + e.getMessage());
        }
    }

    private void seedDefaultAdmin(Connection conn) {
        String checkSql = "SELECT COUNT(*) FROM Admins;";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO Admins (username, password) VALUES (?, ?);";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setString(1, "admin");
                    pstmt.setString(2, "admin123");
                    pstmt.executeUpdate();
                    System.out.println("[DatabaseManager] Default admin account seeded (username: admin / password: admin123).");
                }
            }
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Error seeding default admin: " + e.getMessage());
        }
    }
}
