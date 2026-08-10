package dataaccesslayer;

import transferobjects.ExternalClientDTO;

/**
 * Defines persistence operations for external client records.
 * @author Le Bao Thach Nguyen 
 */
public interface ExternalClientDao {
    /**
     * Persists a new external client record.
     *
     * @param client the client to save
     * @return the generated client identifier
     */
    int addClient(ExternalClientDTO client);

    /**
     * Retrieves a client by email address.
     *
     * @param email the email address to look up
     * @return the matching client, or null if it does not exist
     */
    ExternalClientDTO getClientByEmail(String email);

    /**
     * Retrieves a client by unique identifier.
     *
     * @param clientId the client identifier to look up
     * @return the matching client, or null if it does not exist
     */
    ExternalClientDTO getClientById(int clientId);
}
