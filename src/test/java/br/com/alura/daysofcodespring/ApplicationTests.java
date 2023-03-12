package br.com.alura.daysofcodespring;

import br.com.alura.daysofcodespring.MovieController.ListOfMovies;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldReturnTop250Movies() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:"+port+"/top250",
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	void shouldAddFavorite() {
		final String testId = "tt0111161";
		ResponseEntity<ListOfMovies> response = restTemplate.postForEntity(
				"http://localhost:"+port+"/favorite/"+testId, null, ListOfMovies.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().items().stream().anyMatch(m -> m.id().equals(testId)));
	}

	@Test
	void shouldGetFavorites() {
		ResponseEntity<ListOfMovies> response = restTemplate.getForEntity(
				"http://localhost:"+port+"/favorite",
				ListOfMovies.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}
}
