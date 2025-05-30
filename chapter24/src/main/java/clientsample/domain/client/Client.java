package clientsample.domain.client;

import clientsample.common.framework.domain.AggregateRoot;
import clientsample.common.framework.domain.PersistentStatus;
import clientsample.domain.common.valueobject.Address;

import java.time.LocalDateTime;

public abstract class Client extends AggregateRoot {
    private Long id;
    private Address address;

    protected Client(PersistentStatus persistentStatus
        , Long id
        , LocalDateTime createdAt
        , Long createdBy) {
        super(persistentStatus, createdAt, createdBy);
        this.id = id;
    }

    public abstract String getClientType();

    protected void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
