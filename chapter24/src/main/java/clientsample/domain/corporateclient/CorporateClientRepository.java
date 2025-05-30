package clientsample.domain.corporateclient;

import clientsample.adapter.driven.persistence.CorporateClientRepositoryJdbc;

import java.util.Optional;

public interface CorporateClientRepository {
    CorporateClient save(CorporateClient corporateClient);
    Optional<CorporateClient> findById(Long id);
}
