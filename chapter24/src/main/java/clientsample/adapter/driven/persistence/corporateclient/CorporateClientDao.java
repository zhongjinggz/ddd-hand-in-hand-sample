package clientsample.adapter.driven.persistence.corporateclient;

import clientsample.common.framework.adapter.driven.persistence.JdbcHelper;
import clientsample.domain.common.valueobject.Address;
import clientsample.domain.corporateclient.CorporateClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static qiyebao.common.utils.SqlUtils.toLocalDateTime;

@Component
public class CorporateClientDao {

    private final JdbcHelper jdbc;

    public CorporateClientDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "corporate_client");
    }

    void insert(CorporateClient client) {
        Map<String, Object> params = Map.of(
            "id", client.getId()
            , "name", client.getName()
            , "tax_num", client.getTaxNum()
            , "created_at", client.getCreatedAt()
            , "created_by", client.getCreatedBy()
        );

        jdbc.insert(params);
    }

    void update(CorporateClient client) {
        String sql = """
            update corporate_client
            set name = ?
              , tax_num =?
              , last_updated_at =?
              , last_updated_by =?
             where id = ?
            """;

        jdbc.update(sql
            , client.getName()
            , client.getTaxNum()
            , client.getUpdatedAt()
            , client.getUpdatedBy()
            , client.getId());
    }

    Optional<CorporateClient> selectById(Long id) {
        String sql = """
            select c.version
                 , c.addr_country
                 , c.addr_province
                 , c.addr_city
                 , c.addr_district
                 , c.addr_detail
                 , cc.name
                 , cc.tax_num
                 , cc.created_at
                 , cc.created_by
                 , cc.update_at
                 , cc.updated_by
            from client as c
            left join corporate_client  as cc
              on c.id = cc.id
            where c.id = ? and c.client_type = 'C'
            """;

        return jdbc.selectOne(sql, this::mapToCorporateClient, id);
    }

    private CorporateClient mapToCorporateClient(ResultSet rs, int rowNum)
        throws SQLException {

        CorporateClient result = new CorporateClient(
            rs.getLong("id")
            , toLocalDateTime(rs, "created_at")
            , rs.getLong("created_by")
        );

        result.setAddress(new Address(
            rs.getString("addr_country")
            , rs.getString("addr_province")
            , rs.getString("addr_city")
            , rs.getString("addr_district")
            , rs.getString("addr_detail")
        ));
        result.setName(rs.getString("name"));
        result.setTaxNum(rs.getString("tax_num"));
        result.setUpdatedAt(toLocalDateTime(rs, "updated_at"));
        result.setUpdatedBy(rs.getLong("updated_by"));

        return result;
    }

    public void delete(CorporateClient client) {
        jdbc.delete("""
                delete from corporate_client 
                where id = ?
                """
            , client.getId()
        );
    }
}
