package service;

import dto.ConsumableDTO;

/**
 * Immutable result of a {@link ConsumableService} write operation. Models
 * validation failure (missing fields, a duplicate material name, a negative
 * rate) as a normal return value rather than an exception, since those are
 * expected outcomes of bad user input, not exceptional system conditions —
 * the same reasoning already applied to {@code EquipmentResult} in the
 * Equipment module: exceptions are for unexpected conditions (a lost DB
 * connection), not for "this material name is already taken."
 *
 * @author Le Bao Thach Nguyen
 * @version 1.0
 */
public class ConsumableResult {

    private final boolean success;
    private final String message;
    private final ConsumableDTO consumable;

    private ConsumableResult(boolean success, String message, ConsumableDTO consumable) {
        this.success = success;
        this.message = message;
        this.consumable = consumable;
    }

    /**
     * Creates a successful result.
     *
     * @param consumable the consumable record the operation succeeded on
     * @return a success result carrying the consumable record
     */
    public static ConsumableResult success(ConsumableDTO consumable) {
        return new ConsumableResult(true, "Success.", consumable);
    }

    /**
     * Creates a failed result.
     *
     * @param message a human-readable explanation of the failure
     * @return a failure result carrying only the error message
     */
    public static ConsumableResult failure(String message) {
        return new ConsumableResult(false, message, null);
    }

    /** @return whether the operation succeeded */
    public boolean isSuccess() {
        return success;
    }

    /** @return a success or failure message suitable for display */
    public String getMessage() {
        return message;
    }

    /** @return the affected consumable record, or {@code null} on failure */
    public ConsumableDTO getConsumable() {
        return consumable;
    }
}
