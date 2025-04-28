package qiyebao.common.framework.domain;

public class Persistent {
    private PersistentStatus persistentStatus;

    public Persistent() {
    }

    public Persistent(PersistentStatus status) {
        this.persistentStatus = status;
    }

    public PersistentStatus getPersistentStatus() {
        return persistentStatus;
    }

    public void toAsIs() {
        this.persistentStatus = PersistentStatus.AS_IS;
    }

    public void toNew() {
        this.persistentStatus = PersistentStatus.NEW;
    }

    public void toDeleted() {
        this.persistentStatus = PersistentStatus.DELETED;
    }

    public void toUpdated() {
        this.persistentStatus = PersistentStatus.UPDATED;
    }

    protected void asIsToUpdated() {
        if (this.persistentStatus == PersistentStatus.AS_IS) {
            this.toUpdated();
        }
    }
}
