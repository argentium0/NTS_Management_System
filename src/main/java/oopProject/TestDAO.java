package oopProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDAO provides pure JDBC operations for the 'Tests' SQLite table.
 */
public class TestDAO {

    private final DatabaseManager dbManager;

    public TestDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean addTest(Test test) {
        String sql = "INSERT INTO Tests (testID, testName, marks, charges, passingPer) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, test.getTestID());
            pstmt.setString(2, test.getTestName());
            pstmt.setInt(3, test.getMarks());
            pstmt.setDouble(4, test.getCharges());
            pstmt.setFloat(5, test.getPassingPer());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TestDAO] Error adding test: " + e.getMessage());
            return false;
        }
    }

    public List<Test> getAllTests() {
        List<Test> list = new ArrayList<>();
        String sql = "SELECT testID, testName, marks, charges, passingPer FROM Tests ORDER BY testID ASC;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Test t = new Test(
                        rs.getInt("testID"),
                        rs.getString("testName"),
                        rs.getInt("marks"),
                        rs.getDouble("charges"),
                        rs.getFloat("passingPer")
                );
                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("[TestDAO] Error retrieving tests: " + e.getMessage());
        }
        return list;
    }

    public boolean updateTest(Test test) {
        String sql = "UPDATE Tests SET testName = ?, marks = ?, charges = ?, passingPer = ? WHERE testID = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, test.getTestName());
            pstmt.setInt(2, test.getMarks());
            pstmt.setDouble(3, test.getCharges());
            pstmt.setFloat(4, test.getPassingPer());
            pstmt.setInt(5, test.getTestID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TestDAO] Error updating test: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTest(int testID) {
        String sql = "DELETE FROM Tests WHERE testID = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, testID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[TestDAO] Error deleting test: " + e.getMessage());
            return false;
        }
    }
}
