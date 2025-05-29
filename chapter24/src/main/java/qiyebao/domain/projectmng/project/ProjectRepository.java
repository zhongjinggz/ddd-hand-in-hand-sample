package qiyebao.domain.projectmng.project;

import java.util.List;

public interface ProjectRepository {
    List<Project> findAssignmentsByEmp(Long empId);
    List<Project> findProjectsNotRequireAssignment();
}
