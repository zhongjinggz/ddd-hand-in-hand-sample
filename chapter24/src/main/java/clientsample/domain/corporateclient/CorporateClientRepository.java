package clientsample.domain.corporateclient;

import java.util.Optional;

public interface CorporateClientRepository {
    CorporateClient save(CorporateClient corporateClient);
    Optional<CorporateClient> findById(Long id);
}
