package software.ulpgc.katas.infrastructure.adapters;

import software.ulpgc.katas.architecture.domain.Pokemon;
import software.ulpgc.katas.architecture.ports.PokemonDisplay;

public class ConsolePokemonDisplay implements PokemonDisplay {

    @Override
    public void show(Pokemon pokemon) {
        System.out.println("=========================================");
        System.out.printf(" #%03d - %s%n", pokemon.id(), pokemon.name().toUpperCase());
        System.out.printf(" Tipos  : %s%n", String.join(", ", pokemon.types()));
        System.out.printf(" Altura : %.2f m | Peso: %.2f kg%n", pokemon.heightMeters(), pokemon.weightKg());
        System.out.println("=========================================");
    }

    @Override
    public void showError(String message) {
        System.err.println("[ERROR] " + message);
    }
}