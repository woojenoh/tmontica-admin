package com.internship.tmontica_admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoutingController {

    @GetMapping("/signin")
    public String signin(){
        return "/index.html";
    }
}
