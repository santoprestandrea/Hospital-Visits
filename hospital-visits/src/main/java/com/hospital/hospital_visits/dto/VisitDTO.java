package com.hospital.hospital_visits.dto;

import java.time.LocalDate;
import java.util.Set;

/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class VisitDTO {

    private Long id;
    private LocalDate date;

    private Long departmentId;
    private String departmentName;

    private Long visitTypeId;
    private String visitTypeName;

    private Set<String> doctorCfs;   // Doctor PK = cf
    private Set<String> patientCfs;  // Patient PK = cf

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Long getVisitTypeId() { return visitTypeId; }
    public void setVisitTypeId(Long visitTypeId) { this.visitTypeId = visitTypeId; }

    public String getVisitTypeName() { return visitTypeName; }
    public void setVisitTypeName(String visitTypeName) { this.visitTypeName = visitTypeName; }

    public Set<String> getDoctorCfs() { return doctorCfs; }
    public void setDoctorCfs(Set<String> doctorCfs) { this.doctorCfs = doctorCfs; }

    public Set<String> getPatientCfs() { return patientCfs; }
    public void setPatientCfs(Set<String> patientCfs) { this.patientCfs = patientCfs; }
}
