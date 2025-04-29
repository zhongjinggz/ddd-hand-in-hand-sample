package qiyebao.application.orgmng.empservice.dto;

import qiyebao.domain.orgmng.emp.WorkExperience;

import java.time.LocalDate;

public class WorkExperienceDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String company;

    public WorkExperienceDto() {
    }

    public WorkExperienceDto(LocalDate startDate, LocalDate endDate, String company) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.company = company;
    }

    public WorkExperienceDto(WorkExperience experience) {
        this(experience.getPeriod().getStart()
            , experience.getPeriod().getEnd()
            , experience.getCompany());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
