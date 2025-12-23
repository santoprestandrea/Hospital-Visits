package com.hospital.hospital_visits.dto;

import java.util.Set;
/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class ClinicRoomDTO {
    private Long id;
    private String address;
    private Set<String> doctorCfs;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Set<String> getDoctorCfs() { return doctorCfs; }
    public void setDoctorCfs(Set<String> doctorCfs) { this.doctorCfs = doctorCfs; }
}
