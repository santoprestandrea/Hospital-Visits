package com.hospital.hospital_visits.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
/*
*
* creation of the objects that we will need as a
* "transfer" (dto) for the REST CONTROLLER classes,
*  which will allow us to run the apis
*
* */
public class ClinicRoomCreateUpdateDTO {

    @NotBlank
    @Size(max = 120)
    private String address;

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
