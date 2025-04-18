package qiyebao.common.framework.domain;

public enum PersistentStatus {
    NEW            // 新增的，数据库还没有，需要 insert
    , AS_IS        // 数据库中已经存在，没有变化，因此不需要任何操作
    , UPDATED      // 数据库中已经存在，发生了变化，需要 update
    , DELETED      // 数据库中已经存在，要删除，需要 delete
}
