package transferobjects;

import java.time.LocalDateTime;

/**
 * Represents an external client or service requester in the system.
 * @author Le Bao Thach Nguyen
 */
public class ExternalClientDTO {
    private Integer clientId;
    private String clientName;
    private String organization;
    private String phone;
    private String email;
    private LocalDateTime createdAt;

    /**
     * Returns the unique identifier of the client.
     *
     * @return the client identifier
     */
    public Integer getClientId() { return clientId; }

    /**
     * Sets the unique identifier of the client.
     *
     * @param clientId the client identifier to assign
     */
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    /**
     * Returns the display name of the client.
     *
     * @return the client name
     */
    public String getClientName() { return clientName; }

    /**
     * Sets the display name of the client.
     *
     * @param clientName the client name to assign
     */
    public void setClientName(String clientName) { this.clientName = clientName; }

    /**
     * Returns the organization associated with the client.
     *
     * @return the organization name
     */
    public String getOrganization() { return organization; }

    /**
     * Sets the organization associated with the client.
     *
     * @param organization the organization name to assign
     */
    public void setOrganization(String organization) { this.organization = organization; }

    /**
     * Returns the contact phone number for the client.
     *
     * @return the phone number
     */
    public String getPhone() { return phone; }

    /**
     * Sets the contact phone number for the client.
     *
     * @param phone the phone number to assign
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Returns the email address for the client.
     *
     * @return the email address
     */
    public String getEmail() { return email; }

    /**
     * Sets the email address for the client.
     *
     * @param email the email address to assign
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Returns the timestamp when the client record was created.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Sets the timestamp when the client record was created.
     *
     * @param createdAt the creation timestamp to assign
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
