package businesslayer;

import java.util.List;
import dataaccesslayer.LedgerDao;
import dataaccesslayer.LedgerDaoImpl;
import transferobjects.AccountTransactionDTO;
import transferobjects.LedgerSummaryDTO;

/**
 * Provides business operations for monthly credit and debit reporting.
 * Supports retrieving account summaries and transaction histories,
 * recording account transactions, and settling member accounts.
 *
 * @author Tianzhu Li
 */
public class LedgerBusinessLogic {

    private final LedgerDao ledgerDao;

    /**
     * Creates the ledger business logic with the default DAO
     * implementation.
     */
    public LedgerBusinessLogic() {
        this(new LedgerDaoImpl());
    }

    /**
     * Creates the ledger business logic with the specified ledger DAO.
     *
     * @param ledgerDao the DAO used for ledger operations
     */
    public LedgerBusinessLogic(LedgerDao ledgerDao) {
        this.ledgerDao = ledgerDao;
    }

    /**
     * Returns the current month's ledger summary for a user.
     *
     * @param userId the ID of the user
     * @return the user's current-month ledger summary
     */
    public LedgerSummaryDTO getMonthlySummary(int userId) {
        return ledgerDao.getCurrentMonthSummary(userId);
    }

    /**
     * Returns the current month's ledger summaries for all users.
     *
     * @return a list of current-month ledger summaries
     */
    public List<LedgerSummaryDTO> getAllMonthlySummaries() {
        return ledgerDao.getAllCurrentMonthSummaries();
    }

    /**
     * Returns the complete transaction history for a user.
     *
     * @param userId the ID of the user
     * @return a list of the user's account transactions
     */
    public List<AccountTransactionDTO> getTransactionHistory(
            int userId) {

        return ledgerDao.getTransactionsForUser(userId);
    }

    /**
     * Records an account transaction through the ledger data layer.
     * This method allows another business class to use ledger operations
     * without accessing the ledger DAO directly.
     *
     * @param transaction the account transaction to record
     */
    public void recordTransaction(
            AccountTransactionDTO transaction) {

        ledgerDao.recordTransaction(transaction);
    }

    /**
     * Validates and processes an account settlement.
     * Outstanding debit transactions are settled using the supplied
     * payment amount, beginning with the oldest unsettled debit.
     *
     * @param userId the ID of the user settling the account
     * @param amount the settlement payment amount
     * @throws ValidationException if the amount is not positive or the
     *                             user has no outstanding debits
     */
    public void settleAccount(
            int userId,
            double amount) throws ValidationException {

        if (amount <= 0) {
            throw new ValidationException(
                    "Settlement amount must be positive."
            );
        }

        List<AccountTransactionDTO> unsettled =
                ledgerDao.getUnsettledDebits(userId);

        if (unsettled.isEmpty()) {
            throw new ValidationException(
                    "There are no outstanding debits to settle."
            );
        }

        ledgerDao.settleDebits(userId, amount);
    }
}