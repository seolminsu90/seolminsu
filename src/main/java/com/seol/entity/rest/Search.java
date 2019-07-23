package com.seol.entity.rest;

import java.util.List;

import lombok.Data;

@Data
public class Search {
    private Meta meta;
    private List<Documents> documents;
}
