package com.sat.tmf.movietkt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sat.tmf.movietkt.entities.Movie;
import com.sat.tmf.movietkt.entities.Show;
import com.sat.tmf.movietkt.service.MovieService;
import com.sat.tmf.movietkt.service.ShowService;

@Controller
public class UserMovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ShowService showService;

    @GetMapping("/movies")
    public String listMovies(Model model, @RequestParam(required = false) String search) {
        List<Movie> movies;
        if (search != null && !search.isEmpty()) {
            movies = movieService.searchMovies(search);
        } else {
            movies = movieService.findAllMovies();
        }
        model.addAttribute("movies", movies);
        model.addAttribute("search", search);
        model.addAttribute("contentPage", "/WEB-INF/views/pages/movies.jsp");
        model.addAttribute("pageTitle", "Now Showing");
        return "layout/layout";
    }

    @GetMapping("/movies/{id}/shows")
    public String listShowsForMovie(@PathVariable Integer id, Model model) {
        Movie movie = movieService.findById(id);
        if (movie == null) {
            model.addAttribute("message", "Movie not found");
            model.addAttribute("contentPage", "/WEB-INF/views/pages/notFound.jsp");
            model.addAttribute("pageTitle", "Movie Not Found");
            return "layout/layout";
        }
        List<Show> shows = showService.findUpcomingShows(movie);
        model.addAttribute("movie", movie);
        model.addAttribute("shows", shows);
        model.addAttribute("contentPage", "/WEB-INF/views/pages/movieShows.jsp");
        model.addAttribute("pageTitle", movie.getTitle() + " - Showtimes");
        return "layout/layout";
    }
}