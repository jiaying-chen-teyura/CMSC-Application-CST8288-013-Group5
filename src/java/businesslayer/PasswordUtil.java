package businesslayer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides utility methods for hashing and verifying user passwords.
 *
 * Passwords are converted into SHA-256 hashes before they are stored
 * or compared. Plain-text passwords are not stored in the database.
 */
public final class PasswordUtil {

    /**
     * Prevents the utility class from being instantiated.
     */
    private PasswordUtil() {
    }

    /**
     * Converts a plain-text password into a SHA-256 hash.
     *
     * @param plainText the plain-text password
     * @return the hexadecimal SHA-256 hash
     */
    public static String hash(String plainText) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] bytes = digest.digest(
                    plainText.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hashBuilder = new StringBuilder();

            for (byte currentByte : bytes) {
                hashBuilder.append(
                        String.format("%02x", currentByte)
                );
            }

            return hashBuilder.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is not available.",
                    exception
            );
        }
    }

    /**
     * Determines whether a plain-text password matches a stored hash.
     *
     * @param plainText the plain-text password entered by the user
     * @param storedHash the password hash stored in the database
     * @return true if the password matches; otherwise false
     */
    public static boolean matches(
            String plainText,
            String storedHash) {

        return storedHash != null
                && storedHash.equalsIgnoreCase(hash(plainText));
    }
}