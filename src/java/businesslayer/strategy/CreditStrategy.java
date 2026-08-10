package businesslayer.strategy;

/**
 * Defines the Strategy pattern interface for calculating member credits.
 * Different contribution activities use different credit calculations,
 * allowing the appropriate calculation strategy to be selected at runtime
 * without placing all calculation rules in the business logic classes.
 *
 * @author Tianzhu Li
 */
public interface CreditStrategy {

    /**
     * Calculates the credit earned from a contribution quantity.
     *
     * @param quantity the quantity used to calculate the credit, such as
     *                 donated units or contributed hours
     * @return the calculated credit amount
     */
    double calculateCredit(double quantity);
}