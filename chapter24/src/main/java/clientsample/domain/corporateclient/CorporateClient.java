package clientsample.domain.corporateclient;

import clientsample.common.framework.domain.PersistentStatus;
import clientsample.domain.client.Client;

import java.time.LocalDateTime;

public class CorporateClient extends Client {
    public static final String CLIENT_TYPE_CORPORATE = "C";
    private String name;
    private String taxNum;

    // 用于新增
    public CorporateClient(LocalDateTime createdAt, Long createdBy) {
        super(PersistentStatus.added(), null, createdAt, createdBy);
    }

    // 用于从数据库加载
    public CorporateClient(Long id, LocalDateTime createdAt, Long createdBy) {
        super(PersistentStatus.loaded(), id, createdAt, createdBy);
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
