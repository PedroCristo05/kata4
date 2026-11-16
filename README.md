# Kata 4 - Cliente para consumir un servicio HTTP

El objetivo de esta entrega es construir un cliente HTTP desacoplado bajo **Arquitectura Hexagonal** que consuma la API pública **PokéAPI** (`https://pokeapi.co/api/v2`) sin credenciales, transforme las respuestas JSON en objetos de dominio inmutables y gestione de forma explícita y controlada los fallos de red, errores HTTP y respuestas malformadas.

---

## Enlace al vídeo explicativo

- **Vídeo:** [https://youtu.be/AdFSTRxtbJ4?si=rUk0Qqxy-nb6nr4w]

---

## Entorno y Dependencias

- **Lenguaje:** Java 26 (compatible con JDK 21+)
- **Gestor de construcción:** Maven
- **Cliente HTTP:** `java.net.http.HttpClient` (nativo de la biblioteca estándar de Java, con peticiones asíncronas mediante `sendAsync`)
- **Biblioteca JSON:** Google Gson (`com.google.code.gson:gson:2.12.0`)
- **Servicio externo consumido:** PokéAPI (`https://pokeapi.co/api/v2/pokemon/{id_o_nombre}`), servicio REST público sin autenticación

---

---

## Estructura de la Entrega (Arquitectura Hexagonal)

El proyecto separa estrictamente el núcleo de la aplicación (`architecture`, libre de dependencias externas) de los detalles técnicos de red, parseo y consola (`infrastructure`):

```text
src/main/java/software/ulpgc/katas/
├── architecture/
│   ├── domain/
│   │   ├── Pokemon.java               # Record inmutable de dominio (id, name, heightMeters, weightKg, types).
│   │   └── PokemonFetchException.java # Excepción de dominio para transportar errores controlados sin acoplar tecnología.
│   ├── ports/
│   │   ├── PokemonRepository.java     # Puerto de servicio (contrato para obtener un Pokémon sin mencionar HTTP/JSON).
│   │   └── PokemonDisplay.java        # Puerto de salida/presentación para mostrar resultados y errores.
│   └── usecases/
│       └── ConsultPokemonUseCase.java # Caso de uso que orquesta el flujo (Repository -> Display).
├── infrastructure/
│   └── adapters/
│       ├── PokeApiClient.java         # Adaptador HTTP con HttpClient (sendAsync), URL base configurable y validación de códigos HTTP.
│       ├── PokeApiParser.java         # Mapper JSON -> Dominio mediante DTOs internos con Gson.
│       └── ConsolePokemonDisplay.java # Adaptador de salida formateada por consola (System.out / System.err).
└── Main.java                          # Ensamblador (Inyección de Dependencias) y configuración centralizada de la URL base.
```

---

## Configuración de URL Base, Compilación y Ejecución

### 1. Clonar y compilar desde cero
- **Desde IntelliJ IDEA:** File -> New -> Project from Version Control -> pegar la URL del repositorio y entrar

### 2. Ejecutar el cliente
- **Desde IntelliJ IDEA:** Abriendo `Main.java` y pulsando `Shift + F10`.

---

## Evidencias de Verificación (Caso Normal y Errores Controlados)

Al ejecutar `Main`, se comprueban automáticamente tanto consultas válidas como los tres escenarios de fallo exigidos:

```text
=== 1. Consulta válida (pikachu) ===
=========================================
 #025 - PIKACHU
 Tipos  : electric
 Altura : 0,40 m | Peso: 6,00 kg
=========================================

=== 2. Consulta por ID válido (6 - Charizard) ===
=========================================
 #006 - CHARIZARD
 Tipos  : fire, flying
 Altura : 1,70 m | Peso: 90,50 kg
=========================================

=== 3. Error controlado: Pokémon inexistente (HTTP 404) ===
[ERROR] No se encontró ningún Pokémon con identificador: 'pokemon_que_no_existe' (HTTP 404).

=== 4. Error controlado: Servicio no disponible / URL caída ===
[ERROR] Servicio no disponible o error de red al conectar con: https://servicio.caido.local/api/v2

=== 5. Error controlado: Respuesta JSON malformada ===
[ERROR] Respuesta con sintaxis JSON inválida.
```

---

## Repetición Deliberada y Flujo Git

La kata se ha repetido en tres ocasiones independientes con alcance equivalente para consolidar la Arquitectura Hexagonal y el manejo de `HttpClient` en IntelliJ IDEA:

1. **`develop1`:** Primera implementación completa de la arquitectura hexagonal, validación de casos de error y sincronización de flujos de consola.
2. **`develop2`:** Segunda repetición desde el modelo base (`Pokemon`) para afianzar atajos del IDE y estructura de commits atómicos.
3. **`develop3`:** Iteración final grabada en vídeo e integrada en la rama principal `master` mediante `git merge`.
