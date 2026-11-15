package software.ulpgc.katas.architecture.ports;

import software.ulpgc.katas.architecture.domain.Pokemon;

public interface PokemonRepository {
    Pokemon findByNameOrId(String query);
}