package clientsample.domain.client;

import clientsample.domain.corporateclient.CorporateClient;

import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    Optional<Client> findById(Long id);
}
