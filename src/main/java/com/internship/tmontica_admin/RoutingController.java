package com.internship.tmontica_admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoutingController {

    @GetMapping(value={"/signin", "/order", "/menus", "/stat", "/banner"})
    public String routing(){
        return "/index.html";
    }

}
