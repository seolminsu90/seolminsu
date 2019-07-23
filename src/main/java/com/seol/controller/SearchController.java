package com.seol.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seol.dto.Response;
import com.seol.entity.History;
import com.seol.entity.Keyword;
import com.seol.entity.rest.Search;
import com.seol.service.SearchService;

@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    // 인기 키워드 목록
    @GetMapping("/api/keyword")
    public Response<List<Keyword>> popularKeyword() {
        return searchService.popularKeyword();
    }

    // 나의 검색 기록
    @GetMapping("/api/history")
    public Response<List<History>> keywordHistory(HttpServletRequest request, @RequestAttribute String id) {
        return searchService.keywordHistory(id);
    }

    // 책 검색
    @GetMapping("/api/search")
    public Response<Search> searchBook(@RequestParam(value = "query", required = true) String query,
            @RequestParam(value = "page", required = false) Integer page, @RequestAttribute String id) {
        return searchService.searchBooks(id, query, page);
    }
}
