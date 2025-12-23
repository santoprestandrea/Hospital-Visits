package com.hospital.hospital_visits.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/*
*
* This class allows us to return to the home.html page and
*  redirect to the "/" pages, that is "home.html"
*
* */
@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        /*
        * this return search "home.html"
        * */
        return "home";
    }
}
