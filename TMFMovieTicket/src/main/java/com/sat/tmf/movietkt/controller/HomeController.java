package com.sat.tmf.movietkt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "🎬 Welcome to TMF Movie Ticket App with Spring Boot MVC");
      
     
        model.addAttribute("contentPage", "/WEB-INF/views/pages/home.jsp"); 
        model.addAttribute("pageTitle", "Home");

        return "layout/layout"; // This returns your main layout JSP file
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        // Serve the login page directly at /login (aligns with Spring Security default processing URL)
        model.addAttribute("pageTitle", "Login");
        return "pages/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle", "Register");
        return "pages/register";
    }
}