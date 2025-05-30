package clientsample.domain.personalclient;

import clientsample.common.framework.domain.PersistentStatus;
import clientsample.domain.client.Client;

import java.time.LocalDateTime;
import java.util.Objects;

public class PersonalClient extends Client {
    public static final String CLIENT_TYPE_PERSONAL = "P";
    private String name;
    private String idNum;

    //用于新增(这时还没有id)
    public PersonalClient(LocalDateTime createdAt, Long createdBy) {
        super(PersistentStatus.added(), null, createdAt, createdBy);
    }

    //用于从数据库加载(这时已经有id)
    public PersonalClient(Long id
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(PersistentStatus.loaded()
            , Objects.requireNonNull(id)
            , createdAt
            , createdBy);
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
