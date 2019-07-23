package com.seol.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    // 메인 뷰 페이지
    @GetMapping(path = "/")
    public String index() {
        return "index.html";
    }
}
