package com.juanm.backendapi.integration.gemini;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GeminiPreguntasClientTest {
    private static final ValidatorFactory VALIDADORES = Validation.buildDefaultValidatorFactory();
    private static final String TEXTO = "La Tierra orbita alrededor del Sol.";
    private static final String URL_BASE = "https://generativelanguage.googleapis.com";
    private static final String URL_GENERACION = URL_BASE + "/v1beta/models/modelo-prueba:generateContent";
    private final JsonMapper json = JsonMapper.builder().build();
    private final RestClient.Builder builder = RestClient.builder().baseUrl(URL_BASE);
    private final MockRestServiceServer servidor = MockRestServiceServer.bindTo(builder).build();
    private final GeminiPreguntasClient cliente = new GeminiPreguntasClient(
            builder.build(), json, VALIDADORES.getValidator(), "clave-prueba", "modelo-prueba");

    @AfterAll
    static void cerrarValidadores() {
        VALIDADORES.close();
    }

    @Test
    void enviaTextoYEsquemaJsonYLeeLaRespuesta() {
        responderCon(List.of("La Luna", "El Sol", "Marte", "Júpiter"));

        var resultado = cliente.generar(TEXTO);

        assertEquals(1, resultado.size());
        assertEquals("El Sol", resultado.getFirst().opciones().get(resultado.getFirst().correcta() - 1));
        servidor.verify();
    }

    @Test
    void informaElLimiteDeUsoSinReintentar() {
        servidor.expect(requestTo(URL_GENERACION)).andRespond(withStatus(HttpStatus.TOO_MANY_REQUESTS));

        var error = assertThrows(ResponseStatusException.class, () -> cliente.generar(TEXTO));

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, error.getStatusCode());
        servidor.verify();
    }

    @Test
    void distingueElRechazoHttpDeUnCuestionarioInvalido() {
        servidor.expect(requestTo(URL_GENERACION)).andRespond(withStatus(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {"error":{"status":"INVALID_ARGUMENT","message":"Dato interno de la petición"}}
                        """));

        var error = assertThrows(ResponseStatusException.class, () -> cliente.generar(TEXTO));

        assertEquals(HttpStatus.BAD_GATEWAY, error.getStatusCode());
        assertEquals("Gemini rechazó la petición de generación. (Gemini HTTP 400, INVALID_ARGUMENT)", error.getReason());
        assertFalse(error.getMessage().contains("Dato interno"));
        servidor.verify();
    }

    @Test
    void rechazaPreguntasConOpcionesRepetidas() {
        responderCon(List.of("La Luna", "El Sol", "Marte", " el sol "));

        var error = assertThrows(ResponseStatusException.class, () -> cliente.generar(TEXTO));

        assertEquals(HttpStatus.BAD_GATEWAY, error.getStatusCode());
        servidor.verify();
    }

    private void responderCon(List<String> opciones) {
        responderCon(opciones, 2);
    }

    @Test
    void rechazaUnaRespuestaCorrectaDeTipoTexto() {
        responderCon(List.of("La Luna", "El Sol", "Marte", "Júpiter"), "2");

        var error = assertThrows(ResponseStatusException.class, () -> cliente.generar(TEXTO));

        assertEquals(HttpStatus.BAD_GATEWAY, error.getStatusCode());
        servidor.verify();
    }

    private void responderCon(List<String> opciones, Object correcta) {
        String cuestionario = json.writeValueAsString(Map.of("preguntas", List.of(Map.of(
                "enunciado", "¿Alrededor de qué astro orbita la Tierra?",
                "tema", "Astronomía",
                "retroalimentacion", TEXTO,
                "correcta", correcta,
                "opciones", opciones))));
        String respuesta = json.writeValueAsString(Map.of("candidates", List.of(
                Map.of("finishReason", "STOP", "content", Map.of("parts", List.of(
                        Map.of("thought", true, "text", "Texto interno"),
                        Map.of("text", cuestionario)))))));
        servidor.expect(requestTo(URL_GENERACION))
                .andExpect(header("x-goog-api-key", "clave-prueba"))
                .andExpect(jsonPath("$.contents[0].parts[0].text").value(TEXTO))
                .andExpect(jsonPath("$.generationConfig.responseMimeType").value("application/json"))
                .andExpect(jsonPath("$.generationConfig.responseJsonSchema.properties.preguntas.maxItems").value(5))
                .andRespond(withSuccess(respuesta, MediaType.APPLICATION_JSON));
    }
}
