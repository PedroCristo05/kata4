package software.ulpgc.katas.architecture.domain;

public class PokemonFetchException extends RuntimeException {
    public PokemonFetchException(String message) {
        super(message);
    }

    public PokemonFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}