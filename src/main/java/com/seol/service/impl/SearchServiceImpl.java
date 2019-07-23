package com.seol.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seol.dto.Response;
import com.seol.dto.ResponseCode;
import com.seol.entity.History;
import com.seol.entity.Keyword;
import com.seol.entity.rest.Search;
import com.seol.repository.HistoryRepository;
import com.seol.repository.KeywordRepository;
import com.seol.service.RestService;
import com.seol.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    RestService restService;

    @Override
    public Response<List<Keyword>> popularKeyword() {
        Response<List<Keyword>> response = new Response<>();
        response.setCode(ResponseCode.OK.getValue());
        response.setData(keywordRepository.findTop10ByOrderBySearchCountDesc());

        return response;
    }

    @Override
    public Response<List<History>> keywordHistory(String id) {
        Response<List<History>> response = new Response<>();
        response.setCode(ResponseCode.OK.getValue());
        response.setData(historyRepository.findTop10ByIdOrderByRegdateDesc(id));

        return response;
    }

    @Override
    public Response<Search> searchBooks(String id, String query, Integer page) {
        Response<Search> response = new Response<>();
        response.setCode(ResponseCode.OK.getValue());
        response.setData(restService.searchKeyword(query, page));

        // 초기 검색 시에만 Count 및 기록 저장
        if (page == null) {
            History history = new History();
            history.setId(id);
            history.setKeyword(query);
            history.setRegdate(LocalDateTime.now());
            historyRepository.saveAndFlush(history);

            keywordRepository.upsertCount(query);
        }
        return response;
    }

}
