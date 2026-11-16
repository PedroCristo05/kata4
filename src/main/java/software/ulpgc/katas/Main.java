package software.ulpgc.katas;

import software.ulpgc.katas.architecture.domain.PokemonFetchException;
import software.ulpgc.katas.architecture.ports.PokemonDisplay;
import software.ulpgc.katas.architecture.ports.PokemonRepository;
import software.ulpgc.katas.architecture.usecases.ConsultPokemonUseCase;
import software.ulpgc.katas.infrastructure.adapters.ConsolePokemonDisplay;
import software.ulpgc.katas.infrastructure.adapters.PokeApiClient;
import software.ulpgc.katas.infrastructure.adapters.PokeApiParser;

public class Main {
    private static final String DEFAULT_BASE_URL = "https://pokeapi.co/api/v2";

    public static void main(String[] args) {
        String baseUrl = args.length > 0 ? args[0] : DEFAULT_BASE_URL;

        PokemonDisplay display = new ConsolePokemonDisplay();
        PokeApiParser parser = new PokeApiParser();
        PokemonRepository repository = new PokeApiClient(baseUrl, parser);
        ConsultPokemonUseCase useCase = new ConsultPokemonUseCase(repository, display);

        System.out.println("=== 1. Consulta válida (pikachu) ===");
        useCase.execute("pikachu");

        System.out.println("\n=== 2. Consulta por ID válido (6 - Charizard) ===");
        useCase.execute("6");

        System.out.println("\n=== 3. Error controlado: Pokémon inexistente (HTTP 404) ===");
        useCase.execute("pokemon_que_no_existe");

        System.out.println("\n=== 4. Error controlado: Servicio no disponible / URL caída ===");
        PokemonRepository brokenRepository = new PokeApiClient("https://servicio.caido.local/api/v2", parser);
        new ConsultPokemonUseCase(brokenRepository, display).execute("pikachu");

        System.out.println("\n=== 5. Error controlado: Respuesta JSON malformada ===");
        try {
            parser.parse("{ json_corrupto_sin_cerrar ");
        } catch (PokemonFetchException e) {
            display.showError(e.getMessage());
        }
    }
}