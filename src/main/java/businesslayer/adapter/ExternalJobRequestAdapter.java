package businesslayer.adapter;

import businesslayer.builder.WorkOrderBuilder;
import dataaccesslayer.ExternalClientDao;
import dataaccesslayer.ExternalClientDaoImpl;
import transferobjects.ExternalClientDTO;
import transferobjects.WorkOrderDTO;

/**
 * Adapter Pattern (required pattern), second usage.
 * Converts an ExternalJobRequestForm (the outside world's shape for "a
 * client wants a fabrication job done") into a WorkOrderDTO built with the
 * Builder pattern, resolving/creating the matching external_clients row
 * along the way. SubmitWorkOrderCommand calls this instead of hand-rolling
 * client lookup + WorkOrderDTO assembly itself.
 *
 * Used by: SubmitWorkOrderCommand (External Client actor path).
 * @author Le Bao Thach Nguyen 
 */
public class ExternalJobRequestAdapter {

    private final ExternalClientDao clientDao = new ExternalClientDaoImpl();

    public WorkOrderDTO adapt(ExternalJobRequestForm form) {
        ExternalClientDTO client = clientDao.getClientByEmail(form.getContactEmail());
        if (client == null) {
            client = new ExternalClientDTO();
            client.setClientName(form.getContactName());
            client.setOrganization(form.getOrganization());
            client.setEmail(form.getContactEmail());
            client.setPhone(form.getContactPhone());
            int newId = clientDao.addClient(client);
            client.setClientId(newId);
        }

        return new WorkOrderBuilder()
                .forExternalClient(client.getClientId())
                .description(form.getJobDetails())
                .priority(form.isUrgent() ? WorkOrderDTO.Priority.RUSH : WorkOrderDTO.Priority.STANDARD)
                .estimatedLabourCost(form.getLabourRate())
                .build();
    }
}
