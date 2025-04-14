package qiyebao.domain.orgmng.emp;

import qiyebao.common.framework.domain.AuditableEntity;
import qiyebao.common.framework.domain.CodeEnum;
import qiyebao.common.framework.exception.BusinessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class Emp extends AuditableEntity {
    private Long id;
    final private Long tenantId;
    private Long orgId;

    private String empNum;
    private String idNum;

    private String name;
    private Gender gender;
    private LocalDate dob;
    private Status status;

    final private List<Skill> skills = new ArrayList<>();
    final private List< WorkExperience> experiences = new ArrayList<>();
    final private List<Post> posts = new ArrayList<>();

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

    public void addSkill(Long skillTypeId
        , Skill.Level level
        , Integer duration
        , Long userId
    ) {
        // 固定规则: 同一技能不能录入两次
        expectSkillTypeNotDuplicated(skillTypeId);

        Skill newSkill = new Skill(this
            , tenantId
            , skillTypeId
            , LocalDateTime.now()
            , userId);
        newSkill.setLevel(level);
        newSkill.setDuration(duration);

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

    public void addExperience(LocalDate startDate
        , LocalDate endDate
        , String company
        , Long userId
    ) {
        // 调用业务规则: 工作经验的时间段不能重叠
        expectDurationNotOverlap(startDate, endDate);

        WorkExperience newExperience = new WorkExperience(this
            , tenantId
            , startDate
            , endDate
            , LocalDateTime.now()
            , userId);
        newExperience.setCompany(company);

        experiences.add(newExperience);
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

    public void addPost(String postTypeCode, Long userId) {
        Post newPost = new Post(this
            , tenantId
            , postTypeCode
            , LocalDateTime.now()
            , userId);

        posts.add(newPost);
    }

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
    }
}
