package qiyebao.common.framework.domain;


import static qiyebao.common.framework.domain.PersistentStatus.NEW;
import static qiyebao.common.framework.domain.PersistentStatus.UPDATED;

public abstract class Persister<T extends Persistent> {

    public T save(T theObject) {
        switch (theObject.getPersistentStatus()) {
            case NEW:
                insert(theObject);
                theObject.toAsIs();
                saveSubEntries(theObject);
                break;
            case UPDATED:
                update(theObject);
                theObject.toAsIs();
                saveSubEntries(theObject);
                break;
            case DELETED:
                removeSubEntries(theObject);
                delete(theObject);
                break;
        }

        return theObject;
    }

    protected void insert(T theObject) {
    }

    protected void update(T theObject) {
    }

    protected void delete(T theObject) {
    }

    protected void saveSubEntries(T theObject) {
    }

    protected void removeSubEntries(T theObject) {
    }

}
