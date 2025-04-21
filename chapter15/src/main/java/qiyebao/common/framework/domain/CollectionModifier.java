package qiyebao.common.framework.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// M - C 的集合管理器（Collection Manager）类型
// C - 当前集合（需要被变更的集合）中条目的类型
// R - 请求集合中条目的类型 （根据R变更C）
public abstract class CollectionModifier<M, C, R> {

    public void modify(M manager, Collection<R> reqItems, Long userId) {
        Collection<R> reqItemsCopy = new ArrayList<>(reqItems); // 创建请求集合的副本
        Collection<C> currItems = getCurrItems(manager);        // 获取当前集合

        for (C currItem : currItems) {
            boolean found = false;
            Iterator<R> reqIterator = reqItemsCopy.iterator();
            while (reqIterator.hasNext()) {
                R reqItem = reqIterator.next();
                if (isSame(currItem, reqItem)) {
                    modifyItem(manager, reqItem, userId);
                    found = true;
                    reqIterator.remove(); // 删除请求集合副本中的相应元素
                    break;
                }
            }
            if (!found) {
                removeItem(manager, currItem, userId);
            }
        }

        for (R requestItem : reqItemsCopy) {
            addItem(manager, requestItem, userId);
        }
    }

    protected abstract Collection<C> getCurrItems(M manager);

    protected abstract boolean isSame(C currItem, R reqItem);

    protected abstract void modifyItem(M manager, R reqItem, Long userId);

    protected abstract void removeItem(M manager, C currItem, Long userId);
    
    protected abstract void addItem(M manager, R reqItem, Long userId);
}
