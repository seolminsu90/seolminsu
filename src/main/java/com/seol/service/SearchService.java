package com.seol.service;

import java.util.List;

import com.seol.dto.Response;
import com.seol.entity.History;
import com.seol.entity.Keyword;
import com.seol.entity.rest.Search;

public interface SearchService {
    public Response<List<Keyword>> popularKeyword();
    public Response<List<History>> keywordHistory(String id);
    public Response<Search> searchBooks(String id, String query, Integer page);
}
