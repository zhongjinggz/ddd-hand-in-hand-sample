package qiyebao.common.framework.adapter.driven.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

public interface JdbcTemplateConsumer {
    JdbcTemplate getJdbc();
}
