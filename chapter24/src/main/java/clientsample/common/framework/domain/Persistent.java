package clientsample.common.framework.domain;

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

    public void loaded() {
        persistentStatus = PersistentStatus.loaded();
    }

    public void added() {
        persistentStatus = PersistentStatus.added();
    }

    public void removed() {
        persistentStatus = persistentStatus.removed();
    }

     public void modified() {
        persistentStatus = persistentStatus.modified();
    }

    public void saved() {
        persistentStatus = persistentStatus.saved();
    }
}
