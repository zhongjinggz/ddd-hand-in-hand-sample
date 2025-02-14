package qiyebao.common.framework.adapter.driven.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class Selector {
    private final JdbcTemplate jdbc;

    public Selector(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean selectExists(String sql, Object... params) {
        return !(jdbc.queryForList(sql, params)).isEmpty();
    }


    public <T> Optional<T> selectOne(String sql
            , Function<Map<String, Object>, T> toObj
            , Object... args) {

        List<T> objList = selectList(sql, toObj, args);
        return objList.isEmpty()
                ? Optional.empty()
                : Optional.of(objList.getFirst());
    }

    public <T> List<T> selectList(String sql
            , Function<Map<String, Object>, T> toObj
            , Object... args) {

        List<Map<String, Object>> maps = selectMaps(sql, args);
        return maps.stream().map(toObj).toList();
    }

    public List<Map<String, Object>> selectMaps(String sql
            , Object... args) {
        return jdbc.queryForList(sql, args);
    }
}
