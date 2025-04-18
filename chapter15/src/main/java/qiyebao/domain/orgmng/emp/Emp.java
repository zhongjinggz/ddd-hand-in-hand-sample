package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.AuditInfo;
import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.common.framework.domain.PersistentStatus;
import qiyebao.common.framework.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static qiyebao.domain.orgmng.emp.Emp.Status.PROBATION;
import static qiyebao.domain.orgmng.emp.Emp.Status.TERMINATED;

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

    private Emp() {
    }

    Emp(Long tenantId, Status status, Long createdBy) {
        super(PersistentStatus.NEW, LocalDateTime.now(), createdBy);
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

    void setOrgId(Long orgId) {
        if (!Objects.equals(this.orgId, orgId)) {
            this.orgId = orgId;
            asIsToUpdated();
        }
    }

    public String getEmpNum() {
        return empNum;
    }

    void setEmpNum(String empNum) {
        if(!Objects.equals(this.empNum, empNum)) {
            this.empNum = empNum;
            asIsToUpdated();
        }
    }

    public String getIdNum() {
        return idNum;
    }

    void setIdNum(String idNum) {
        if (!Objects.equals(this.idNum, idNum)) {
            this.idNum = idNum;
            asIsToUpdated();
        }
    }


    public String getName() {
        return name;
    }

    void setName(String name) {
        if(!Objects.equals(this.name, name)) {
            this.name = name;
            asIsToUpdated();
        }
    }

    public Gender getGender() {
        return gender;
    }

    void setGender(Gender gender) {
        if(!Objects.equals(this.gender, gender)) {
            this.gender = gender;
            asIsToUpdated();
        }
    }

    public LocalDate getDob() {
        return dob;
    }

    void setDob(LocalDate dob) {
        if(!Objects.equals(this.dob, dob)) {
            this.dob = dob;
            asIsToUpdated();
        }
    }

    public Status getStatus() {
        return status;
    }

    //转正
    void becomeRegular() {
        // 调用业务规则: 试用期的员工才能被转正
        onlyProbationCanBecomeRegular();
        status = Status.REGULAR;
        asIsToUpdated();
    }

    // 实现业务规则: 试用期的员工才能被转正
    private void onlyProbationCanBecomeRegular() {
        if (status != PROBATION) {
            throw new BusinessException("试用期员工才能转正！");
        }
    }

    //终止
    void terminate() {
        // 调用业务规则: 已经终止的员工不能再次终止
        shouldNotTerminateAgain();
        status = TERMINATED;
        asIsToUpdated();
    }

    // 实现业务规则: 已经终止的员工不能再次终止
    private void shouldNotTerminateAgain() {
        if (status == TERMINATED) {
            throw new BusinessException("已经终止的员工不能再次终止！");
        }
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

        newSkill(skillTypeId
            , level
            , duration
            , new AuditInfo(LocalDateTime.now(), userId)
        ).toNew();

        asIsToUpdated();
        setUpdatedInfo(userId);
    }

    private Skill newSkill(Long skillTypeId
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
        return newSkill;
    }

    public void modifySkill(Long skillTypeId
        , Skill.Level level
        , Integer duration
        , Long userId
    ) {
        Skill skill = this.getSkill(skillTypeId)
            .orElseThrow(() -> new IllegalArgumentException("不存在要修改的skillTypeId!"));

        if (skill.getLevel() != level
            || !skill.getDuration().equals(duration)
        ) {
            skill.setLevel(level);
            skill.setDuration(duration);
            skill.setUpdatedBy(userId);
            skill.setUpdatedAt(LocalDateTime.now());
            skill.toUpdated();

            asIsToUpdated();
            setUpdatedInfo(userId);
        }
    }

    public void removeSkill(Long skillTypeId, Long userId) {
        this.getSkill(skillTypeId)
            .orElseThrow(() -> new IllegalArgumentException("中不存在要删除的skillTypeId!"))
            .toDeleted();

        asIsToUpdated();
        setUpdatedInfo(userId);
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

        newExperience(startDate
            , endDate
            , company
            , new AuditInfo(LocalDateTime.now(), userId)
        ).toNew();

        asIsToUpdated();
        setUpdatedInfo(userId);
    }

    private WorkExperience newExperience(LocalDate startDate
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

        experiences.add(newExperience);
        return newExperience;
    }

    public void modifyExperience(LocalDate startDate
        , LocalDate endDate
        , String company
        , Long userId
    ) {
        WorkExperience exp = this.getExperience(startDate, endDate)
            .orElseThrow(() -> new IllegalArgumentException("不存在要修改的 WorkExperience!"));

        if (!exp.getCompany().equals(company)) {
            exp.setCompany(company);
            exp.setUpdatedBy(userId);
            exp.setUpdatedAt(LocalDateTime.now());
            exp.toUpdated();

            asIsToUpdated();
            setUpdatedInfo(userId);
        }
    }

    public void removeExperience(LocalDate startDate, LocalDate endDate, Long userId) {
        this.getExperience(startDate, endDate)
            .orElseThrow(() -> new IllegalArgumentException("不存在要删除的WorkExperience!"))
            .toDeleted();

        asIsToUpdated();
        setUpdatedInfo(userId);
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
        newPost(postTypeCode
            , new AuditInfo(LocalDateTime.now(), userId)
        ).toNew();

        asIsToUpdated();
        setUpdatedInfo(userId);
    }

    private Post newPost(String postTypeCode, AuditInfo audit) {
        Post newPost = new Post(this
            , tenantId
            , postTypeCode
            , audit.getCreatedAt()
            , audit.getCreatedBy()
        );
        newPost.setUpdatedAt(audit.getUpdatedAt());
        newPost.setUpdatedBy(audit.getUpdatedBy());

        posts.add(newPost);

        return newPost;
    }


    void removePost(String postTypeCode, Long userId) {
        this.getPost(postTypeCode)
            .orElseThrow(() -> new IllegalArgumentException("不存在要删除的岗位!"))
            .toDeleted();

        asIsToUpdated();
        setUpdatedInfo(userId);
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

        public Loader loadSkill(Long skillTypeId
            , String levelCode
            , Integer duration
            , AuditInfo audit
        ) {
            emp.newSkill(
                    skillTypeId
                    , Skill.Level.ofCode(levelCode)
                    , duration
                    , audit)
                .toAsIs();

            return this;
        }

        public Loader loadExperience(LocalDate startDate
            , LocalDate endDate
            , String company
            , AuditInfo audit
        ) {
            emp.newExperience(
                    startDate
                    , endDate
                    , company
                    , audit)
                .toAsIs();

            return this;
        }

        public Loader loadPost(String postTypeCode, AuditInfo audit
        ) {
            emp.newPost(postTypeCode, audit)
                .toAsIs();
            return this;
        }

        public Emp load() {
            return this.emp;
        }

    }
}
