package clientsample.adapter.driven.persistence.client;

import clientsample.domain.client.Client;
import clientsample.domain.client.ClientRepository;
import clientsample.domain.corporateclient.CorporateClient;
import clientsample.domain.corporateclient.CorporateClientRepository;
import clientsample.domain.personalclient.PersonalClient;
import clientsample.domain.personalclient.PersonalClientRepository;

import java.util.Optional;

public class ClientRepositoryJdbc implements ClientRepository {
    final private CorporateClientRepository corporateClientRepository;
    final private PersonalClientRepository personalClientRepository;

    public ClientRepositoryJdbc(CorporateClientRepository corporateClientRepository
        , PersonalClientRepository personalClientRepository) {
        this.corporateClientRepository = corporateClientRepository;
        this.personalClientRepository = personalClientRepository;
    }

    @Override
    public Client save(Client client) {
        return switch (client) {
            case CorporateClient c -> corporateClientRepository.save(c);
            case PersonalClient p -> personalClientRepository.save(p);
            default -> throw new IllegalArgumentException("Unknown client type");
        };
    }

    @Override
    public Optional<Client> findById(Long id) {
        Optional<PersonalClient> p = personalClientRepository.findById(id);
        if (p.isPresent()) {
            return Optional.of(p.get());
        } else {
            return Optional.ofNullable(
                corporateClientRepository.findById(id).orElse(null)
            );
        }
    }
}
