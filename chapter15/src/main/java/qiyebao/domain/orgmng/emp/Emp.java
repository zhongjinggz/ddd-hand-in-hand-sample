package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.AuditInfo;
import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.common.framework.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class Emp extends AuditableEntity {
    private Long id;
    private Long tenantId;
    private Long orgId;

    private String empNum;
    private String idNum;

    private String name;
    private Gender gender;
    private LocalDate dob;
    private Status status;

    final private List<Skill> skills = new ArrayList<>();
    final private List<WorkExperience> experiences = new ArrayList<>();
    final private List<Post> posts = new ArrayList<>();

    // 仅用于从数据库中加载数据或编写测试案例
    public static Loader loader() {
        return new Loader();
    }

    public static Loader loader(Emp emp) {
        return new Loader(emp);

    }

    public Emp() {
    }

    public Emp(Long tenantId, Status status, Long createdBy) {
        super(LocalDateTime.now(), createdBy);
        this.tenantId = tenantId;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Status getStatus() {
        return status;
    }

    public void becomeRegular() {
        status = Status.REGULAR;
    }

    public void terminate() {
        status = Status.TERMINATED;
    }

    public List<Skill> getSkills() {
        return Collections.unmodifiableList(skills);

    }

    public Optional<Skill> getSkill(Long skillTypeId) {
        return skills.stream()
            .filter(s -> s.getSkillTypeId() == skillTypeId)
            .findAny();
    }

    void addSkill(Long skillTypeId
        , Skill.Level level
        , Integer duration
        , Long userId
    ) {
        // 固定规则: 同一技能不能录入两次
        expectSkillTypeNotDuplicated(skillTypeId);

        addSkillInternal(skillTypeId
            , level
            , duration
            , new AuditInfo(LocalDateTime.now(), userId)
        );
    }

    private void addSkillInternal(Long skillTypeId
        , Skill.Level level
        , Integer duration
        , AuditInfo audit
    ) {
        Skill newSkill = new Skill(this
            , tenantId
            , skillTypeId
            , audit.getCreatedAt()
            , audit.getCreatedBy()
        );
        newSkill.setLevel(level);
        newSkill.setDuration(duration);
        newSkill.setUpdatedAt(audit.getUpdatedAt());
        newSkill.setUpdatedBy(audit.getUpdatedBy());

        skills.add(newSkill);
    }

    private void expectSkillTypeNotDuplicated(Long otherSkillTypeId) {
        if (skills.stream().anyMatch(
            s -> s.getSkillTypeId() == otherSkillTypeId)) {
            throw new BusinessException("同一技能不能录入两次！");
        }
    }

    public List<WorkExperience> getExperiences() {
        return Collections.unmodifiableList(experiences);
    }

    public Optional<WorkExperience> getExperience(LocalDate startDate
        , LocalDate endDate) {
        return experiences.stream()
            .filter(e -> e.getStartDate().equals(startDate)
                && e.getEndDate().equals(endDate))
            .findAny();
    }

    void addExperience(LocalDate startDate
        , LocalDate endDate
        , String company
        , Long userId
    ) {
        // 调用业务规则: 工作经验的时间段不能重叠
        expectDurationNotOverlap(startDate, endDate);

        WorkExperience newExperience = addExperienceInternal(startDate
            , endDate
            , company
            , new AuditInfo(LocalDateTime.now(), userId)
        );

        experiences.add(newExperience);
    }

    private WorkExperience addExperienceInternal(LocalDate startDate
        , LocalDate endDate
        , String company
        , AuditInfo audit) {
        WorkExperience newExperience = new WorkExperience(this
            , tenantId
            , startDate
            , endDate
            , audit.getCreatedAt()
            , audit.getCreatedBy()
        );
        newExperience.setCompany(company);
        newExperience.setUpdatedAt(audit.getUpdatedAt());
        newExperience.setUpdatedBy(audit.getUpdatedBy());

        return newExperience;
    }

    private void expectDurationNotOverlap(LocalDate startDate, LocalDate endDate) {
        if (experiences.stream().anyMatch(
            e -> overlap(e, startDate, endDate))) {
            throw new BusinessException("工作经验的时间段不能重叠!");
        }
    }

    private boolean overlap(WorkExperience experience
        , LocalDate otherStart, LocalDate otherEnd) {
        LocalDate thisStart = experience.getStartDate();
        LocalDate thisEnd = experience.getEndDate();

        return otherStart.isBefore(thisEnd)
            && otherEnd.isAfter(thisStart);
    }

    public List<Post> getPosts() {
        return Collections.unmodifiableList(posts);
    }

    public Optional<Post> getPost(String postTypeCode) {
        return posts.stream()
            .filter(p -> p.getPostTypeCode().equals(postTypeCode))
            .findAny();
    }

    void addPost(String postTypeCode, Long userId) {
        expectPostNotDuplicated(postTypeCode);
        addPostInternal(postTypeCode
            , new AuditInfo(LocalDateTime.now(), userId)
        );
    }

    private void addPostInternal(String postTypeCode, AuditInfo audit) {
        Post newPost = new Post(this
            , tenantId
            , postTypeCode
            , audit.getCreatedAt()
            , audit.getCreatedBy()
        );
        newPost.setUpdatedAt(audit.getUpdatedAt());
        newPost.setUpdatedBy(audit.getUpdatedBy());

        posts.add(newPost);
    }

    private void expectPostNotDuplicated(String postTypeCode) {
        if (posts.stream().anyMatch(
            post ->
                post.getPostTypeCode().equals(postTypeCode))
        ) {
            throw new BusinessException("不能重复添加岗位!");
        }
    }

    // ==== internal classes ====

    public static enum Status implements CodeEnum {
        REGULAR("REG", "正式"),
        PROBATION("PRO", "试用期"),
        TERMINATED("TER", "终止");

        private final String code;
        private final String desc;

        Status(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static Status ofCode(String code) {
            return CodeEnum.ofCode(Status.values(), code);
        }

        public static boolean isValidCode(String statusCode) {
            return CodeEnum.isValidCode(Status.values(), statusCode);
        }

        @Override
        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }


    // 用于从数据库加载
    public static class Loader {
        Emp emp;

        private Loader() {
            this.emp = new Emp();
        }

        public Loader(Emp emp) {
            this.emp = emp;
        }

        public Loader tenantId(Long tenantId) {
            emp.tenantId = tenantId;
            return this;
        }

        public Loader id(Long id) {
            emp.id = id;
            return this;
        }

        public Loader orgId(Long orgId) {
            emp.orgId = orgId;
            return this;
        }

        public Loader idNum(String idNum) {
            emp.idNum = idNum;
            return this;
        }

        public Loader empNum(String empNum) {
            emp.empNum = empNum;
            return this;
        }

        public Loader dob(LocalDate dob) {
            emp.dob = dob;
            return this;
        }

        public Loader name(String name) {
            emp.name = name;
            return this;
        }

        public Loader genderCode(String genderCode) {
            emp.gender = Gender.ofCode(genderCode);
            return this;
        }

        public Loader statusCode(String statusCode) {
            emp.status = Status.ofCode(statusCode);
            return this;
        }

        public Loader createdBy(Long createdBy) {
            emp.createdBy = createdBy;
            return this;
        }

        public Loader createdAt(LocalDateTime createdAt) {
            emp.createdAt = createdAt;
            return this;
        }

        public Loader updatedBy(Long updatedBy) {
            emp.updatedBy = updatedBy;
            return this;
        }

        public Loader updatedAt(LocalDateTime updatedAt) {
            emp.updatedAt = updatedAt;
            return this;
        }

        public Loader addSkill(Long skillTypeId
            , String levelCode
            , Integer duration
            , AuditInfo audit
        ) {
            emp.addSkillInternal(skillTypeId
                , Skill.Level.ofCode(levelCode)
                , duration
                , audit
            );
            return this;
        }

        public Loader addExperience(LocalDate startDate
            , LocalDate endDate
            , String company
            , AuditInfo audit
        ) {
            emp.addExperienceInternal(startDate
                , endDate
                , company
                , audit);

            return this;
        }

        public Loader addPost(String postTypeCode
            , AuditInfo audit
        ) {
            emp.addPostInternal(postTypeCode, audit);
            return this;
        }

        public Emp load() {
            return this.emp;
        }

    }

}
