package oopProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CandidateDAO provides pure JDBC Data Access Object (DAO) operations for the 'Candidates' SQLite table.
 * Implements standard CRUD operations (addCandidate, getCandidateByIdCard, getCandidateByFormNo, getAllCandidates, updateCandidate, deleteCandidate).
 * Uses robust try-with-resources to prevent SQLite database locking errors.
 */
public class CandidateDAO {

    private final DatabaseManager dbManager;

    public CandidateDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Inserts a new Candidate record into the SQLite Candidates table.
     */
    public boolean addCandidate(Candidate candidate) {
        String sql = "INSERT INTO Candidates (idCard, formNo, name, fname, phoneNo, candidateEmail, candidatePass) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, candidate.getIdCard());
            pstmt.setInt(2, candidate.getFormNo());
            pstmt.setString(3, candidate.getName());
            pstmt.setString(4, candidate.getFname());
            pstmt.setString(5, candidate.getPhoneNo());
            pstmt.setString(6, candidate.getCandidateEmail());
            pstmt.setString(7, candidate.getCandidatePass());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("[CandidateDAO] Error adding candidate: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a Candidate record by idCard (Primary Key).
     */
    public Candidate getCandidateByIdCard(String idCard) {
        String sql = "SELECT idCard, formNo, name, fname, phoneNo, candidateEmail, candidatePass FROM Candidates WHERE idCard = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idCard);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractCandidateFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CandidateDAO] Error fetching candidate by idCard: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a Candidate record by formNo.
     */
    public Candidate getCandidateByFormNo(int formNo) {
        String sql = "SELECT idCard, formNo, name, fname, phoneNo, candidateEmail, candidatePass FROM Candidates WHERE formNo = ?;";
        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, formNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractCandidateFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[CandidateDAO] Error fetching candidate by formNo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all Candidate records from the database.
     */
    public List<Candidate> getAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        String sql = "SELECT idCard, formNo, name, fname, phoneNo, candidateEmail, candidatePass FROM Candidates ORDER BY formNo ASC;";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                candidates.add(extractCandidateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("[CandidateDAO] Error retrieving all candidates: " + e.getMessage());
        }
        return candidates;
    }

    /**
     * Updates an existing Candidate record in the database.
     */
    public boolean updateCandidate(Candidate candidate) {
        String sql = "UPDATE Candidates SET formNo = ?, name = ?, fname = ?, phoneNo = ?, candidateEmail = ?, candidatePass = ? WHERE idCard = ?;";

        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, candidate.getFormNo());
            pstmt.setString(2, candidate.getName());
            pstmt.setString(3, candidate.getFname());
            pstmt.setString(4, candidate.getPhoneNo());
            pstmt.setString(5, candidate.getCandidateEmail());
            pstmt.setString(6, candidate.getCandidatePass());
            pstmt.setString(7, candidate.getIdCard());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("[CandidateDAO] Error updating candidate: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a Candidate record from the database by idCard.
     */
    public boolean deleteCandidate(String idCard) {
        String sql = "DELETE FROM Candidates WHERE idCard = ?;";

        try (Connection conn = dbManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idCard);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("[CandidateDAO] Error deleting candidate: " + e.getMessage());
            return false;
        }
    }

    private Candidate extractCandidateFromResultSet(ResultSet rs) throws SQLException {
        String idCard = rs.getString("idCard");
        int formNo = rs.getInt("formNo");
        String name = rs.getString("name");
        String fname = rs.getString("fname");
        String phoneNo = rs.getString("phoneNo");
        String candidateEmail = rs.getString("candidateEmail");
        String candidatePass = rs.getString("candidatePass");

        Candidate candidate = new Candidate();
        candidate.addBasicData(name, fname, idCard, phoneNo, formNo, candidateEmail, candidatePass);
        return candidate;
    }
}
