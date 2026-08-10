package businesslayer;

import java.time.LocalDateTime;

/**
 * Provides shared validation for scheduling operations.
 * Ensures that scheduled times fall exactly on a 15-minute interval,
 * including :00, :15, :30, or :45, with no seconds or nanoseconds.
 *
 * @author Tianzhu Li
 */
public class TimeSlotValidation {

    /**
     * Prevents this utility class from being instantiated.
     */
    private TimeSlotValidation() {
    }

    /**
     * Validates that a date and time falls on a 15-minute interval.
     * A null value is ignored so that required-field validation can be
     * handled by the calling business logic.
     *
     * @param dateTime the date and time to validate
     * @param fieldLabel the field name used in the validation message
     * @throws ValidationException if the time is not on a 15-minute interval
     */
    public static void validateQuarterHourSlot(
            LocalDateTime dateTime,
            String fieldLabel) throws ValidationException {

        if (dateTime == null) {
            return;
        }

        if (dateTime.getMinute() % 15 != 0
                || dateTime.getSecond() != 0
                || dateTime.getNano() != 0) {

            throw new ValidationException(
                    fieldLabel
                    + " must be on a 15-minute slot "
                    + "(:00, :15, :30, or :45)."
            );
        }
    }
}