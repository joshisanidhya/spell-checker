package com.shuddhai.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.shuddhai.model.SpellCheckResponse;
import com.shuddhai.spell.SpellChecker;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class RequestHandler implements HttpHandler {
    private static final Gson GSON = new Gson();
    private final SpellChecker spellChecker;

    public RequestHandler(SpellChecker spellChecker) { this.spellChecker = spellChecker; }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        addCors(exchange);
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { send(exchange, 204, ""); return; }
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendError(exchange, 405, "Method not allowed"); return;
        }
        try (InputStream body = exchange.getRequestBody()) {
            JsonObject request = GSON.fromJson(new String(body.readAllBytes(), StandardCharsets.UTF_8), JsonObject.class);
            if (request == null || !request.has("text") || request.get("text").isJsonNull()) {
                sendError(exchange, 400, "Request must include text"); return;
            }
            String text = request.get("text").getAsString();
            if (text.trim().isEmpty()) { sendError(exchange, 400, "Text must not be empty"); return; }
            SpellCheckResponse response = spellChecker.check(text);
            send(exchange, 200, GSON.toJson(response));
        } catch (JsonParseException | IllegalStateException exception) {
            sendError(exchange, 400, "Invalid JSON request");
        } catch (Exception exception) {
            sendError(exchange, 500, "Unable to process request");
        }
    }

    private static void addCors(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private static void sendError(HttpExchange exchange, int status, String message) throws IOException {
        JsonObject error = new JsonObject(); error.addProperty("error", message);
        send(exchange, status, GSON.toJson(error));
    }

    private static void send(HttpExchange exchange, int status, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (var output = exchange.getResponseBody()) { output.write(bytes); }
    }
}