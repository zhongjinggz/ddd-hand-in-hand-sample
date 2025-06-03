package clientsample.adapter.driven.persistence.personalclient;

import clientsample.common.framework.adapter.driven.persistence.JdbcHelper;
import clientsample.domain.common.valueobject.Address;
import clientsample.domain.personalclient.PersonalClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static qiyebao.common.utils.SqlUtils.toLocalDateTime;


@Component
public class PersonalClientDao {

    private final JdbcHelper jdbc;

    public PersonalClientDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "personal_client");
    }

    void insert(PersonalClient client) {
        jdbc.insert(Map.<String, Object>of(
            "id", requireNonNull(client.getId())
            , "name", client.getName()
            , "id_num", client.getIdNum()
            , "created_at", client.getCreatedAt()
            , "created_by", client.getCreatedBy()
        ));
    }

    void update(PersonalClient client) {
        String sql = """
            update personal_client 
               set name = ?
                 , id_num =? 
                 , updated_at =?
                 , updated_by =? 
             where id = ? 
            """;
        jdbc.update(sql
            , client.getName()
            , client.getIdNum()
            , client.getUpdatedAt()
            , client.getUpdatedBy()
            , client.getId());
    }

    Optional<PersonalClient> selectById(Long id) {
        String sql = """
            select c.version
                 , c.addr_country
                 , c.addr_province
                 , c.addr_city
                 , c.addr_district
                 , c.addr_detail
                 , pc.name
                 , pc.id_num
                 , pc.created_at
                 , pc.created_by
                 , pc.update_at
                 , pc.updated_by
             from personal_client as pc
               left join client  as c
               on c.id = pc.id 
             where c.id = ?
               and c.client_type = 'P' 
            """;

        return jdbc.selectOne(sql, this::mapToPersonalClient, id);
    }

    private PersonalClient mapToPersonalClient(ResultSet rs, int rowNum)
        throws SQLException {

        PersonalClient result = new PersonalClient(
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
        result.setIdNum(rs.getString("id_num"));
        result.setUpdatedAt(toLocalDateTime(rs, "updated_at"));
        result.setUpdatedBy(rs.getLong("updated_by"));

        return result;
    }
}
