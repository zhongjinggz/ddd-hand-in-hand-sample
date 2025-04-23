package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.Post;

import java.util.List;
import java.util.Map;

@Component
public class PostDao {

    private final JdbcHelper jdbc;

    public PostDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "post");
    }

    Post insert(Post post) {
        Map<String, Object> parms = Map.of(
            "emp_id", post.getEmp().getId()
            , "post_type_code", post.getPostTypeCode()
            , "created_at", post.getCreatedAt()
            , "created_by", post.getCreatedBy());

        jdbc.insert(parms);
        return post;

    }

    public List<TypedMap> selectByEmpId(Long tenantId, Long empId) {
        var sql = """
            select post_type_code
                 , created_at
                 , created_by
                 , updated_at
                 , updated_by
            from post
            where tenant_id = ? 
              and emp_id = ?
            """;

        return jdbc.selectMapList(sql, tenantId, empId);
    }

    public void save(Post post) {

    }

    void deleteByEmpId(Emp emp) {
        jdbc.delete("""
                delete from post 
                where tenant_id = ? and emp_id = ?
                """
            , emp.getTenantId()
            , emp.getId());
    }
}
