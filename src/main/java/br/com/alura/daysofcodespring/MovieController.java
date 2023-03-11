package br.com.alura.daysofcodespring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

@RestController
public class MovieController {

    @Autowired
    private ImdbApiClient imdbApiClient;

    private List<Movie> movies;

    @GetMapping("/top250")
    public List<Movie> getTop250Movies(@RequestParam Optional<String> title) {
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

        return filteredMovies;
    }

    record ListOfMovies(List<Movie> items, String errorMessage) {}
}
