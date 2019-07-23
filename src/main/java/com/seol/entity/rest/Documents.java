package com.seol.entity.rest;

import java.util.List;

import lombok.Data;

@Data
public class Documents {
    // 제목, 도서 썸네일, 소개, ISBN, 저자, 출판사, 출판일, 정가, 판매가
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
