package clientsample.domain.personalclient;

import clientsample.domain.corporateclient.CorporateClient;

import java.util.Optional;

public interface PersonalClientRepository {
    PersonalClient save(PersonalClient personalClient);
    Optional<PersonalClient> findById(Long id);
}
