package qiyebao.common.framework.domain;


public abstract class Persister<T extends AuditableEntity> {

    public T save(T theObject) {
        switch (theObject.getPersistentStatus()) {
            case NEW:
                insert(theObject);
                saveSubsidiaries(theObject);
                break;
            case UPDATED:
                update(theObject);
                saveSubsidiaries(theObject);
                break;
            case DELETED:
                deleteSubsidiaries(theObject);
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

    protected void saveSubsidiaries(T theObject) {
    }

    protected void deleteSubsidiaries(T theObject) {
    }

}
