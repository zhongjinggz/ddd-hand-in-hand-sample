package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.framework.domain.Persister;
import qiyebao.common.utils.TypedMap;
import qiyebao.domain.orgmng.emp.Emp;
import qiyebao.domain.orgmng.emp.Post;

import java.util.List;
import java.util.Map;

@Component
public class PostDao extends Persister<Post> {

    private final JdbcHelper jdbc;

    public PostDao(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "post");
    }

    @Override
    protected void insert(Post post) {
        Map<String, Object> parms = Map.of(
            "emp_id", post.getEmp().getId()
            , "post_type_code", post.getPostTypeCode()
            , "created_at", post.getCreatedAt()
            , "created_by", post.getCreatedBy());

        jdbc.insert(parms);
    }

    @Override
    protected void delete(Post post) {
        jdbc.delete("""
                delete from post 
                where tenant_id = ? 
                  and emp_id = ?
                  and post_type_code = ?
                """
            , post.getTenantId()
            , post.getEmp().getId()
            , post.getPostTypeCode());
    }

    void deleteByEmpId(Emp emp, Long empId) {
        jdbc.delete("""
                delete from post 
                where tenant_id = ? and emp_id = ?
                """
            , emp.getTenantId()
            , empId);
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
}
