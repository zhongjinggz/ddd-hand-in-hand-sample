package qiyebao.application.effortmng.effortitemservice;

import qiyebao.domain.effortmng.commoneffortitem.CommonEffortItem;
import qiyebao.domain.effortmng.effortitem.EffortItem;
import qiyebao.domain.effortmng.effortitem.EffortItemRepository;
import qiyebao.domain.projectmng.project.Project;
import qiyebao.domain.projectmng.project.SubProject;

import java.util.ArrayList;
import java.util.List;

import static qiyebao.application.effortmng.effortitemservice.EffortItemDTO.Type.*;

public class EffortItemService {
    final private EffortItemRepository effortItemRepository;

    public EffortItemService(EffortItemRepository effortItemRepository) {
        this.effortItemRepository = effortItemRepository;
    }

    // 查询员工的工时项
    public List<EffortItemDTO> findEffortItemsByEmp(Long empId) {
        List<EffortItem> effortItems = effortItemRepository.findByEmp(empId);

        List<EffortItemDTO> result = new ArrayList<>();
        for (EffortItem item : effortItems) {
            result.add(buildEffortItemDTO(item));
        }

        return result;
    }

    // 根据 EffortItem 构建 EffortItemDTO
    private EffortItemDTO buildEffortItemDTO(EffortItem item) {
        EffortItemDTO.Type type = typeOf(item);

        EffortItemDTO effortItemDTO = new EffortItemDTO(item.getEffortItemId()
            , item.getName()
            , type);

        // 递归构建子工时项
        List<? extends EffortItem> subItems = item.getSubEffortItems();
        for (EffortItem subItem : subItems) {
            EffortItemDTO subEffortItemDTO = buildEffortItemDTO(subItem);
            effortItemDTO.addSubItem(subEffortItemDTO);
        }

        return effortItemDTO;
    }

    // 根据工时项类型
    private EffortItemDTO.Type typeOf(EffortItem effortItem) {
        return switch (effortItem) {
            case Project p -> PROJECT;
            case SubProject s -> SUB_PROJECT;
            case CommonEffortItem c -> COMMON;
            default ->
                throw new IllegalArgumentException("Unknown effort item type: "
                    + effortItem.getClass().getName());
        };
    }
}
