package oopProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestCentreDAO provides pure JDBC operations for the 'TestCentres' SQLite table.
 */
public class TestCentreDAO {

    private final DatabaseManager dbManager;

    public TestCentreDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean addTestCentre(TestCentre tc) {
        String sql = "INSERT INTO TestCentres (testCentreNo, testCentreBuilding, testCentreAdress, allocationDate) VALUES (?, ?, ?, ?);";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tc.getTestCentreNo());
            pstmt.setString(2, tc.getTestCentreBuilding());
            pstmt.setString(3, tc.getTestCentreAdress());
            pstmt.setString(4, tc.getAllocationDate());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TestCentreDAO] Error adding test centre: " + e.getMessage());
            return false;
        }
    }

    public List<TestCentre> getAllTestCentres() {
        List<TestCentre> list = new ArrayList<>();
        String sql = "SELECT testCentreNo, testCentreBuilding, testCentreAdress, allocationDate FROM TestCentres ORDER BY testCentreNo ASC;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                TestCentre tc = new TestCentre(
                        rs.getInt("testCentreNo"),
                        rs.getString("testCentreBuilding"),
                        rs.getString("testCentreAdress"),
                        rs.getString("allocationDate")
                );
                list.add(tc);
            }
        } catch (SQLException e) {
            System.err.println("[TestCentreDAO] Error retrieving test centres: " + e.getMessage());
        }
        return list;
    }

    public boolean updateTestCentre(TestCentre tc) {
        String sql = "UPDATE TestCentres SET testCentreBuilding = ?, testCentreAdress = ?, allocationDate = ? WHERE testCentreNo = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tc.getTestCentreBuilding());
            pstmt.setString(2, tc.getTestCentreAdress());
            pstmt.setString(3, tc.getAllocationDate());
            pstmt.setInt(4, tc.getTestCentreNo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TestCentreDAO] Error updating test centre: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTestCentre(int testCentreNo) {
        String sql = "DELETE FROM TestCentres WHERE testCentreNo = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, testCentreNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TestCentreDAO] Error deleting test centre: " + e.getMessage());
            return false;
        }
    }
}
