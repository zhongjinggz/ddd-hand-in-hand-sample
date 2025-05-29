package qiyebao.domain.projectmng.project;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import qiyebao.common.framework.domain.AggregateRoot;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.domain.common.valueobject.DatePeriod;
import qiyebao.domain.effortmng.effortitem.EffortItem;

import static qiyebao.domain.projectmng.project.Project.Status.TODO;

public class Project extends AggregateRoot
    implements EffortItem {

    private final Long tenantId;
    private final Long effortItemId;
    private String name;

    private DatePeriod period;


    private Status status;
    private Boolean clientProject;
    private Boolean shouldAssignMember;
    private EffortGranularity effortGranularity;
    private ProjectMng currentMng;


    private final List<ProjectMng> mngs = new ArrayList<>();
    private final List<SubProject> subProjects = new ArrayList<>();

    public Project(Long tenantId
        , Long effortItemId
        , String name
        , Boolean clientProject
        , Boolean shouldAssignMember
        , LocalDate startAt
        , EffortGranularity effortGranularity
        , Long createdBy) {

        super(PersistentStatus.NEW, LocalDateTime.now(), createdBy);
        this.tenantId = tenantId;
        this.effortItemId = effortItemId;
        this.name = name;
        this.clientProject = clientProject;
        this.shouldAssignMember = shouldAssignMember;
        this.period = DatePeriod.startAt(startAt);
        this.status = TODO;
        this.effortGranularity = effortGranularity;
    }

    @Override
    public Long getEffortItemId() {
        return effortItemId;
    }

    @Override
    public String getName() {
        return name;
    }

    public String setName() {
        return this.name = name;
    }

    public List<SubProject> getSubProjects() {
        return Collections.unmodifiableList(subProjects);
    }

    @Override
    public List<? extends EffortItem> getSubEffortItems() {
        return getSubProjects();
    }

    public enum Status {
        TODO, DOING, DONE
    }

    public enum EffortGranularity {
        PROJECT, SUB_PROJECT, BOTH
    }
}
