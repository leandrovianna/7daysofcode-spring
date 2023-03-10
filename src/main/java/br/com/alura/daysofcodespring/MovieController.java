package br.com.alura.daysofcodespring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/top250")
    public String getTop250Movies() {
        String apiKey = System.getenv("IMDB_API_KEY");
        if (apiKey == null)
            throw new RuntimeException("IMDB_API_KEY is not defined");

        String url = "https://imdb-api.com/API/Top250Movies/" + apiKey;

        ResponseEntity<ListOfMovies> response = restTemplate.getForEntity(url, ListOfMovies.class);

        Writer writer = new StringWriter();
        HTMLGenerator generator = new HTMLGenerator(writer);
        try {
            generator.generate(response.getBody().items());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return writer.toString();
    }

    record ListOfMovies(List<Movie> items, String errorMessage) {}
}
