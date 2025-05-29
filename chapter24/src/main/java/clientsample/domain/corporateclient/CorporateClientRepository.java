package clientsample.domain.corporateclient;

import java.util.Optional;

public interface CorporateClientRepository {
    boolean save(CorporateClient corporateClient);
    Optional<CorporateClient> findById(Long id);
}
