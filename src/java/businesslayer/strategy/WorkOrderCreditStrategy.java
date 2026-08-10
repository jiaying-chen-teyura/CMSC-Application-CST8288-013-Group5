package businesslayer.strategy;

/**
 * Calculates the credit awarded to a Shop-Tech for completing a work order.
 * The credit is based on the quoted labour cost, with an additional bonus
 * applied when the work order is marked as a rush job.
 *
 * @author Tianzhu Li
 */
public class WorkOrderCreditStrategy implements CreditStrategy {

    // The Shop-Tech receives 100 percent of the quoted labour as credit.
    private static final double LABOUR_CREDIT_SHARE = 1.0;

    private final boolean rush;

    /**
     * Creates a work-order credit strategy.
     *
     * @param rush true if the work order is a rush job; otherwise false
     */
    public WorkOrderCreditStrategy(boolean rush) {
        this.rush = rush;
    }

    /**
     * Calculates the credit earned for completing a work order.
     * Rush work orders receive a 25 percent bonus.
     *
     * @param quotedLabourCost the quoted labour cost of the work order
     * @return the calculated work-order credit rounded to two decimal places
     */
    @Override
    public double calculateCredit(double quotedLabourCost) {
        double base = quotedLabourCost * LABOUR_CREDIT_SHARE;
        return round2(rush ? base * 1.25 : base);
    }

    /**
     * Rounds a numeric value to two decimal places.
     *
     * @param v the value to round
     * @return the value rounded to two decimal places
     */
    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}