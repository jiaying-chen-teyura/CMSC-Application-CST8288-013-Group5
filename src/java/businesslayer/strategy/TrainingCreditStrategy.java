package businesslayer.strategy;

/**
 * Calculates the credit awarded to a trainer for delivering an introductory
 * or safety training session. The trainer receives a base credit amount plus
 * an additional credit amount for each attendee.
 *
 * @author Tianzhu Li
 */
public class TrainingCreditStrategy implements CreditStrategy {

    private static final double BASE_CREDIT = 20.0;
    private static final double CREDIT_PER_ATTENDEE = 4.0;

    /**
     * Calculates the credit earned for conducting a training session.
     *
     * @param attendeeCount the number of people who attended the session
     * @return the calculated training credit rounded to two decimal places
     */
    @Override
    public double calculateCredit(double attendeeCount) {
        return round2(
                BASE_CREDIT + attendeeCount * CREDIT_PER_ATTENDEE
        );
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