package dataaccesslayer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import transferobjects.AccountTransactionDTO;
import transferobjects.LedgerSummaryDTO;

/**
 * Implements data access operations for the credit and debit ledger.
 * Records account transactions, retrieves transaction history, generates
 * monthly summaries, and settles outstanding debit transactions.
 *
 * @author Tianzhu Li
 */
public class LedgerDaoImpl implements LedgerDao {

    /**
     * Maps the current result-set row to an account transaction.
     *
     * @param rs the result set containing transaction data
     * @return the mapped account transaction
     * @throws SQLException if the result set cannot be read
     */
    private AccountTransactionDTO map(ResultSet rs) throws SQLException {
        AccountTransactionDTO t = new AccountTransactionDTO();

        t.setAccountTransactionId(
                rs.getInt("account_transaction_id")
        );
        t.setUserId(rs.getInt("user_id"));
        t.setTransactionType(
                AccountTransactionDTO.TransactionType.valueOf(
                        rs.getString("transaction_type")
                )
        );
        t.setActivityType(
                AccountTransactionDTO.ActivityType.valueOf(
                        rs.getString("activity_type")
                )
        );
        t.setAmount(rs.getDouble("amount"));
        t.setDescription(rs.getString("description"));
        t.setTransactionDate(
                rs.getTimestamp("transaction_date").toLocalDateTime()
        );
        t.setSettled(rs.getBoolean("settled"));

        Timestamp settled = rs.getTimestamp("settled_at");

        if (settled != null) {
            t.setSettledAt(settled.toLocalDateTime());
        }

        return t;
    }

    /**
     * Stores a new account transaction in the ledger.
     *
     * @param tx the account transaction to record
     */
    @Override
    public void recordTransaction(AccountTransactionDTO tx) {
        String sql = "INSERT INTO account_transactions "
                + "(user_id, transaction_type, activity_type, amount, "
                + "description) VALUES (?,?,?,?,?)";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tx.getUserId());
            ps.setString(2, tx.getTransactionType().name());
            ps.setString(3, tx.getActivityType().name());
            ps.setDouble(4, tx.getAmount());
            ps.setString(5, tx.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("recordTransaction failed", e);
        }
    }

    /**
     * Retrieves all account transactions for a user in reverse
     * chronological order.
     *
     * @param userId the ID of the user
     * @return a list of the user's account transactions
     */
    @Override
    public List<AccountTransactionDTO> getTransactionsForUser(int userId) {
        String sql = "SELECT * FROM account_transactions "
                + "WHERE user_id = ? ORDER BY transaction_date DESC";

        List<AccountTransactionDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getTransactionsForUser failed", e
            );
        }

        return list;
    }

    /**
     * Retrieves all unsettled debit transactions for a user in
     * chronological order.
     *
     * @param userId the ID of the user
     * @return a list of the user's unsettled debit transactions
     */
    @Override
    public List<AccountTransactionDTO> getUnsettledDebits(int userId) {
        String sql = "SELECT * FROM account_transactions "
                + "WHERE user_id = ? AND transaction_type = 'DEBIT' "
                + "AND settled = FALSE ORDER BY transaction_date";

        List<AccountTransactionDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("getUnsettledDebits failed", e);
        }

        return list;
    }

    /**
     * Maps the current result-set row to a monthly ledger summary.
     *
     * @param rs the result set containing monthly summary data
     * @return the mapped ledger summary
     * @throws SQLException if the result set cannot be read
     */
    private LedgerSummaryDTO mapSummary(ResultSet rs)
            throws SQLException {

        LedgerSummaryDTO s = new LedgerSummaryDTO();

        s.setUserId(rs.getInt("user_id"));
        s.setName(rs.getString("name"));
        s.setEmail(rs.getString("email"));
        s.setReportMonth(rs.getString("report_month"));
        s.setTotalCredits(rs.getDouble("total_credits"));
        s.setTotalDebits(rs.getDouble("total_debits"));
        s.setTotalPayments(rs.getDouble("total_payments"));
        s.setAmountToSettle(rs.getDouble("amount_to_settle"));

        return s;
    }

    /**
     * Retrieves the current month's ledger summary for a user.
     * If the user has no transactions during the current month, a summary
     * containing the user ID and zero values is returned.
     *
     * @param userId the ID of the user
     * @return the user's current-month ledger summary
     */
    @Override
    public LedgerSummaryDTO getCurrentMonthSummary(int userId) {
        String sql = "SELECT * FROM v_user_monthly_account_report "
                + "WHERE user_id = ? "
                + "AND report_month = "
                + "DATE_FORMAT(CURRENT_TIMESTAMP, '%Y-%m')";

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSummary(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getCurrentMonthSummary failed", e
            );
        }

        LedgerSummaryDTO empty = new LedgerSummaryDTO();
        empty.setUserId(userId);

        return empty;
    }

    /**
     * Retrieves current-month ledger summaries for all users, ordered by
     * user name.
     *
     * @return a list of current-month ledger summaries
     */
    @Override
    public List<LedgerSummaryDTO> getAllCurrentMonthSummaries() {
        String sql = "SELECT * FROM v_user_monthly_account_report "
                + "WHERE report_month = "
                + "DATE_FORMAT(CURRENT_TIMESTAMP, '%Y-%m') "
                + "ORDER BY name";

        List<LedgerSummaryDTO> list = new ArrayList<>();

        try (Connection con = DataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapSummary(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "getAllCurrentMonthSummaries failed", e
            );
        }

        return list;
    }

    /**
     * Settles the oldest outstanding debit transactions that can be fully
     * covered by the supplied payment amount. The operation also records a
     * payment transaction and is performed as a database transaction.
     *
     * @param userId the ID of the user making the payment
     * @param amount the payment amount
     */
    @Override
    public void settleDebits(int userId, double amount) {
        try (Connection con = DataSource.getConnection()) {
            con.setAutoCommit(false);

            try {
                double remaining = amount;

                String selSql = "SELECT account_transaction_id, amount "
                        + "FROM account_transactions "
                        + "WHERE user_id = ? "
                        + "AND transaction_type = 'DEBIT' "
                        + "AND settled = FALSE "
                        + "ORDER BY transaction_date FOR UPDATE";

                try (PreparedStatement sel =
                        con.prepareStatement(selSql)) {

                    sel.setInt(1, userId);

                    try (ResultSet rs = sel.executeQuery()) {
                        String updSql =
                                "UPDATE account_transactions "
                                + "SET settled = TRUE, settled_at = NOW() "
                                + "WHERE account_transaction_id = ?";

                        try (PreparedStatement upd =
                                con.prepareStatement(updSql)) {

                            while (rs.next() && remaining > 0) {
                                double debitAmt =
                                        rs.getDouble("amount");

                                if (debitAmt <= remaining) {
                                    upd.setInt(
                                            1,
                                            rs.getInt(
                                                    "account_transaction_id"
                                            )
                                    );
                                    upd.executeUpdate();
                                    remaining -= debitAmt;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }

                String insSql = "INSERT INTO account_transactions "
                        + "(user_id, transaction_type, activity_type, "
                        + "amount, description, settled, settled_at) "
                        + "VALUES (?, 'PAYMENT', 'SETTLEMENT', ?, "
                        + "'Member settlement/recharge', TRUE, NOW())";

                try (PreparedStatement ins =
                        con.prepareStatement(insSql)) {

                    ins.setInt(1, userId);
                    ins.setDouble(2, amount);
                    ins.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("settleDebits failed", e);
        }
    }
}