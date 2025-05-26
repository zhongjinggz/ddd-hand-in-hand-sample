package qiyebao.domain.effortmng.effortitem;

import java.util.List;

public interface EffortItemRepository {
    List<EffortItem> findByEmp(Long empId) ;
}
