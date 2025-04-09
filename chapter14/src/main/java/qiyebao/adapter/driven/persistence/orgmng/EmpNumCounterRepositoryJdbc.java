package qiyebao.adapter.driven.persistence.orgmng;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import qiyebao.common.framework.adapter.driven.persistence.JdbcHelper;
import qiyebao.common.framework.exception.SystemException;
import qiyebao.domain.orgmng.empnumcounter.EmpNumCounter;
import qiyebao.domain.orgmng.empnumcounter.EmpNumCounterRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class EmpNumCounterRepositoryJdbc implements EmpNumCounterRepository {
    private final JdbcHelper jdbc;


    public EmpNumCounterRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbc = new JdbcHelper(jdbcTemplate, "emp_num_counter");
    }

    @Override
    public EmpNumCounter save(EmpNumCounter empNumCounter) {
        Map<String, Object> params = new HashMap<>(3);

        params.put("tenant_id", empNumCounter.getTenantId());
        params.put("year_num", empNumCounter.getYearNum());
        params.put("max_emp_num", empNumCounter.getMaxEmpNum());

        jdbc.insert(params);
        return empNumCounter;
    }

    @Override
    public Optional<EmpNumCounter> findByYear(Long tenantId, Integer yearNum) {
        final String sql = " select tenant_id"
                + ", year_num"
                + ", max_emp_num"
                + " from emp_num_counter "
                + " where tenant_id = ?  and year_num = ? ";

        return jdbc.selectOne(sql, this::mapToEmpNumCounter, tenantId, yearNum);
    }

    private EmpNumCounter mapToEmpNumCounter(ResultSet rs, int rowNum) throws SQLException {
        return new EmpNumCounter(
                rs.getLong("tenant_id")
                , rs.getInt("year_num")
                , rs.getInt("max_emp_num"));
    }

    @Override
    public Integer nextNumByYear(Long tenantId, Integer yearNum) {
        final String increaseSql = " update emp_num_counter "
                + " set max_emp_num = max_emp_num + 1 "
                + " where tenant_id = ?  and year_num = ? ";

        int rowsAffected = jdbc.update(increaseSql, tenantId, yearNum);

        if (rowsAffected == 0) {
            throw new SystemException("租户ID为'" + tenantId + "'的年份为'" + yearNum + "'的员工编号计数器不存在！");
        }

        final String selectSql = " select max_emp_num "
                + " from emp_num_counter "
                + " where tenant_id = ?  and year_num = ? ";
        return jdbc.selectOne(selectSql, Integer.class, tenantId, yearNum)
            .orElseThrow((() -> new SystemException("未知的数据访问错误")));

    }
}
