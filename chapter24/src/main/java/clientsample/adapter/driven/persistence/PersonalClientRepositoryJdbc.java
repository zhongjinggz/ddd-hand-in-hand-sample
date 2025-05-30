package clientsample.adapter.driven.persistence;

import clientsample.common.framework.domain.Persister;
import clientsample.domain.personalclient.PersonalClient;
import clientsample.domain.personalclient.PersonalClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersonalClientRepositoryJdbc
    extends Persister<PersonalClient>
    implements PersonalClientRepository {

    final ClientDao clientDao;
    final PersonalClientDao personalClientDao;

    @Autowired
    public PersonalClientRepositoryJdbc(ClientDao clientDao
            , PersonalClientDao personalClientDao){
        this.clientDao = clientDao;
        this.personalClientDao = personalClientDao;
    }

    @Override
    protected void insert(PersonalClient client) {
        clientDao.insert(client);
        personalClientDao.insert(client);
    }

    @Override
    protected void update(PersonalClient client) {
        clientDao.update(client);
        personalClientDao.update(client);
    }


    @Override
    public Optional<PersonalClient> findById(Long id) {
        return personalClientDao.selectById(id);
    }
}
