package businesslayer.strategy;

/**
 * Calculates the credit awarded to a member for donating consumable
 * materials. The donated quantity is multiplied by a configurable
 * credit-per-unit rate.
 *
 * @author Tianzhu Li
 */
public class DonationCreditStrategy implements CreditStrategy {

    private final double creditPerUnit;

    /**
     * Creates a donation credit strategy with the specified credit rate.
     *
     * @param creditPerUnit the credit awarded for each donated unit
     */
    public DonationCreditStrategy(double creditPerUnit) {
        this.creditPerUnit = creditPerUnit;
    }

    /**
     * Calculates the credit earned from a consumable material donation.
     *
     * @param quantityDonated the quantity of material donated
     * @return the calculated donation credit rounded to two decimal places
     */
    @Override
    public double calculateCredit(double quantityDonated) {
        return round2(quantityDonated * creditPerUnit);
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