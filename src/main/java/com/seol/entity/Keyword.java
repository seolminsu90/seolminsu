package com.seol.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicInsert;

import lombok.Data;

@Data
@Entity
@Table(name = "TB_SEARCH_KEYWORD")
@DynamicInsert
public class Keyword implements Serializable {
    @Id
    @Size(max = 50)
    @NotNull
    private String keyword;
    private Long searchCount;
}
