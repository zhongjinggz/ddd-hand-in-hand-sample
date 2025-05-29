package clientsample.domain.corporateclient;

import clientsample.common.framework.domain.PersistentStatus;
import clientsample.domain.client.Client;

import java.time.LocalDateTime;

public class CorporateClient extends Client {
    public static final String CLIENT_TYPE_CORPORATE = "C";
    private String name;
    private String taxNum;

    public CorporateClient(PersistentStatus persistentStatus
        , LocalDateTime createdAt
        , Long createdBy
    ) {
        super(persistentStatus, createdAt, createdBy);
    }

    @Override
    public String getClientType() {
        return CLIENT_TYPE_CORPORATE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }
}
