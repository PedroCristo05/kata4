package software.ulpgc.katas.architecture.ports;

import software.ulpgc.katas.architecture.domain.Pokemon;

public interface PokemonDisplay {
    void show(Pokemon pokemon);

    void showError(String message);
}