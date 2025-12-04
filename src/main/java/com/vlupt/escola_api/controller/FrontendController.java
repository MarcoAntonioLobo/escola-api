package com.vlupt.escola_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping(value = {"/", "/clients", "/data"})
    public String index() {
        return "index.html";
    }
}
