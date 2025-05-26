package qiyebao.adapter.driven.persistence.effortmng.commoneffortitem;

import org.springframework.stereotype.Repository;
import qiyebao.domain.effortmng.commoneffortitem.CommonEffortItem;
import qiyebao.domain.effortmng.commoneffortitem.CommonEffortItemRepository;

import java.util.List;

@Repository
public class CommonEffortItemRepositoryJdbc implements CommonEffortItemRepository {
    @Override
    public List<CommonEffortItem> findAll() {
        return null;
    }
}
