package software.ulpgc.katas.architecture.domain;

import java.util.List;

public record Pokemon(
        int id,
        String name,
        double heightMeters,
        double weightKg,
        List<String> types
) {
}