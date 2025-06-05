package qiyebao.application.effortmng.effortitemservice;

import qiyebao.domain.effortmng.commoneffortitem.CommonEffortItem;
import qiyebao.domain.effortmng.effortitem.EffortItem;
import qiyebao.domain.effortmng.effortitem.EffortItemRepository;
import qiyebao.domain.projectmng.project.Project;
import qiyebao.domain.projectmng.project.SubProject;

import java.util.ArrayList;
import java.util.List;

import static qiyebao.application.effortmng.effortitemservice.EffortItemDto.Type.*;

public class EffortItemService {
    final private EffortItemRepository effortItemRepository;

    public EffortItemService(EffortItemRepository effortItemRepository) {
        this.effortItemRepository = effortItemRepository;
    }

    // 查询员工的工时项
    public List<EffortItemDto> findEffortItemsByEmp(Long empId) {
        List<EffortItem> effortItems = effortItemRepository.findByEmp(empId);

        List<EffortItemDto> result = new ArrayList<>();
        for (EffortItem item : effortItems) {
            result.add(buildEffortItemDto(item));
        }

        return result;
    }

    // 根据 EffortItem 构建 EffortItemDTO
    private EffortItemDto buildEffortItemDto(EffortItem item) {
        EffortItemDto.Type type = typeOf(item);

        EffortItemDto effortItemDto = new EffortItemDto(item.getEffortItemId()
            , item.getName()
            , type);

        // 递归构建子工时项
        List<? extends EffortItem> subItems = item.getSubEffortItems();
        for (EffortItem subItem : subItems) {
            EffortItemDto subEffortItemDto = buildEffortItemDto(subItem);
            effortItemDto.addSubItem(subEffortItemDto);
        }

        return effortItemDto;
    }

    // 根据工时项类型
    private EffortItemDto.Type typeOf(EffortItem effortItem) {
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
