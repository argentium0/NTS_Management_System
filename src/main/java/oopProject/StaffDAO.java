package oopProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * StaffDAO provides pure JDBC operations for the 'Invigilators' and 'Superintendents' SQLite tables.
 */
public class StaffDAO {

    private final DatabaseManager dbManager;

    public StaffDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean addInvigilator(Invigilator inv) {
        String sql = "INSERT INTO Invigilators (employeeID, idCard, name, fname, phoneNo, employeeCity, experience, invig_allowance, designation, superintendentName, supdtPhone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inv.getEmployeeID());
            pstmt.setString(2, inv.getIdCard());
            pstmt.setString(3, inv.getName());
            pstmt.setString(4, inv.getFname());
            pstmt.setString(5, inv.getPhoneNo());
            pstmt.setString(6, inv.getEmployeeCity());
            pstmt.setInt(7, inv.getExperience());
            pstmt.setDouble(8, inv.getInvig_allowance());
            pstmt.setString(9, inv.getDesignation());
            pstmt.setString(10, inv.getSuperintendentName());
            pstmt.setLong(11, inv.getSupdtPhone() != null ? inv.getSupdtPhone() : 0L);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error adding invigilator: " + e.getMessage());
            return false;
        }
    }

    public boolean addSuperintendent(Superintendent spd) {
        String sql = "INSERT INTO Superintendents (employeeID, idCard, name, fname, phoneNo, employeeCity, experience, spdt_allowance, interval) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, spd.getEmployeeID());
            pstmt.setString(2, spd.getIdCard());
            pstmt.setString(3, spd.getName());
            pstmt.setString(4, spd.getFname());
            pstmt.setString(5, spd.getPhoneNo());
            pstmt.setString(6, spd.getEmployeeCity());
            pstmt.setInt(7, spd.getExperience());
            pstmt.setDouble(8, spd.getSpdt_allowance());
            pstmt.setInt(9, spd.getInterval());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error adding superintendent: " + e.getMessage());
            return false;
        }
    }

    public List<Invigilator> getAllInvigilators() {
        List<Invigilator> list = new ArrayList<>();
        String sql = "SELECT employeeID, idCard, name, fname, phoneNo, employeeCity, experience, invig_allowance, designation, superintendentName, supdtPhone FROM Invigilators;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Invigilator inv = new Invigilator(
                        rs.getString("name"),
                        rs.getString("fname"),
                        rs.getString("idCard"),
                        rs.getString("phoneNo"),
                        rs.getInt("employeeID"),
                        rs.getString("employeeCity"),
                        0.0f,
                        rs.getInt("experience"),
                        rs.getDouble("invig_allowance"),
                        0.0,
                        rs.getString("designation"),
                        rs.getString("superintendentName"),
                        rs.getLong("supdtPhone")
                );
                list.add(inv);
            }
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error retrieving invigilators: " + e.getMessage());
        }
        return list;
    }

    public List<Superintendent> getAllSuperintendents() {
        List<Superintendent> list = new ArrayList<>();
        String sql = "SELECT employeeID, idCard, name, fname, phoneNo, employeeCity, experience, spdt_allowance, interval FROM Superintendents;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Superintendent spd = new Superintendent(
                        rs.getString("name"),
                        rs.getString("fname"),
                        rs.getString("idCard"),
                        rs.getString("phoneNo"),
                        rs.getInt("employeeID"),
                        rs.getString("employeeCity"),
                        0.0f,
                        rs.getInt("experience"),
                        0.0,
                        rs.getDouble("spdt_allowance"),
                        rs.getInt("interval")
                );
                list.add(spd);
            }
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error retrieving superintendents: " + e.getMessage());
        }
        return list;
    }

    public boolean updateInvigilator(Invigilator inv) {
        String sql = "UPDATE Invigilators SET idCard = ?, name = ?, fname = ?, phoneNo = ?, employeeCity = ?, experience = ?, invig_allowance = ?, designation = ?, superintendentName = ?, supdtPhone = ? WHERE employeeID = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inv.getIdCard());
            pstmt.setString(2, inv.getName());
            pstmt.setString(3, inv.getFname());
            pstmt.setString(4, inv.getPhoneNo());
            pstmt.setString(5, inv.getEmployeeCity());
            pstmt.setInt(6, inv.getExperience());
            pstmt.setDouble(7, inv.getInvig_allowance());
            pstmt.setString(8, inv.getDesignation());
            pstmt.setString(9, inv.getSuperintendentName());
            pstmt.setLong(10, inv.getSupdtPhone() != null ? inv.getSupdtPhone() : 0L);
            pstmt.setInt(11, inv.getEmployeeID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error updating invigilator: " + e.getMessage());
            return false;
        }
    }

    public boolean updateSuperintendent(Superintendent spd) {
        String sql = "UPDATE Superintendents SET idCard = ?, name = ?, fname = ?, phoneNo = ?, employeeCity = ?, experience = ?, spdt_allowance = ?, interval = ? WHERE employeeID = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, spd.getIdCard());
            pstmt.setString(2, spd.getName());
            pstmt.setString(3, spd.getFname());
            pstmt.setString(4, spd.getPhoneNo());
            pstmt.setString(5, spd.getEmployeeCity());
            pstmt.setInt(6, spd.getExperience());
            pstmt.setDouble(7, spd.getSpdt_allowance());
            pstmt.setInt(8, spd.getInterval());
            pstmt.setInt(9, spd.getEmployeeID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error updating superintendent: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteInvigilator(int employeeID) {
        String sql = "DELETE FROM Invigilators WHERE employeeID = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error deleting invigilator: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSuperintendent(int employeeID) {
        String sql = "DELETE FROM Superintendents WHERE employeeID = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[StaffDAO] Error deleting superintendent: " + e.getMessage());
            return false;
        }
    }
}
