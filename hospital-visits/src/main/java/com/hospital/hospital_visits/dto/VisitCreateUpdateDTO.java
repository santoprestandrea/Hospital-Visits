package com.hospital.hospital_visits.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class VisitCreateUpdateDTO {

    @NotNull
    private LocalDate date;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long visitTypeId;

    // medici assegnati alla visita (tabella performed)
    private Set<String> doctorCfs;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getVisitTypeId() { return visitTypeId; }
    public void setVisitTypeId(Long visitTypeId) { this.visitTypeId = visitTypeId; }

    public Set<String> getDoctorCfs() { return doctorCfs; }
    public void setDoctorCfs(Set<String> doctorCfs) { this.doctorCfs = doctorCfs; }
}
