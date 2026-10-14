package fr.uge.git.model;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;
import java.util.Objects;

@Serdeable
public record WebHooksDto(int id, String name, List<String> events) {
    public WebHooksDto {
        Objects.requireNonNull(name);
    }
}
