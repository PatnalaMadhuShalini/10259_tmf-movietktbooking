package com.sat.tmf.movietkt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sat.tmf.movietkt.entities.User;
import com.sat.tmf.movietkt.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // =================== REGISTER ===================
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        // Return the register page directly to avoid layout includes during login/register
        model.addAttribute("pageTitle", "Register");
        return "pages/register"; // resolves to /WEB-INF/views/pages/register.jsp
    }

//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute User user, Model model) {
//        try {
//            userService.register(user);
//            model.addAttribute("message", "Registration successful! You can now log in.");
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//        }
//        model.addAttribute("contentPage", "/WEB-INF/views/pages/register.jsp");
//        return "layout/layout";
//    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttrs) {
        try {
            userService.register(user);
            redirectAttrs.addFlashAttribute("message", "Registration successful! You can now log in.");
            // redirect to canonical /login
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
    }


    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout) {
        // Redirect to canonical /login to avoid duplicate mappings and redirect loops
        return "redirect:/login";
    }
    // =================== PROFILE ===================
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        // profile.jsp lives under pages directory
        model.addAttribute("contentPage", "/WEB-INF/views/pages/profile.jsp");
        model.addAttribute("pageTitle", "My Profile");
        return "layout/layout";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User user, Principal principal, Model model) {
        userService.updateUserProfile(principal.getName(), user);
        model.addAttribute("message", "Profile updated successfully!");
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("contentPage", "/WEB-INF/views/pages/profile.jsp");
        return "layout/layout";
    }

    // =================== ADMIN VIEW ALL USERS ===================
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        // adminUsers.jsp will be placed in WEB-INF/views/admin/
        model.addAttribute("contentPage", "/WEB-INF/views/admin/adminUsers.jsp");
        model.addAttribute("pageTitle", "All Users");
        return "layout/layout";
    }
}