package transferobjects;

/**
 * Transfer object for one row of the monthly member account report.
 * Maps to the v_user_monthly_account_report database view and stores
 * the total credits, debits, payments, and amount to settle for a member.
 *
 * @author Tianzhu Li
 */
public class LedgerSummaryDTO {

    private Integer userId;
    private String name;
    private String email;
    private String reportMonth;
    private double totalCredits;
    private double totalDebits;
    private double totalPayments;
    private double amountToSettle;

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param userId the user ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Returns the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the month represented by the report.
     *
     * @return the report month
     */
    public String getReportMonth() {
        return reportMonth;
    }

    /**
     * Sets the month represented by the report.
     *
     * @param reportMonth the report month
     */
    public void setReportMonth(String reportMonth) {
        this.reportMonth = reportMonth;
    }

    /**
     * Returns the total credits earned by the user during the report month.
     *
     * @return the total credit amount
     */
    public double getTotalCredits() {
        return totalCredits;
    }

    /**
     * Sets the total credits earned by the user during the report month.
     *
     * @param totalCredits the total credit amount
     */
    public void setTotalCredits(double totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * Returns the total debits charged to the user during the report month.
     *
     * @return the total debit amount
     */
    public double getTotalDebits() {
        return totalDebits;
    }

    /**
     * Sets the total debits charged to the user during the report month.
     *
     * @param totalDebits the total debit amount
     */
    public void setTotalDebits(double totalDebits) {
        this.totalDebits = totalDebits;
    }

    /**
     * Returns the total payments made by the user during the report month.
     *
     * @return the total payment amount
     */
    public double getTotalPayments() {
        return totalPayments;
    }

    /**
     * Sets the total payments made by the user during the report month.
     *
     * @param totalPayments the total payment amount
     */
    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }

    /**
     * Returns the amount that the user must settle for the report month.
     *
     * @return the amount to settle
     */
    public double getAmountToSettle() {
        return amountToSettle;
    }

    /**
     * Sets the amount that the user must settle for the report month.
     *
     * @param amountToSettle the amount to settle
     */
    public void setAmountToSettle(double amountToSettle) {
        this.amountToSettle = amountToSettle;
    }
}