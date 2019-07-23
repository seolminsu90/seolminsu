package com.seol.service;

import com.seol.entity.rest.Search;

public interface RestService {
    public Search searchKeyword(String query, Integer page);
}
