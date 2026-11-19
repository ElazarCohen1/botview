package fr.uge.llm;


import fr.uge.llm.model.LlmResponse;

import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.Objects;

@Singleton
public class LlmParser {

  private final ObjectMapper objectMapper;

  public LlmParser(ObjectMapper objectMapper) {
    this.objectMapper = Objects.requireNonNull(objectMapper);
  }

  /**
   * Parses the LLM JSON response.
   *
   * @param response raw JSON response from the LLM
   * @return parsed LLM response
   */
  public LlmResponse parseLlmResponse(String response) {
    Objects.requireNonNull(response);

    try {
      return objectMapper.readValue(response, LlmResponse.class);
    } catch (IOException e) {
      throw new IllegalArgumentException("Unable to parse LLM response" + response, e);
    }
  }
}