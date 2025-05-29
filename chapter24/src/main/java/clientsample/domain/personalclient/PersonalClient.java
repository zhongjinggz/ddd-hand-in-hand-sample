package clientsample.domain.personalclient;

import clientsample.common.framework.domain.PersistentStatus;
import clientsample.domain.client.Client;

import java.time.LocalDateTime;

public class PersonalClient extends Client {
    public static final String CLIENT_TYPE_PERSONAL = "P";
    private String name;
    private String idNum;

    public PersonalClient(PersistentStatus persistentStatus
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(persistentStatus, createdAt, createdBy);
    }

    @Override
    public String getClientType() {
        return CLIENT_TYPE_PERSONAL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
}
