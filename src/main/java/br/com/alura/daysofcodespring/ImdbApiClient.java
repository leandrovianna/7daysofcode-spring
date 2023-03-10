package br.com.alura.daysofcodespring;

import br.com.alura.daysofcodespring.MovieController.ListOfMovies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImdbApiClient {

    @Value("${imdb.apikey}")
    String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    public ListOfMovies getBody() {
        String url = "https://imdb-api.com/API/Top250Movies/" + apiKey;

        ResponseEntity<ListOfMovies> response = restTemplate.getForEntity(url, ListOfMovies.class);
        return response.getBody();
    }
}
