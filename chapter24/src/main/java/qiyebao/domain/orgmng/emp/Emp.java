package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.*;
import qiyebao.common.framework.exception.BusinessException;
import qiyebao.domain.common.valueobject.DatePeriod;
import qiyebao.domain.common.valueobject.IndividualName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Emp extends AggregateRoot {
    private Long id;
    private Long tenantId;
    private Long orgId;

    private String empNum;
    private String idNum;

    private IndividualName name;
    private Gender gender;
    private LocalDate dob;
    private Status status;

    // final private List<Skill> skills = new ArrayList<>();
    final private Map<Long, Skill> skills = new HashMap<>();

    final private List<WorkExperience> experiences = new ArrayList<>();

    //    final private List<Post> posts = new ArrayList<>();
    final private Map<String,Post> posts = new HashMap<>();

    // 仅用于从数据库中加载数据或编写测试案例
    public static Loader loader() {
        return new Loader();
    }

    private Emp() {
    }

    Emp(Long tenantId, String statusCode, Long createdBy) {
        super(PersistentStatus.NEW, LocalDateTime.now(), createdBy);
        this.tenantId = tenantId;
        this.status = Status.addedAs(statusCode);
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
            modified();
        }
    }

    public String getEmpNum() {
        return empNum;
    }

    void setEmpNum(String empNum) {
        if (!Objects.equals(this.empNum, empNum)) {
            this.empNum = empNum;
            modified();
        }
    }

    public String getIdNum() {
        return idNum;
    }

    void setIdNum(String idNum) {
        if (!Objects.equals(this.idNum, idNum)) {
            idNumShouldValid(idNum);

            this.idNum = idNum;
            modified();
        }
    }

    private void idNumShouldValid(String idNum) {
        // TODO: 验证身份证
    }


    public IndividualName getName() {
        return name;
    }

    void setName(IndividualName name) {
        Objects.requireNonNull(name);
        this.name = name;
        modified();
    }

    public Gender getGender() {
        return gender;
    }

    void setGender(Gender gender) {
        if (!Objects.equals(this.gender, gender)) {
            this.gender = gender;
            modified();
        }
    }

    public LocalDate getDob() {
        return dob;
    }

    void setDob(LocalDate dob) {
        if (!Objects.equals(this.dob, dob)) {
            this.dob = dob;
            modified();
        }
    }

    public Status getStatus() {
        return status;
    }

    //转正
    void becomeRegular() {
        status = status.becameRegular();
        modified();
    }

    //终止
    void terminate() {
        status = status.terminated();
        modified();
    }

    // public List<Skill> getSkills() {
    public Collection<Skill> getSkills() {
        return Collections.unmodifiableCollection(skills.values());
    }

    public Optional<Skill> getSkill(Long skillTypeId) {
        //return skills.stream()
        //    .filter(s -> s.getSkillTypeId() == skillTypeId)
        //    .findAny();
        return Optional.ofNullable(skills.get(skillTypeId));
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
        ).added();

        modified();
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

        // skills.add(newSkill);
        skills.put(skillTypeId, newSkill);
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
            skill.modified();

            modified();
        }
    }

    public void removeSkill(Long skillTypeId) {
        this.getSkill(skillTypeId)
            .orElseThrow(() -> new IllegalArgumentException("中不存在要删除的skillTypeId!"))
            .removed();

        modified();
    }

    private void expectSkillTypeNotDuplicated(Long otherSkillTypeId) {
        //if (skills.stream().anyMatch(
        //    s -> s.getSkillTypeId() == otherSkillTypeId)) {
        //    throw new BusinessException("同一技能不能录入两次！");
        //}
        if (skills.get(otherSkillTypeId) != null) {
            throw new BusinessException("同一技能不能录入两次！");
        }
    }

    public List<WorkExperience> getExperiences() {
        return Collections.unmodifiableList(experiences);
    }

    public Optional<WorkExperience> getExperience(DatePeriod period) {
        return experiences.stream()
            .filter(e -> e.getPeriod().equals(period))
            .findAny();
    }

    void addExperience(DatePeriod period
        , String company
        , Long userId
    ) {
        // 调用业务规则: 工作经验的时间段不能重叠
        expectDurationNotOverlap(period);

        newExperience(period
            , company
            , new AuditInfo(LocalDateTime.now(), userId)
        ).added();

        modified();
    }

    private WorkExperience newExperience(DatePeriod period
        , String company
        , AuditInfo audit) {
        WorkExperience newExperience = new WorkExperience(this
            , tenantId
            , period
            , audit.getCreatedAt()
            , audit.getCreatedBy()
        );
        newExperience.setCompany(company);
        newExperience.setUpdatedAt(audit.getUpdatedAt());
        newExperience.setUpdatedBy(audit.getUpdatedBy());

        experiences.add(newExperience);
        return newExperience;
    }

    public void modifyExperience(DatePeriod period
        , String company
        , Long userId
    ) {
        WorkExperience exp = this.getExperience(period)
            .orElseThrow(() -> new IllegalArgumentException("不存在要修改的 WorkExperience!"));

        if (!exp.getCompany().equals(company)) {
            exp.setCompany(company);
            exp.setUpdatedBy(userId);
            exp.setUpdatedAt(LocalDateTime.now());
            exp.modified();

            modified();
        }
    }

    public void removeExperience(DatePeriod period) {
        this.getExperience(period)
            .orElseThrow(() -> new IllegalArgumentException("不存在要删除的WorkExperience!"))
            .removed();

        modified();
    }

    private void expectDurationNotOverlap(DatePeriod period) {
        if (experiences.stream().anyMatch(
            e -> e.getPeriod().isOverlapped(period))
        ) {
            throw new BusinessException("工作经验的时间段不能重叠!");
        }
    }

    public Collection<Post> getPosts() {
        return Collections.unmodifiableCollection(posts.values());
    }

    public Optional<Post> getPost(String postTypeCode) {
        return Optional.ofNullable(posts.get(postTypeCode));
    }

    void addPost(String postTypeCode, Long userId) {
        expectPostNotDuplicated(postTypeCode);
        newPost(postTypeCode
            , new AuditInfo(LocalDateTime.now(), userId)
        ).added();

        modified();
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

//        posts.add(newPost);
        posts.put(postTypeCode, newPost);
        return newPost;
    }


    void removePost(String postTypeCode, Long userId) {
        this.getPost(postTypeCode)
            .orElseThrow(() -> new IllegalArgumentException("不存在要删除的岗位!"))
            .removed();

        modified();
    }

    private void expectPostNotDuplicated(String postTypeCode) {
        if (posts.get(postTypeCode) != null) {
            throw new BusinessException("不能重复添加岗位!");
        }
    }

    // ==== internal classes ====

    public enum Status implements CodeEnum {
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

        static Status addedAs(String code) {
            Status asStatus = ofCode(code);
            if (!(PROBATION == asStatus) && !(REGULAR == asStatus)) {
                throw new BusinessException("添加的员工只能是正式工或处于试用期!");
            }
            return asStatus;
        }

        Status becameRegular() {
            if (this != PROBATION) {
                throw new BusinessException("试用期员工才能转正！");
            }
            return REGULAR;
        }

        Status terminated() {
            if (this == TERMINATED) {
                throw new BusinessException("已经终止的员工不能再次终止！");
            }
            return TERMINATED;
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
            emp.name = IndividualName.of(name);
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

        public Loader version(Long version) {
            emp.setVersion(version);
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
                .loaded();

            return this;
        }

        public Loader loadExperience(LocalDate startDate
            , LocalDate endDate
            , String company
            , AuditInfo audit
        ) {
            emp.newExperience(
                    DatePeriod.of(startDate, endDate)
                    , company
                    , audit)
                .loaded();

            return this;
        }

        public Loader loadPost(String postTypeCode, AuditInfo audit
        ) {
            emp.newPost(postTypeCode, audit)
                .loaded();
            return this;
        }

        public Emp load() {
            emp.loaded();
            return this.emp;
        }
    }
}
