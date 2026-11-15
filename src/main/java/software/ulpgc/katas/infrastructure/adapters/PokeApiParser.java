package software.ulpgc.katas.infrastructure.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import software.ulpgc.katas.architecture.domain.Pokemon;
import software.ulpgc.katas.architecture.domain.PokemonFetchException;

import java.util.List;

public class PokeApiParser {
    private final Gson gson = new Gson();

    public Pokemon parse(String jsonBody) {
        if (jsonBody == null || jsonBody.isBlank()) {
            throw new PokemonFetchException("La respuesta recibida del servicio está vacía.");
        }
        try {
            PokeApiDto dto = gson.fromJson(jsonBody, PokeApiDto.class);
            if (dto == null || dto.name == null || dto.types == null) {
                throw new PokemonFetchException("El formato JSON recibido no corresponde a un Pokémon válido.");
            }
            return toDomain(dto);
        } catch (JsonSyntaxException e) {
            throw new PokemonFetchException("Respuesta con sintaxis JSON inválida.", e);
        }
    }

    private Pokemon toDomain(PokeApiDto dto) {
        List<String> typeNames = dto.types.stream()
                .map(slot -> slot.type.name)
                .toList();
        return new Pokemon(
                dto.id,
                dto.name,
                dto.height / 10.0,
                dto.weight / 10.0,
                typeNames
        );
    }

    private record PokeApiDto(int id, String name, int height, int weight, List<TypeSlotDto> types) {}

    private record TypeSlotDto(TypeDetailDto type) {}

    private record TypeDetailDto(String name) {}
}