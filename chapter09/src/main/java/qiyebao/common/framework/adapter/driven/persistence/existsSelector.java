package qiyebao.common.framework.adapter.driven.persistence;

public interface existsSelector extends JdbcTemplateConsumer {
    default boolean selectExists(String sql, Object[] params) {
        return !(getJdbc().queryForList(sql, params)).isEmpty();
    }
}
