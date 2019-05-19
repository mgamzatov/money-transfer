package ru.magomed.gamzatov.money.transfer.helper

import com.fasterxml.jackson.databind.ObjectMapper
import spark.ResponseTransformer

class JsonTransformer(private val objectMapper: ObjectMapper) : ResponseTransformer {
    /**
     * Generates a JSON from a Java object and returns the generated JSON as a string.
     *
     * @param model Java object
     * @return JSON as a string
     */
    override fun render(model: Any?): String? = model?.let(objectMapper::writeValueAsString)
}