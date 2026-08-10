package businesslayer.strategy;

/**
 * Provides the context for the credit calculation Strategy pattern.
 * Stores the currently selected credit strategy and delegates credit
 * calculations to that strategy.
 *
 * @author Tianzhu Li
 */
public class CreditContext {

    private CreditStrategy strategy;

    /**
     * Creates a credit context without an initial strategy.
     * A strategy must be assigned before calculating credit.
     */
    public CreditContext() {
    }

    /**
     * Creates a credit context with the specified calculation strategy.
     *
     * @param strategy the credit strategy to use
     */
    public CreditContext(CreditStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Changes the credit calculation strategy used by this context.
     *
     * @param strategy the credit strategy to use
     */
    public void setStrategy(CreditStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Calculates credit by delegating the calculation to the currently
     * selected strategy.
     *
     * @param quantity the quantity used to calculate credit
     * @return the calculated credit amount
     * @throws IllegalStateException if no credit strategy has been assigned
     */
    public double computeCredit(double quantity) {
        if (strategy == null) {
            throw new IllegalStateException(
                    "CreditContext: no CreditStrategy set"
            );
        }

        return strategy.calculateCredit(quantity);
    }
}