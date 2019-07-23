package com.seol.entity.rest;

import java.util.List;

import lombok.Data;

@Data
public class Documents {
    private String title;
    private String thumbnail;
    private String contents;
    private String isbn;
    private List<String> authors;
    private String publisher;
    private String datetime;
    private Integer price;
    private Integer sale_price;
}
