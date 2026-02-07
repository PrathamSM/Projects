package com.smeportal.projects_service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Projects")
public class Projects {

    @Id
    @Column(name = "project_id", nullable = false, unique = true, length = 10) // 10 characters
    private String projectId; // Alphanumeric Project ID

    @Column(name = "project_name", nullable = false) // Project Name
    private String projectName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "project_start_date", nullable = false)
    private LocalDate projectStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "project_end_date", nullable = true)
    private LocalDate projectEndDate;

    @Column(name = "project_contact_person", nullable = true)
    private String projectContactPerson;

    @Column(name = "project_contact_email", nullable = true)
    private String projectContactEmail;

    @Column(name = "project_contact_phone", nullable = true)
    private String projectContactPhone;

    @Column(name = "project_location", nullable = true)
    private String projectLocation;

    @Column(name = "project_description", columnDefinition = "TEXT", nullable = true)
    private String projectDescription;

    @Column(name = "planned_budget", nullable = true)
    private Double plannedBudget;

    @Column(name = "billing_model", nullable = true)
    private String billingModel;

    @Column(name = "budget_alert_threshold", nullable = true)
    private Double budgetAlertThreshold;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "currency", nullable = true)
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Lifecycle Hooks
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getProjectContactPerson() {
        return projectContactPerson;
    }

    public void setProjectContactPerson(String projectContactPerson) {
        this.projectContactPerson = projectContactPerson;
    }

    public String getProjectContactEmail() {
        return projectContactEmail;
    }

    public void setProjectContactEmail(String projectContactEmail) {
        this.projectContactEmail = projectContactEmail;
    }

    public String getProjectContactPhone() {
        return projectContactPhone;
    }

    public void setProjectContactPhone(String projectContactPhone) {
        this.projectContactPhone = projectContactPhone;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Double getPlannedBudget() {
        return plannedBudget;
    }

    public void setPlannedBudget(Double plannedBudget) {
        this.plannedBudget = plannedBudget;
    }

    public String getBillingModel() {
        return billingModel;
    }

    public void setBillingModel(String billingModel) {
        this.billingModel = billingModel;
    }

    public Double getBudgetAlertThreshold() {
        return budgetAlertThreshold;
    }

    public void setBudgetAlertThreshold(Double budgetAlertThreshold) {
        this.budgetAlertThreshold = budgetAlertThreshold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public String toString() {
        return "Projects{" +
                "plannedBudget=" + plannedBudget +
                ", projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectStartDate=" + projectStartDate +
                ", projectEndDate=" + projectEndDate +
                ", projectContactPerson='" + projectContactPerson + '\'' +
                ", projectContactEmail='" + projectContactEmail + '\'' +
                ", projectContactPhone='" + projectContactPhone + '\'' +
                ", projectLocation='" + projectLocation + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", billingModel='" + billingModel + '\'' +
                ", budgetAlertThreshold=" + budgetAlertThreshold +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", currency='" + currency + '\'' +
                '}';
    }
}

