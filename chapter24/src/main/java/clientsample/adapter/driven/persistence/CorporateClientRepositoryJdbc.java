package clientsample.adapter.driven.persistence;

import clientsample.domain.corporateclient.CorporateClient;
import clientsample.domain.corporateclient.CorporateClientRepository;
import clientsample.common.framework.domain.Persister;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CorporateClientRepositoryJdbc
    extends Persister<CorporateClient>
    implements CorporateClientRepository {

    final ClientDao clientDao;
    final CorporateClientDao corporateClientDao;

    @Autowired
    public CorporateClientRepositoryJdbc(ClientDao clientDao
            , CorporateClientDao corporateClientDao) {
        this.clientDao = clientDao;
        this.corporateClientDao = corporateClientDao;
    }

    @Override
    protected void insert(CorporateClient client) {
        clientDao.insert(client);
        corporateClientDao.insert(client);
    }

    @Override
    protected void update(CorporateClient client) {
        clientDao.update(client);
        corporateClientDao.update(client);
    }


    @Override
    public Optional<CorporateClient> findById(Long id) {
        return corporateClientDao.selectById(id);
    }
}
