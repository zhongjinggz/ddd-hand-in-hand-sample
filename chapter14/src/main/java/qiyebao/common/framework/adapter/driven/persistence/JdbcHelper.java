package qiyebao.common.framework.adapter.driven.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import qiyebao.common.framework.exception.SystemException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcHelper {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert jdbcInsert;
    private final String[] keyColumns;

    public JdbcHelper(JdbcTemplate jdbc
        , String tableName
        , String... keyColumns
    ) {
        this.jdbc = jdbc;
        this.keyColumns = keyColumns;

        this.jdbcInsert = new SimpleJdbcInsert(jdbc)
            .withTableName(tableName);
        if (keyColumns != null && keyColumns.length > 0) {
            this.jdbcInsert.usingGeneratedKeyColumns(keyColumns);
        }
    }

    public int insert(Map<String, ?> params) {
        return jdbcInsert.execute(params);
    }

    public Number insertAndReturnKey(Map<String, ?> params) {
        return jdbcInsert.executeAndReturnKey(params);
    }

    public int delete(String sql, Object... params) {
        return jdbc.update(sql, params);
    }

    public int update(String sql, Object... params) {
        return jdbc.update(sql, params);
    }

    public int optimisticUpdate(String sql, Object... params) {
        return jdbc.update(sql, params);
    }

    public boolean selectExists(String sql, Object... params) {
        return !(jdbc.queryForList(sql, params).isEmpty());
    }

    public <T> List<T> selectList(String sql
        , Class<T> elementType, Object... params) {

        return jdbc.queryForList(sql, elementType, params);
    }

    public <T> Optional<T> selectOne(String sql
        , Class<T> elementType, Object... params) {

        List<T> objList = selectList(sql, elementType, params);

        if (objList.size() > 1) {
            throw new SystemException("selectOne() return more than one record!");
        }

        return objList.isEmpty() ? Optional.empty() : Optional.of(objList.getFirst());
    }

    public <T> List<T> selectList(String sql
        , RowMapper<T> rowMapper, Object... params) {

        return jdbc.query(sql, rowMapper, params);
    }

    public <T> Optional<T> selectOne(String sql
        , RowMapper<T> rowMapper, Object... params) {

        List<T> objList = selectList(sql, rowMapper, params);

        if (objList.size() > 1) {
            throw new SystemException("selectOne() return more than one record!");
        }

        return objList.isEmpty() ? Optional.empty() : Optional.of(objList.getFirst());
    }
}
