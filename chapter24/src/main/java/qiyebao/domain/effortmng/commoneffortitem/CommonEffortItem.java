package qiyebao.domain.effortmng.commoneffortitem;

import qiyebao.common.framework.domain.AggregateRoot;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.domain.effortmng.effortitem.EffortItem;

import java.time.LocalDateTime;

public class CommonEffortItem extends AggregateRoot
        implements EffortItem {
    private final Long tenantId;
    private Long id;

    private Long effortItemId;
    private String name;

    // 用于新建工时记录
    public CommonEffortItem(Long tenantId
            , String name
            , Long createdBy) {

        super(PersistentStatus.NEW, LocalDateTime.now(), createdBy);
        this.tenantId = tenantId;
        this.name = name;
    }

    //用于从数据库重建工时记录
    public CommonEffortItem(Long tenantId
            , Long id
            , Long effortItemId
            , String name
                            , Long createdBy
            , LocalDateTime createdAt) {

        this(tenantId,  name, createdBy);
        this.effortItemId = effortItemId;
        this.id = id;
        this.createdAt = createdAt;
    }

    @Override
    public Long getEffortItemId() {
        return effortItemId;
    }

    @Override
    public String getName() {
        return name;
    }
}
