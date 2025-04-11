package qiyebao.adapter.driven.persistence.orgmng.emp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.domain.orgmng.emp.Post;

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

}
