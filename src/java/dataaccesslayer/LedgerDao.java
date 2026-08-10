package dataaccesslayer;

import java.util.List;
import transferobjects.AccountTransactionDTO;
import transferobjects.LedgerSummaryDTO;

/**
 * Defines data access operations for the credit and debit ledger.
 * Supports recording transactions, retrieving account activity,
 * generating monthly account summaries, and settling outstanding debits.
 *
 * @author Tianzhu Li
 */
public interface LedgerDao {

    /**
     * Stores a new account transaction in the ledger.
     *
     * @param tx the account transaction to record
     */
    void recordTransaction(AccountTransactionDTO tx);

    /**
     * Returns all ledger transactions associated with a specific user.
     *
     * @param userId the ID of the user
     * @return a list of the user's account transactions
     */
    List<AccountTransactionDTO> getTransactionsForUser(int userId);

    /**
     * Returns all unsettled debit transactions associated with a user.
     *
     * @param userId the ID of the user
     * @return a list of the user's unsettled debit transactions
     */
    List<AccountTransactionDTO> getUnsettledDebits(int userId);

    /**
     * Returns the current month's Ledger summary for a specific user.
     * If the user has no transactions during the current month, a summary
     * containing the user ID and zero values is returned.
     * @param userId the ID of the user
     * @return the user's current-month ledger summary
     */
    LedgerSummaryDTO getCurrentMonthSummary(int userId);

    /**
     * Returns the current month's ledger summaries for all users.
     *
     * @return a list of current-month ledger summaries
     */
    List<LedgerSummaryDTO> getAllCurrentMonthSummaries();

    /**
     * Records a payment and marks the appropriate debit transactions as
     * settled for a user.
     *
     * @param userId the ID of the user
     * @param amount the payment amount used to settle the debits
     */
    void settleDebits(int userId, double amount);
}