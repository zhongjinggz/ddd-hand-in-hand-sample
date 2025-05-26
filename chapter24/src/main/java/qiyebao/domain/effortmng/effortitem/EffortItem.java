package qiyebao.domain.effortmng.effortitem;

import java.util.Collections;
import java.util.List;

public interface EffortItem {
    Long getEffortItemId();
    String getName();
    default List<? extends EffortItem> getSubEffortItems() {
        return Collections.emptyList();
    };
}
