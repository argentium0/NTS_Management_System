package oopProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminDAO provides pure JDBC Data Access Object operations for the 'Admins' SQLite table.
 */
public class AdminDAO {

    private final DatabaseManager dbManager;

    public AdminDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean validateAdmin(String username, String password) {
        String sql = "SELECT password FROM Admins WHERE username = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return storedPassword.equals(password);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error validating admin credentials: " + e.getMessage());
        }
        return false;
    }

    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE Admins SET password = ? WHERE username = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("[AdminDAO] Error updating admin password: " + e.getMessage());
            return false;
        }
    }
}
