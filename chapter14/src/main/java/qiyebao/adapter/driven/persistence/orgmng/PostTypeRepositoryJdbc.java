package qiyebao.adapter.driven.persistence.orgmng;

import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.domain.orgmng.posttype.PostType;
import qiyebao.domain.orgmng.posttype.PostTypeRepository;

@Repository
public class PostTypeRepositoryJdbc implements PostTypeRepository {
    private final JdbcHelper jdbc;

    public PostTypeRepositoryJdbc(JdbcHelper jdbc) {
        this.jdbc = jdbc;
     }

    @Override
    public boolean existsByCodeAndStatus(Long tenantId
        , String code
        , PostType.Status status
    ) {
        final String sql = "select 1"
                + " from post_type "
                + " where tenant_id = ? "
                + " and code = ?"
                + " and status_code = ?"
                + " limit 1";

        return jdbc.selectExists(sql, tenantId, code, status.getCode());
    }
}
