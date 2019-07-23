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

	@GetMapping("/api/keyword")
	public Response<List<Keyword>> popularKeyword() {
		return searchService.popularKeyword();
	}

	@GetMapping("/api/history")
	public Response<List<History>> keywordHistory(HttpServletRequest request, @RequestAttribute String id) {
		return searchService.keywordHistory(id);
	}

	@GetMapping("/api/search")
	public Response<Search> searchBook(@RequestParam(value = "query", required = true) String query,
			@RequestParam(value = "page", required = false) Integer page, @RequestAttribute String id) {
		return searchService.searchBooks(id, query, page);
	}
}
