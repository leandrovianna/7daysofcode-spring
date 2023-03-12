package br.com.alura.daysofcodespring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class MovieController {

    @Autowired
    private ImdbApiClient imdbApiClient;

    private List<Movie> movies;
    private final Set<Movie> favorites = new HashSet<>();

    @GetMapping("/top250")
    public ListOfMovies getTop250Movies(@RequestParam Optional<String> title) {
        if (movies == null) {
            ListOfMovies response = imdbApiClient.getBody();
            movies = response.items;
        }

        final List<Movie> filteredMovies = movies.stream()
                .filter(m -> m.title().contains(title.orElse("")))
                .toList();

        try {
            Writer writer = new PrintWriter("src/main/resources/content.html");
            new HTMLGenerator(writer).generate(filteredMovies);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ListOfMovies(filteredMovies, "");
    }

    @PostMapping("/favorite/{id}")
    public ListOfMovies addFavoriteMovie(@PathVariable String id) {
        if (movies != null) {
            movies.stream()
                    .filter(m -> m.id().equals(id))
                    .forEach(m -> favorites.add(m));
        }

        return new ListOfMovies(favorites.stream().toList(), "");
    }

    @GetMapping("/favorite")
    public ListOfMovies listFavorites() {
        return new ListOfMovies(favorites.stream().toList(), "");
    }

    record ListOfMovies(List<Movie> items, String errorMessage) {}
}
