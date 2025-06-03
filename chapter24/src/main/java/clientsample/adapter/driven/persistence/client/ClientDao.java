package clientsample.adapter.driven.persistence.client;

import clientsample.domain.client.Client;
import clientsample.domain.common.valueobject.Address;
import clientsample.domain.corporateclient.CorporateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import clientsample.common.framework.adapter.driven.persistence.JdbcHelper;

import java.util.Map;

import static qiyebao.common.utils.ReflectUtils.forceSet;

@Component
public class ClientDao {
    private final JdbcHelper jdbc;

    @Autowired
    public ClientDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "client", "id");
    }

    public void insert(Client client) {
        Address address = client.getAddress();

        Map<String, Object> params = Map.of(
            "client_type", client.getClientType()
            , "addr_country", address.getCountry()
            , "addr_province", address.getProvince()
            , "addr_city", address.getCity()
            , "addr_district", address.getDistrict()
            , "addr_detail", address.getDetail()
            , "version", 1L
            , "created_at", client.getCreatedAt()
            , "created_by", client.getCreatedBy()
        );

        Number createdId = jdbc.insertAndReturnKey(params);
        forceSet(client, "id", createdId.longValue());
    }

    public void update(Client client) {
        Address address = client.getAddress();
        String sql = """
            update client
               set version = version + 1
                 , addr_country =?
                 , addr_province =?
                 , addr_city =?
                 , addr_district =?
                 , addr_detail =?
                 , updated_at =?
                 , updated_by =?
              where id = ? and version = ?
            """;

        jdbc.optimisticUpdate(sql
            , address.getCountry()
            , address.getProvince()
            , address.getCity()
            , address.getDistrict()
            , address.getDetail()
            , client.getUpdatedAt()
            , client.getUpdatedBy()
            , client.getId()
            , client.getVersion());
    }

    public void delete(CorporateClient client) {
        jdbc.delete("""
                delete from corporate_client
                where id = ?
                """
            , client.getId());
    }
}
