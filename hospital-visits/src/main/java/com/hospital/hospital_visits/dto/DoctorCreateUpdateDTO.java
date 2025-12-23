package com.hospital.hospital_visits.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;
/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class DoctorCreateUpdateDTO {

    @NotBlank
    @Size(min = 16, max = 16)
    private String cf;

    @NotBlank
    @Size(max = 60)
    private String firstName;

    @NotBlank
    @Size(max = 60)
    private String surname;

    @Size(max = 120)
    private String address;

    private LocalDate birthDate;

    @Email
    @Size(max = 100)
    private String email;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long specializationId;

    private Set<Long> clinicRoomIds;

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

    public Long getSpecializationId() { return specializationId; }
    public void setSpecializationId(Long specializationId) { this.specializationId = specializationId; }

    public Set<Long> getClinicRoomIds() { return clinicRoomIds; }
    public void setClinicRoomIds(Set<Long> clinicRoomIds) { this.clinicRoomIds = clinicRoomIds; }
}
