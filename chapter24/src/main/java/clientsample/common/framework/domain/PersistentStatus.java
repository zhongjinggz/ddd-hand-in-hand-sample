package clientsample.common.framework.domain;

import clientsample.common.framework.exception.SystemException;

public enum PersistentStatus {
    NEW            // 新增的，数据库还没有，需要 insert
    , AS_IS        // 数据库中已经存在，没有变化，因此不需要任何操作
    , UPDATED      // 数据库中已经存在，发生了变化，需要 update
    , DELETED;      // 数据库中已经存在，要删除，需要 delete

    public static PersistentStatus added() {
        return NEW;
    }

    public static PersistentStatus loaded() {
        return AS_IS;
    }

    public PersistentStatus saved() {
        switch (this) {
            case NEW, AS_IS, UPDATED:
                return AS_IS;
            case DELETED:
                return null;
            default:
                return null;
        }
    }

    public PersistentStatus removed() {
        switch (this) {
            case NEW:
                throw new SystemException("对象未保存，不能删除！");
            case AS_IS, UPDATED:
                return DELETED;
            case DELETED:
                throw new SystemException("对象已删除，不能再删除！");
            default:
                return null;
        }
    }

    public PersistentStatus modified() {
        switch (this) {
            case NEW:
                return NEW;
            case AS_IS, UPDATED:
                return UPDATED;
            case DELETED:
                throw new SystemException("对象已删除，不能修改！");
            default:
                return null;
        }
    }
}
