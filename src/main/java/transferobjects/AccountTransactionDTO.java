package transferobjects;

import java.time.LocalDateTime;

/**
 * Transfer object for an account transaction.
 * Stores information about a user's credit, debit, payment, or adjustment,
 * including the related activity, amount, transaction date, and settlement
 * status.
 *
 * @author Tianzhu Li
 */
public class AccountTransactionDTO {

    /**
     * Represents the financial type of an account transaction.
     */
    public enum TransactionType {
        CREDIT,
        DEBIT,
        PAYMENT,
        ADJUSTMENT
    }

    /**
     * Represents the activity associated with an account transaction.
     */
    public enum ActivityType {
        EQUIPMENT_USAGE,
        MATERIAL_USAGE,
        DONATION,
        TRAINING,
        MAINTENANCE,
        WORK_ORDER,
        SETTLEMENT
    }

    private Integer accountTransactionId;
    private Integer userId;
    private TransactionType transactionType;
    private ActivityType activityType;
    private double amount;
    private String description;
    private LocalDateTime transactionDate;
    private boolean settled;
    private LocalDateTime settledAt;

    /**
     * Returns the unique identifier of the account transaction.
     *
     * @return the account transaction ID
     */
    public Integer getAccountTransactionId() {
        return accountTransactionId;
    }

    /**
     * Sets the unique identifier of the account transaction.
     *
     * @param accountTransactionId the account transaction ID
     */
    public void setAccountTransactionId(Integer accountTransactionId) {
        this.accountTransactionId = accountTransactionId;
    }

    /**
     * Returns the ID of the user associated with the transaction.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user associated with the transaction.
     *
     * @param userId the user ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the financial type of the transaction.
     *
     * @return the transaction type
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the financial type of the transaction.
     *
     * @param transactionType the transaction type
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Returns the activity associated with the transaction.
     *
     * @return the activity type
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * Sets the activity associated with the transaction.
     *
     * @param activityType the activity type
     */
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    /**
     * Returns the monetary amount of the transaction.
     *
     * @return the transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the monetary amount of the transaction.
     *
     * @param amount the transaction amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the description of the transaction.
     *
     * @return the transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description the transaction description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the date and time when the transaction occurred.
     *
     * @return the transaction date and time
     */
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the date and time when the transaction occurred.
     *
     * @param transactionDate the transaction date and time
     */
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Indicates whether the transaction has been settled.
     *
     * @return true if the transaction has been settled; otherwise false
     */
    public boolean isSettled() {
        return settled;
    }

    /**
     * Sets whether the transaction has been settled.
     *
     * @param settled true if the transaction has been settled; otherwise false
     */
    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    /**
     * Returns the date and time when the transaction was settled.
     *
     * @return the settlement date and time
     */
    public LocalDateTime getSettledAt() {
        return settledAt;
    }

    /**
     * Sets the date and time when the transaction was settled.
     *
     * @param settledAt the settlement date and time
     */
    public void setSettledAt(LocalDateTime settledAt) {
        this.settledAt = settledAt;
    }
}