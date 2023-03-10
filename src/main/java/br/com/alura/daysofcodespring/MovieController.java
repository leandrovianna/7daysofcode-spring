package br.com.alura.daysofcodespring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private ImdbApiClient imdbApiClient;

    @GetMapping("/top250")
    public ListOfMovies getTop250Movies() {
        ListOfMovies movies = imdbApiClient.getBody();

        try {
            Writer writer = new PrintWriter("src/main/resources/content.html");
            new HTMLGenerator(writer).generate(movies.items);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }

    record ListOfMovies(List<Movie> items, String errorMessage) {}
}
