package qiyebao.adapter.driven.persistence.projectmng.project;

import org.springframework.stereotype.Repository;
import qiyebao.domain.projectmng.project.Project;
import qiyebao.domain.projectmng.project.ProjectRepository;
import java.util.List;

@Repository
public class ProjectRepositoryJdbc implements ProjectRepository {
    @Override
    public List<Project> findAssignmentsByEmp(Long empId) {
        //TODO
        return null;
    }

    @Override
    public List<Project> findProjectsNotRequireAssignment() {
        //TODO
        return null;
    }
}
