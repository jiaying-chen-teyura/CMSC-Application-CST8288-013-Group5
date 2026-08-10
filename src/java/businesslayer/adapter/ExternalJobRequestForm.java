package businesslayer.adapter;

/**
 * Stand-in for a job-request payload coming from an outside source (a
 * public "request a fabrication job" web form, or a partner portal) that
 * external clients use to submit work, per FR "How job requests from
 * external clients are submitted". Field names/structure are whatever that
 * outside form happens to use - not our WorkOrderDTO shape. This is the
 * "adaptee" for the second required Adapter usage.
 */
public class ExternalJobRequestForm {
    private String firstName;
    private String lastName;
    private String organization;
    private String contactEmail;
    private String contactPhone;
    private String jobDetails;
    private boolean isUrgent;
    private double labourRate;

    public ExternalJobRequestForm(String firstName, String lastName, String organization, String contactEmail,
                                   String contactPhone, String jobDetails, boolean isUrgent, double labourRate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.organization = organization;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.jobDetails = jobDetails;
        this.isUrgent = isUrgent;
        this.labourRate = labourRate;
    }

    public String getContactName() { return ((firstName == null ? "" : firstName.trim()) + " " + (lastName == null ? "" : lastName.trim())).trim(); }
    public String getOrganization() { return organization; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public String getJobDetails() { return jobDetails; }
    public boolean isUrgent() { return isUrgent; }
    public double getLabourRate() { return labourRate; }
}
