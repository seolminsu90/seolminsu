package com.seol.service.impl;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.seol.entity.rest.Search;
import com.seol.service.RestService;

@Service
public class RestServiceImpl implements RestService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String kakaoKey;

    @Value("${kakao.api.url}")
    private String kakaoUrl;

    @Override
    public Search searchKeyword(String query, Integer page) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + kakaoKey);
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(kakaoUrl).queryParam("query", query)
                    .queryParam("page", (page == null) ? 1 : page).build(false);

            ResponseEntity<Search> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    new HttpEntity<String>(headers), Search.class);
            return result.getBody();
        } catch (Exception e) {
            //해야함 exception
            e.printStackTrace();
            return null;
        }
    }
}
