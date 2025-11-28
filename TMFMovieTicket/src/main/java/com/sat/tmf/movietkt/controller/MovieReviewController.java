package com.sat.tmf.movietkt.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sat.tmf.movietkt.entities.Movie;
import com.sat.tmf.movietkt.entities.MovieReview;
import com.sat.tmf.movietkt.entities.User;
import com.sat.tmf.movietkt.service.MovieReviewService;
import com.sat.tmf.movietkt.service.MovieService;
import com.sat.tmf.movietkt.service.UserService;

@Controller
@RequestMapping("/movies")
public class MovieReviewController {

    @Autowired private MovieService movieService;
    @Autowired private MovieReviewService reviewService;
    @Autowired private UserService userService;

    @GetMapping("/{id}")
    public String movieDetails(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findById(id);
        if (movie == null) {
            model.addAttribute("message", "Movie not found");
            model.addAttribute("contentPage", "/WEB-INF/views/pages/notFound.jsp");
            model.addAttribute("pageTitle", "Movie Not Found");
            return "layout/layout";
        }

        List<MovieReview> reviews = reviewService.findByMovie(movie);
        Double avgRating = reviewService.getAverageRating(movie);

        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviews);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("contentPage", "/WEB-INF/views/pages/movieDetails.jsp");
        model.addAttribute("pageTitle", movie.getTitle() != null ? movie.getTitle() : "Movie Details");
        return "layout/layout";
    }

    @PostMapping("/{id}/review")
    @ResponseBody
    public String submitReview(@PathVariable Integer id,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               Principal principal) {
        if (principal == null) {
            return "ERROR: Login required";
        }

        Movie movie = movieService.findById(id);
        if (movie == null) {
            return "ERROR: Movie not found";
        }

        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            return "ERROR: User not found";
        }

        reviewService.addReview(movie, user, rating, comment);
        return "OK";
    }
}