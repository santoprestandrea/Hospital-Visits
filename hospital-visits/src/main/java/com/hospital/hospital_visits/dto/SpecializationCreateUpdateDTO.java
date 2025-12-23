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
public class SpecializationCreateUpdateDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
