package businesslayer.strategy;

/**
 * Calculates the credit awarded to a Shop-Tech for time spent performing
 * preventive or repair maintenance. Maintenance work earns a fixed amount
 * of credit for each hour logged.
 *
 * @author Tianzhu Li
 */
public class MaintenanceCreditStrategy implements CreditStrategy {

    private static final double CREDIT_PER_HOUR = 15.0;

    /**
     * Calculates the credit earned from maintenance work.
     *
     * @param hoursLogged the number of maintenance hours logged
     * @return the calculated maintenance credit rounded to two decimal places
     */
    @Override
    public double calculateCredit(double hoursLogged) {
        return round2(hoursLogged * CREDIT_PER_HOUR);
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