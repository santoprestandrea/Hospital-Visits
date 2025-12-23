
create database hospital_visits_db;

CREATE TABLE person(
    cf VARCHAR(16) PRIMARY KEY,
    first_name VARCHAR(60) NOT NULL,
    surname VARCHAR(60) NOT NULL,
    address VARCHAR(120),
    birth_date DATE 
);

CREATE TABLE patient(
    cf VARCHAR(16) PRIMARY KEY,
    FOREIGN KEY(cf)references person(cf)
);

CREATE TABLE department(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255)

);

CREATE TABLE specialization(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255) 
);


CREATE TABLE doctor(
    cf VARCHAR(16) PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    department_id BIGINT,
    specialization_id BIGINT,
    FOREIGN KEY(cf)references person(cf),
    FOREIGN KEY(specialization_id)references specialization(id),
    FOREIGN KEY(department_id)references department(id)
);

CREATE TABLE visit_type(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE clinic_room(
    id BIGSERIAL PRIMARY KEY,
    address VARCHAR(255)
);

CREATE TABLE visit(
    id BIGSERIAL PRIMARY key,
    date DATE NOT NULL,
    department_id BIGINT,
    visit_type_id BIGINT,
    FOREIGN KEY(department_id)references department(id),
    FOREIGN KEY(visit_type_id)references visit_type(id)
);

CREATE TABLE app_user(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    profile_image_url VARCHAR(255),
    registration_date TIMESTAMP NOT NULL DEFAULT NOW(),
    patient_cf VARCHAR(16),
    FOREIGN KEY(patient_cf)references patient(cf)
);

CREATE TABLE works_in(
    doctor_cf VARCHAR(16) NOT NULL,
    clinic_room_id BIGINT NOT NULL,
    PRIMARY KEY(doctor_cf, clinic_room_id),
    FOREIGN KEY(doctor_cf)references doctor(cf) ON DELETE CASCADE,
    FOREIGN KEY(clinic_room_id)references clinic_room(id) ON DELETE CASCADE

);

CREATE TABLE doctor_visit(
    doctor_cf VARCHAR(16) NOT NULL,
    visit_id BIGINT NOT NULL,
    PRIMARY KEY(doctor_cf, visit_id),
    FOREIGN KEY(doctor_cf)references doctor(cf) ON DELETE CASCADE,
    FOREIGN KEY(visit_id)references visit(id) ON DELETE CASCADE
);

CREATE TABLE patient_visit(
    patient_cf VARCHAR(16) NOT NULL,
    visit_id BIGINT NOT NULL,
    PRIMARY KEY(patient_cf, visit_id),
    FOREIGN KEY(patient_cf)references patient(cf) ON DELETE CASCADE,
    FOREIGN KEY(visit_id)references visit(id) ON DELETE CASCADE

);




