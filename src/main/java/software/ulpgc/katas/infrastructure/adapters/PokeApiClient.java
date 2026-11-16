package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.architecture.domain.Pokemon;
import software.ulpgc.katas.architecture.domain.PokemonFetchException;
import software.ulpgc.katas.architecture.ports.PokemonRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletionException;

public class PokeApiClient implements PokemonRepository {

    private final String baseUrl;
    private final HttpClient httpClient;
    private final PokeApiParser parser;

    public PokeApiClient(String baseUrl, PokeApiParser parser) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.parser = parser;
    }

    @Override
    public Pokemon findByNameOrId(String query) {
        HttpRequest request = buildRequest(query);
        HttpResponse<String> response = sendRequest(request);
        validateStatus(response.statusCode(), query);
        return parser.parse(response.body());
    }

    private void validateStatus(int statusCode, String query) {
        if (statusCode == 404) {
            throw new PokemonFetchException("No se encontró ningún Pokemon con identificador: " + query + " (HTTP 404).");
        }
        if (statusCode < 200 || statusCode >= 300) {
            throw new PokemonFetchException("Error inesperado del servidor remoto (HTTP " + statusCode + ").");
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join();
        } catch (CompletionException e) {
            throw new PokemonFetchException("Servicio no disponible o error de red al conectar con: " + baseUrl, e.getCause());
        }
    }

    private HttpRequest buildRequest(String query) {
        String normalizedQuery = query.trim().toLowerCase();
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/pokemon/" + normalizedQuery))
                .GET()
                .build();
    }
}
