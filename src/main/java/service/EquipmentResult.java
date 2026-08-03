package service;

import dto.EquipmentDTO;

/**
 * Immutable result of an {@link EquipmentService} write operation. Models
 * validation failure (missing fields, a duplicate asset tag, an invalid
 * rate) as a normal return value rather than an exception, since those are
 * expected outcomes of bad user input, not exceptional system conditions —
 * the same reasoning already applied to {@code RegistrationResult} in
 * Assignment 2: exceptions are for unexpected conditions (a lost DB
 * connection), not for "this asset tag is already taken."
 *
 * @author Oladimeji Durojaiye
 * @version 1.0
 */
public class EquipmentResult {

    private final boolean success;
    private final String message;
    private final EquipmentDTO equipment;

    private EquipmentResult(boolean success, String message, EquipmentDTO equipment) {
        this.success = success;
        this.message = message;
        this.equipment = equipment;
    }

    /**
     * Creates a successful result.
     *
     * @param equipment the equipment record the operation succeeded on
     * @return a success result carrying the equipment record
     */
    public static EquipmentResult success(EquipmentDTO equipment) {
        return new EquipmentResult(true, "Success.", equipment);
    }

    /**
     * Creates a failed result.
     *
     * @param message a human-readable explanation of the failure
     * @return a failure result carrying only the error message
     */
    public static EquipmentResult failure(String message) {
        return new EquipmentResult(false, message, null);
    }

    /** @return whether the operation succeeded */
    public boolean isSuccess() {
        return success;
    }

    /** @return a success or failure message suitable for display */
    public String getMessage() {
        return message;
    }

    /** @return the affected equipment record, or {@code null} on failure */
    public EquipmentDTO getEquipment() {
        return equipment;
    }
}
