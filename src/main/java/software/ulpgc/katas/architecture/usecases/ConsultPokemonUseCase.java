package software.ulpgc.katas.architecture.usecases;

import software.ulpgc.katas.architecture.domain.Pokemon;
import software.ulpgc.katas.architecture.domain.PokemonFetchException;
import software.ulpgc.katas.architecture.ports.PokemonDisplay;
import software.ulpgc.katas.architecture.ports.PokemonRepository;

public class ConsultPokemonUseCase {
    private final PokemonRepository repository;
    private final PokemonDisplay display;

    public ConsultPokemonUseCase(PokemonRepository repository, PokemonDisplay display) {
        this.repository = repository;
        this.display = display;
    }

    public void execute(String query) {
        try {
            Pokemon pokemon = repository.findByNameOrId(query);
            display.show(pokemon);
        } catch (PokemonFetchException e) {
            display.showError(e.getMessage());
        }
    }
}