package dataaccesslayer;

import java.sql.*;
import transferobjects.ExternalClientDTO;

/**
 * JDBC implementation of the external client persistence operations.
 * Supports storing and looking up external client records for work-order submissions.
 * @author Le Bao Thach Nguyen 
 */
public class ExternalClientDaoImpl implements ExternalClientDao {

    private ExternalClientDTO map(ResultSet rs) throws SQLException {
        ExternalClientDTO c = new ExternalClientDTO();
        c.setClientId(rs.getInt("client_id"));
        c.setClientName(rs.getString("client_name"));
        c.setOrganization(rs.getString("organization"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        Timestamp t = rs.getTimestamp("created_at");
        if (t != null) c.setCreatedAt(t.toLocalDateTime());
        return c;
    }

    @Override
    public int addClient(ExternalClientDTO client) {
        String sql = "INSERT INTO external_clients (client_name, organization, phone, email) VALUES (?,?,?,?)";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getClientName());
            ps.setString(2, client.getOrganization());
            ps.setString(3, client.getPhone());
            ps.setString(4, client.getEmail());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("addClient failed", e);
        }
    }

    @Override
    public ExternalClientDTO getClientByEmail(String email) {
        String sql = "SELECT * FROM external_clients WHERE email = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getClientByEmail failed", e);
        }
    }

    @Override
    public ExternalClientDTO getClientById(int clientId) {
        String sql = "SELECT * FROM external_clients WHERE client_id = ?";
        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("getClientById failed", e);
        }
    }
}
