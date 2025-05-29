package clientsample.domain.personalclient;

import clientsample.domain.corporateclient.CorporateClient;

import java.util.Optional;

public interface PersonalClientRepository {
    boolean save(PersonalClient personalClient);
    Optional<PersonalClient> findById(Long id);
}
