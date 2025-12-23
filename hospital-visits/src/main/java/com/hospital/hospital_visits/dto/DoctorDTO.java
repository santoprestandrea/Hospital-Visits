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
public class DoctorDTO {

    private String cf;
    private String firstName;
    private String surname;
    private String address;
    private LocalDate birthDate;
    private String email;

    private Long departmentId;
    private String departmentName;

    private Long specializationId;
    private String specializationName;

    private Set<Long> clinicRoomIds; // works_in
    private Set<Long> performedVisitIds; // performed

    public String getCf() { return cf; }
    public void setCf(String cf) { this.cf = cf; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Long getSpecializationId() { return specializationId; }
    public void setSpecializationId(Long specializationId) { this.specializationId = specializationId; }

    public String getSpecializationName() { return specializationName; }
    public void setSpecializationName(String specializationName) { this.specializationName = specializationName; }

    public Set<Long> getClinicRoomIds() { return clinicRoomIds; }
    public void setClinicRoomIds(Set<Long> clinicRoomIds) { this.clinicRoomIds = clinicRoomIds; }

    public Set<Long> getPerformedVisitIds() { return performedVisitIds; }
    public void setPerformedVisitIds(Set<Long> performedVisitIds) { this.performedVisitIds = performedVisitIds; }
}
