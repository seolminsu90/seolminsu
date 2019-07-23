package com.seol.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "TB_USER")
public class User implements Serializable {
    @Id
    private String id;
    @JsonIgnore
    private String pwd;
}
