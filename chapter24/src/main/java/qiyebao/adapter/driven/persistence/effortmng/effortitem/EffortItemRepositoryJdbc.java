package qiyebao.adapter.driven.persistence.effortmng.effortitem;

import org.springframework.stereotype.Repository;
import qiyebao.domain.effortmng.commoneffortitem.CommonEffortItemRepository;
import qiyebao.domain.effortmng.effortitem.EffortItem;
import qiyebao.domain.effortmng.effortitem.EffortItemRepository;
import qiyebao.domain.projectmng.project.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EffortItemRepositoryJdbc implements EffortItemRepository {
    private ProjectRepository  projectRepository;
    private CommonEffortItemRepository commonEffortItemRepository;

    public EffortItemRepositoryJdbc(ProjectRepository projectRepository
            , CommonEffortItemRepository commonEffortItemRepository) {
        this.projectRepository = projectRepository;
        this.commonEffortItemRepository = commonEffortItemRepository;
    }

    @Override
    public List<EffortItem> findByEmp(Long empId) {
        List<EffortItem> result = new ArrayList<>();

        result.addAll(projectRepository.findAssignmentsByEmp(empId));
        result.addAll(projectRepository.findInternalProjects());
        result.addAll(commonEffortItemRepository.findAll());

        return result;
    }
}
