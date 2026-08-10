package dataaccesslayer;

import transferobjects.ExternalClientDTO;

public interface ExternalClientDao {
    int addClient(ExternalClientDTO client);
    ExternalClientDTO getClientByEmail(String email);
    ExternalClientDTO getClientById(int clientId);
}
